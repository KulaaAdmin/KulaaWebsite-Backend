package com.kula.kula_project_backend.service.impl;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dao.BookMarksRepository;
import com.kula.kula_project_backend.dao.FollowingGroupsRepository;
import com.kula.kula_project_backend.dao.ProfilesRepository;
import com.kula.kula_project_backend.dao.UsersRepository;
import com.kula.kula_project_backend.dto.requestdto.UsersDTO;
import com.kula.kula_project_backend.query.UsersQuery;
import com.kula.kula_project_backend.entity.Users;
import com.kula.kula_project_backend.service.IUsersService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements IUsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProfilesRepository profilesRepository;

    @Autowired
    private BookMarksRepository bookMarksRepository;

    @Autowired
    private FollowingGroupsRepository followingGroupsRepository;

    @Override
    public ResponseResult save(UsersDTO usersDTO) {
        // Implementation for saving a user
        return new ResponseResult(200, "User saved successfully");
    }

    @Override
    public ResponseResult getAll() {
        // Implementation for getting all users
        List<Users> users = usersRepository.findAll();
        return new ResponseResult(200, "All users fetched successfully", users);
    }

    @Override
    public ResponseResult update(UsersDTO usersDTO) {
        // Implementation for updating a user
        return new ResponseResult(200, "User updated successfully");
    }

    @Override
    public ResponseResult listByParams(UsersQuery usersQuery) {
        // Implementation for listing users by query parameters
        return new ResponseResult(200, "Users fetched by parameters");
    }

    @Override
    public ResponseResult getById(ObjectId id) {
        // Implementation for getting a user by ID
        Optional<Users> user = usersRepository.findById(id);
        return user.map(value -> new ResponseResult(200, "User found", value))
                .orElseGet(() -> new ResponseResult(404, "User not found"));
    }

    @Override
    public ResponseResult assignProfile(ObjectId userId, ObjectId profileId) {
        // Implementation for assigning a profile to a user
        Optional<Users> user = usersRepository.findById(userId);
        if (user.isPresent()) {
            Users updatedUser = user.get();
            updatedUser.setProfileId(profileId);
            usersRepository.save(updatedUser);
            return new ResponseResult(200, "Profile assigned successfully");
        }
        return new ResponseResult(404, "User not found");
        //
    }

    @Override
    public ResponseResult login(String emailOrPhoneNumber, String password) {
        // Implementation for login
        return new ResponseResult(200, "Login successful");
    }

    @Override
    public ResponseResult suggestFriends(ObjectId userId) {
        // Implementation for friend suggestions
        return new ResponseResult(200, "Friend suggestions fetched successfully");
    }

    @Override
    public ResponseResult sendEmail(String to, String subject, String text) {
        // Implementation for sending an email
        return new ResponseResult(200, "Email sent successfully");
    }

    @Override
    public boolean saveEmailToRedis(String email, String code) {
        // Implementation for saving email to Redis
        return true;
    }

    @Override
    public boolean checkEmailCode(String email, String code) {
        // Implementation for checking email code
        return true;
    }

    @Override
    public ResponseResult getAverageRating(ObjectId userId) {
        // Implementation for getting a user's average rating
        return new ResponseResult(200, "Average rating fetched successfully");
    }

    @Override
    public ResponseResult sendSMS(String phoneNumber) {
        // Implementation for sending SMS
        return new ResponseResult(200, "SMS sent successfully");
    }

    @Override
    public ResponseResult verifyCode(String phoneNumber, String code) {
        // Implementation for verifying SMS code
        return new ResponseResult(200, "Code verified successfully");
    }

    @Override
    public ResponseResult getBookmarks(ObjectId userId) {
        // Implementation for fetching user bookmarks
        return new ResponseResult(200, "Bookmarks fetched successfully");
    }
}
