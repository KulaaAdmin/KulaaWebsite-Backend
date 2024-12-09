package com.kula.kula_project_backend.service.impl;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dao.BookMarksRepository;
import com.kula.kula_project_backend.dao.FollowingGroupsRepository;
import com.kula.kula_project_backend.dao.ProfilesRepository;
import com.kula.kula_project_backend.dao.UsersRepository;
import com.kula.kula_project_backend.dto.requestdto.UsersDTO;
import com.kula.kula_project_backend.dto.responsedto.UsersResponseDTO;
import com.kula.kula_project_backend.entity.*;
import com.kula.kula_project_backend.query.UsersQuery;
import com.kula.kula_project_backend.security.JwtTokenProvider;
import com.kula.kula_project_backend.service.IUsersService;
import com.kula.kula_project_backend.service.TwilioService;
import com.kula.kula_project_backend.util.EmailUtil;
import org.bson.types.ObjectId;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.kula.kula_project_backend.common.converter.UsersResponseDTOConverter.convertToResponseDTO;
/**
 * Service implementation for Users operations.
 * This service is used to save, delete and get Users.
 * The service uses the UsersRepository to interact with the database.
 * The service uses the ProfilesRepository to interact with the database.
 * The service uses the BookMarksRepository to interact with the database.
 * The service uses the FollowingGroupsRepository to interact with the database.
 * The service uses the PasswordEncoder to encode the password.
 */
@Service
public class UsersServiceImpl implements IUsersService {

    private static final Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UsersRepository UsersRepository;
    @Autowired
    private ProfilesRepository profilesRepository;

    @Autowired
    BookMarksRepository bookMarksRepository;

    @Autowired
    private FollowingGroupsRepository followingGroupsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedissonClient redisson;

    @Autowired
    private TwilioService twilioService;

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;
    @Value("${spring.redis.database}")
    private int redisDatabase;
    @Value("${spring.redis.password}")
    private String redisPassword;
    /**
     * Authenticates a user and generates a JWT token for them.
     * @param emailOrPhoneNumber The email or phone number of the user.
     * @param password The password of the user.
     * @return ResponseResult object containing status, message and the JWT token.
     */
    @Override
    public ResponseResult login(String emailOrPhoneNumber, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(emailOrPhoneNumber, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 获取 UserDetails
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 使用 emailOrPhoneNumber 来确定用户,并获取必要信息
            Users user = UsersRepository.findByEmailOrPhoneNumber(emailOrPhoneNumber)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + emailOrPhoneNumber));

            String token = jwtTokenProvider.generateToken(authentication);

            Map<String, Object> authInfo = new HashMap<>();
            authInfo.put("userId", user.getId().toString());
            authInfo.put("token", token);
            log.info("Login successful for user: {}", userDetails.getUsername());
            return new ResponseResult(200, "Login successful", authInfo);

        } catch (AuthenticationException e) {
            log.error("Login failed for: {}. Error: {}", emailOrPhoneNumber, e.getMessage());
            return new ResponseResult(401, "Invalid email or password");
        }
    }



    /**
     * Saves a new user or updates an existing one.
     * @param usersDTO The DTO containing the user details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Transactional
    @Override
    public ResponseResult save(UsersDTO usersDTO) {

        Users users = new Users();

        if ("email".equals(usersDTO.getRegistrationMethod())) {
            String email = usersDTO.getEmail();
            if (email != null && UsersRepository.existsByEmail(email)) {
                return new ResponseResult(400, "Email already exists.");
            }
            users.setEmail(email);
            users.setPhoneNumber(null);

            // Check if the front end request has a valid verification code attached inside the request body.
            if (usersDTO.getVerificationCode() != null) {
                boolean ifEmailCodeValid = this.checkEmailCode(email, usersDTO.getVerificationCode());
                if (!ifEmailCodeValid) {
                    return new ResponseResult(400, "Invalid verification code");
                } else {
                    // If the verification code is valid, the code will be removed from the Redis database.
                    RBucket<String> bucket = redisson.getBucket(email);
                    bucket.delete();
                }
            } else {
                return new ResponseResult(400, "Verification code is required.");
            }

        } else if ("phone".equals(usersDTO.getRegistrationMethod())) {
            String phoneNumber = usersDTO.getPhoneNumber();
            if (phoneNumber != null && UsersRepository.existsByPhoneNumber(phoneNumber)) {
                return new ResponseResult(400, "Phone number already exists.");
            }
            users.setPhoneNumber(phoneNumber);
            users.setEmail(null);
        } else {
            return new ResponseResult(400, "Invalid registration method.");
        }

        users.setFirstName(usersDTO.getFirstName());
        users.setLastName(usersDTO.getLastName());
        users.setUsername(usersDTO.getUsername());
        users.setPasswordHash(passwordEncoder.encode(usersDTO.getPassword()));
        users.setAdmin(!usersDTO.isAdmin());
        users.setSuspend(false);
        users.setCreatedAt(new Date());
        users.setUpdatedAt(new Date());

        UsersRepository.insert(users);



        if(users.getId() != null) {
            // After successfully set up a new user account, the suer service will also create a new profile for the user.
            Profiles profiles = new Profiles();
            profiles.setUserId(users.getId());
            profiles.setProfileImageURL("doge.jpeg");
            profiles.setUserLevels(1);
            profiles.setUserPoints(0);
            profiles.setPrivateProfile(profiles.isPrivateProfile());
            profiles.setFavoriteDishIds(new ObjectId[0]);
            profiles.setFavoriteCuisinesIds(new ObjectId[0]);
            profiles.setDeliveryOptionsIds(new ObjectId[0]);
            profilesRepository.insert(profiles);
            if (profiles.getId() != null) {
                users.setProfileId(profiles.getId());
                UsersRepository.save(users);
            }

            BookMarks bookMarks = new BookMarks();
            bookMarks.setUserId(users.getId());
            bookMarks.setPostIds(new ObjectId[0]);
            bookMarks.setCollectionName("Saved Posts");
            bookMarksRepository.insert(bookMarks);
            if (bookMarks.getId() != null) {
                users.setBookMarksId(bookMarks.getId());
                UsersRepository.save(users);
            }

            FollowingGroups followingGroups = new FollowingGroups();
            followingGroups.setOwnerId(users.getId());
            followingGroups.setUserIds(new ObjectId[0]);
            followingGroupsRepository.insert(followingGroups);
            if (followingGroups.getId() != null) {
                users.setFollowingGroupsId(followingGroups.getId());
                UsersRepository.save(users);
            }


            return new ResponseResult(200, "success", users.getId().toString());
        }
        return new ResponseResult(400, "fail");


    }
    /**
     * Retrieves all users.
     * @return ResponseResult object containing status, message and all users.
     */
    @Override
    public ResponseResult getAll() {
        List<Users> users = UsersRepository.findAll();
        List<UsersResponseDTO> responseList = new ArrayList<UsersResponseDTO>();
        for (Users user : users) {
            responseList.add(convertToResponseDTO(user));

        }

        return new ResponseResult(200, "success", responseList);

    }
    /**
     * Updates a user.
     * @param usersDTO The DTO containing the updated user details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult update(UsersDTO usersDTO) {

        Optional<Users> users = UsersRepository.findById(usersDTO.getId());
        if (users.isPresent()) {
            Users user = users.get();
            if (usersDTO.getEmail() != null) {
                user.setEmail(usersDTO.getEmail());
            }
            if (usersDTO.getPhoneNumber() != null) {
                user.setPhoneNumber(usersDTO.getPhoneNumber());
            }
            if (usersDTO.getFirstName() != null) {
                user.setFirstName(usersDTO.getFirstName());
            }
            if (usersDTO.getLastName() != null) {
                user.setLastName(usersDTO.getLastName());
            }
            if (usersDTO.getUsername() != null) {
                user.setUsername(usersDTO.getUsername());
            }
            if (usersDTO.getPasswordHash() != null) {
                user.setPasswordHash(usersDTO.getPasswordHash());
            }
            user.setUpdatedAt(new Date());

            UsersRepository.save(user);
            return new ResponseResult(200, "success", user.getId().toString());
        }
        return new ResponseResult(400, "fail");
    }
    /**
     * Retrieves users by various parameters.
     * @param usersQuery The query containing the parameters.
     * @return ResponseResult object containing status, message and the users.
     */
    @Override
    public ResponseResult listByParams(UsersQuery usersQuery) {
        Criteria criteria = new Criteria();
        if (usersQuery.getEmail() != null) {
            criteria.and("email").is(usersQuery.getEmail());
        }
        if (usersQuery.getPhoneNumber() != null) {
            criteria.and("phone_number").is(usersQuery.getPhoneNumber());
        }
        if (usersQuery.getUsername() != null) {
            criteria.and("username").regex(usersQuery.getUsername(), "i");
        }
        if (usersQuery.getFirstName() != null) {
            criteria.and("first_name").regex(usersQuery.getFirstName(), "i");
        }
        if (usersQuery.getLastName() != null) {
            criteria.and("last_name").regex(usersQuery.getLastName(), "i");
        }
        if (usersQuery.isAdmin()) {
            criteria.and("admin").is(true);
        }
        if (usersQuery.isSuspended()) {
            criteria.and("suspend").is(true);
        }
        Query query = new Query(criteria);
        List<Users> users = mongoTemplate.find(query, Users.class);
        List< UsersResponseDTO> responseList = new ArrayList<UsersResponseDTO>();
        for (Users user : users) {
            responseList.add(convertToResponseDTO(user));
        }


        if (users.size() > 0) {
            return new ResponseResult(200, "success", responseList);
        } else {
            return new ResponseResult(400, "fail");
        }

    }
    /**
     * Retrieves a user by their ID.
     * @param id The ID of the user.
     * @return ResponseResult object containing status, message and the user.
     */
    @Override
    public ResponseResult getById(ObjectId id) {
        Optional<Users> users = UsersRepository.findById(id);
        if (users.isPresent()) {
            return new ResponseResult(200, "success", convertToResponseDTO(users.get()));
        }
        return new ResponseResult(400, "fail");
    }
    /**
     * Assigns a profile to a user.
     * @param userId The ID of the user.
     * @param profileId The ID of the profile.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult assignProfile(ObjectId userId, ObjectId profileId) {
        Optional<Users> users = UsersRepository.findById(userId);
        if (users.isPresent()) {
            Users user = users.get();
            user.setProfileId(profileId);
            UsersRepository.save(user);
            return new ResponseResult(200, "success", user.getId().toString());
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Sends an email.
     * @param to The recipient's email address.
     * @param subject The subject of the email.
     * @param text The body of the email.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult sendEmail(String to, String subject, String text) {
        if (to == null || subject == null || text == null) {
            return new ResponseResult(400, "Invalid email address");
        }
        if (!EmailUtil.isValidEmail(to)) {
            return new ResponseResult(400, "Invalid email address");
        }
        Boolean emailExists = UsersRepository.existsByEmail(to);
        if (emailExists) {
            return new ResponseResult(400, "Email already exist");
        }

        String code = EmailUtil.generateVarificationCode();
        boolean ifEmailSaveToRedis = this.saveEmailToRedis(to, code);
        if (!ifEmailSaveToRedis) {
            return new ResponseResult(400, "Already sent email, please check your email box.");
        } else {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text + code);
            javaMailSender.send(message);
            System.out.println("Email sent successfully");
            return new ResponseResult(200, "success");

        }


    }
    /**
     * Saves an email to Redis.
     * @param email The email to be saved.
     * @param code The verification code to be saved.
     * @return boolean indicating whether the operation was successful.
     */
    @Override
    public boolean saveEmailToRedis(String email, String code) {
        try{
            if (redisson.getBucket(email).isExists()) {
                return false;
            }
            RBucket<String> bucket = redisson.getBucket(email);
            bucket.set(code);
            bucket.expire(5, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    /**
     * Checks if the verification code for an email is valid.
     * @param email The email to be checked.
     * @param code The verification code to be checked.
     * @return boolean indicating whether the verification code is valid.
     */
    @Override
    public boolean checkEmailCode(String email, String code) {
        RBucket<String> bucket = redisson.getBucket(email);
        String codeInRedis = bucket.get();
        if (codeInRedis == null) {
            return false;
        }
        if (codeInRedis.equals(code)) {
            return true;
        }
        return false;
    }

    /**
     * Retrieves the average rating of a user.
     * @param userId The ID of the user.
     * @return ResponseResult object containing status, message and the average rating.
     */
    @Override
    public ResponseResult getAverageRating(ObjectId userId) {
        Optional<Users> users = UsersRepository.findById(userId);
        if (users.isPresent()) {
            Users user = users.get();
            Query query = new Query(Criteria.where("auth_id").is(user.getId()));
            List<Posts> relatedPosts = mongoTemplate.find(query, Posts.class);
            if (relatedPosts.size() > 0) {
                int sum = 0;
                for (Posts post : relatedPosts) {
                    sum += post.getRating();
                }
                DecimalFormat df = new DecimalFormat("#.#");
                double averageRating = (double) sum / relatedPosts.size();

                return new ResponseResult(200, "Success", df.format(averageRating));
            }
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Sends an SMS.
     * @param phoneNumber The phone number to send the SMS to.
     * @return ResponseResult object containing status and message of the operation.
     */

    @Override
    public ResponseResult sendSMS(String phoneNumber) {
        twilioService.sendVerification(phoneNumber);
        return new ResponseResult(200, "Verification code sent successfully");
    }
    /**
     * Verifies a code sent via SMS.
     * @param phoneNumber The phone number the SMS was sent to.
     * @param code The code to verify.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult verifyCode(String phoneNumber, String code) {
        boolean isValid = twilioService.checkVerification(phoneNumber, code);
        if (isValid) {
            return new ResponseResult(200, "Verification successful");
        } else {
            return new ResponseResult(400, "Invalid verification code");
        }
    }
    /**
     * Retrieves a user's bookmarks.
     * @param userId The ID of the user.
     * @return ResponseResult object containing status, message and the bookmarks.
     **/
    @Override
    public ResponseResult getBookmarks(ObjectId userId) {
        Optional<Users> users = UsersRepository.findById(userId);
        if (users.isPresent()) {
            Users user = users.get();
            Optional<BookMarks> bookMarks = bookMarksRepository.findById(user.getBookMarksId());
            if (bookMarks.isPresent()) {
                BookMarks bookMark = bookMarks.get();
                return new ResponseResult(200, "success", bookMark.getId().toString());
            } else {
                return new ResponseResult(400, "fail");
            }
        } else {
            return new ResponseResult(400, "fail");
        }

    }

}
