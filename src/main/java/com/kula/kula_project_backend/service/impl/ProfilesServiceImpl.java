package com.kula.kula_project_backend.service.impl;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.converter.ProfilesResponseDTOConverter;
import com.kula.kula_project_backend.dao.ProfilesRepository;
import com.kula.kula_project_backend.dao.UsersRepository;
import com.kula.kula_project_backend.dto.requestdto.ProfilesDTO;
import com.kula.kula_project_backend.dto.responsedto.ProfilesResponseDTO;
import com.kula.kula_project_backend.entity.Profiles;
import com.kula.kula_project_backend.entity.Users;
import com.kula.kula_project_backend.service.IProfilesService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation for Profiles operations.
 * This service is used to save, update and get Profiles.
 * The service uses the ProfilesRepository to interact with the database.
 * The service uses the UsersRepository to interact with the database.
 * The service uses the ProfilesDTO to save the Profiles.
 * The service uses the ProfilesResponseDTO to convert Profiles to ProfilesResponseDTO.
 * The service uses the ResponseResult to return the result of the operation.
 * The service uses the ObjectId to get the ID of the Profiles.
 * The service uses the containerEndpoint to get the URL of the container.
 * The service uses the UUID to generate a random UUID.
 * The service uses the ProfilesResponseDTOConverter to convert Profiles to ProfilesResponseDTO.
 */
@Service
public class ProfilesServiceImpl implements IProfilesService{

    @Autowired
    private ProfilesRepository profilesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Value("${azure.blob.container-endpoint}")
    private String containerEndpoint;
    /**
     * Saves a new profile or updates an existing one.
     * @param profilesDTO The DTO containing the profile details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult save(ProfilesDTO profilesDTO) {
       Profiles profiles = new Profiles();
       profiles.setBio(profilesDTO.getBio());
       if (profilesDTO.getProfileImageURL() != null){
           profiles.setProfileImageURL(profilesDTO.getProfileImageURL());
       } else {
           profiles.setProfileImageURL("doge.jpeg");
       }
       profiles.setUserLevels(1);
       profiles.setUserPoints(0);
       profiles.setPrivateProfile(profiles.isPrivateProfile());
       profiles.setFavoriteDishIds(new ObjectId[0]);
       profiles.setFavoriteCuisinesIds(new ObjectId[0]);
       profiles.setDeliveryOptionsIds(new ObjectId[0]);

       profilesRepository.insert(profiles);
       if(profiles.getId() != null) {
           Optional<Users>  users = usersRepository.findById(profilesDTO.getUserId());
           if (users.isPresent()){
                Users user = users.get();
                user.setProfileId(profiles.getId());
                usersRepository.save(user);
                return new ResponseResult(200, "success", profiles.getId().toString());
           } else {
               return new ResponseResult(400, "fail");
           }

       }
       return new ResponseResult(400, "fail");

    }
    /**
     * Retrieves a profile by its user ID.
     * @param userId The ID of the user.
     * @return ResponseResult object containing status, message and the profile.
     */
    @Override
    public ResponseResult getProfileByUserId(ObjectId userId) {
        Optional<Users> users = usersRepository.findById(userId);
        if (users.isPresent()){
            Users user = users.get();

                Optional<Profiles> profiles = profilesRepository.findById(user.getProfileId());

                if (profiles.isPresent()) {
                    Profiles profile = profiles.get();
                    ProfilesResponseDTO profilesResponseDTO = ProfilesResponseDTOConverter.convertToResponseDTO(profile);

                    return new ResponseResult(200, "success", profilesResponseDTO);
                } else {
                    return new ResponseResult(400, "fail");
                }
            } else {
            return new ResponseResult(400, "fail");
        }

    }
    /**
     * Updates a profile image.
     * @param profilesDTO The DTO containing the updated profile image details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult updateProfileImage(ProfilesDTO profilesDTO) {
        Optional<Profiles> profiles = profilesRepository.findById(profilesDTO.getId());
        if (profiles.isPresent()) {
            Profiles profile = profiles.get();
            if (profilesDTO.getProfileImageURL() != null) {
                String imageExtension = profilesDTO.getProfileImageURL().substring(profilesDTO.getProfileImageURL().lastIndexOf("."));
                profile.setProfileImageURL(UUID.randomUUID().toString() + imageExtension);
            } else {
                profile.setProfileImageURL("doge.jpeg");
            }
            profilesRepository.save(profile);
            return new ResponseResult(200, "success", profile.getId().toString());
        } else {
            return new ResponseResult(400, "fail");
        }
    }
    /**
     * Updates a profile bio.
     * @param profilesDTO The DTO containing the updated profile bio details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult updateBio(ProfilesDTO profilesDTO) {
        Optional<Profiles> profiles = profilesRepository.findById(profilesDTO.getId());
        if (profiles.isPresent()) {
            Profiles profile = profiles.get();
            if (profilesDTO.getBio() != null) {
                profile.setBio(profilesDTO.getBio());
            }
            profilesRepository.save(profile);
            return new ResponseResult(200, "success", profile.getId().toString());
        } else {
            return new ResponseResult(400, "fail");
        }
    }
    /**
     * Updates a profile levels.
     * @param profilesDTO The DTO containing the updated profile levels details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult updateProfileLevels(ProfilesDTO profilesDTO) {
        Optional<Profiles> profiles = profilesRepository.findById(profilesDTO.getId());

        if (profiles.isPresent() && profilesDTO.getUserLevels() > 0) {
            Profiles profile = profiles.get();

            profile.setUserLevels(profilesDTO.getUserLevels());


            profilesRepository.save(profile);
            return new ResponseResult(200, "success", profile.getId().toString());
        } else {
            return new ResponseResult(400, "fail");
        }
    }
    /**
     * Updates a profile points.
     * @param profilesDTO The DTO containing the updated profile points details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult updateProfilePoints(ProfilesDTO profilesDTO) {
        Optional<Profiles> profiles = profilesRepository.findById(profilesDTO.getId());

        if (profiles.isPresent() && profilesDTO.getUserPoints() != null) {
            Profiles profile = profiles.get();
            int newTotalPoints = profilesDTO.getUserPoints();
            profile.setUserPoints(newTotalPoints);

            profilesRepository.save(profile);
            return new ResponseResult(200, "success", profile.getId().toString());
        } else {
            return new ResponseResult(400, "fail");
        }
    }
    /**
     * Increases a profile points.
     * @param profilesDTO The DTO containing the increased profile points details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult gainProfilePoints(ProfilesDTO profilesDTO) {
        Optional<Profiles> profiles = profilesRepository.findById(profilesDTO.getId());

        if (profiles.isPresent() && profilesDTO.getUserPoints() > 0) {
            Profiles profile = profiles.get();
            Integer newTotalPoints = profilesDTO.getUserPoints() + profile.getUserPoints();
            profile.setUserPoints(newTotalPoints);
            profilesRepository.save(profile);
            return new ResponseResult(200, "success", profile.getId().toString());
        } else {
            return new ResponseResult(400, "fail");
        }
    }


    /**
     * Updates a profile privacy.
     * @param profilesDTO The DTO containing the updated profile privacy details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult updatePrivateProfile(ProfilesDTO profilesDTO) {
        Optional<Profiles> profiles = profilesRepository.findById(profilesDTO.getId());

        if (profiles.isPresent()) {
            Profiles profile = profiles.get();
            profile.setPrivateProfile(profilesDTO.isPrivateProfile());
            profilesRepository.save(profile);
            return new ResponseResult(200, "success", profile.getId().toString());
        } else {
            return new ResponseResult(400, "fail");
        }
    }
    /**
     * Retrieves a user level by user ID.
     * @param userId The ID of the user.
     * @return ResponseResult object containing status, message and the user level.
     */
    @Override
    public ResponseResult getUserLevel(ObjectId userId) {
        Optional<Users> users = usersRepository.findById(userId);
        if (users.isPresent()){
            Users user = users.get();

            Optional<Profiles> profiles = profilesRepository.findById(user.getProfileId());

            if (profiles.isPresent()) {
                Profiles profile = profiles.get();
                return new ResponseResult(200, "success", profile.getUserLevels());
            } else {
                return new ResponseResult(400, "fail");
            }
        } else {
            return new ResponseResult(400, "fail");
        }
    }
    /**
     * Retrieves a profile points by user ID.
     * @param userId The ID of the user.
     * @return ResponseResult object containing status, message and the profile points.
     */
    @Override
    public ResponseResult getProfilePoints(ObjectId userId) {
        Optional<Users> users = usersRepository.findById(userId);
        if (users.isPresent()){
            Users user = users.get();

            Optional<Profiles> profiles = profilesRepository.findById(user.getProfileId());

            if (profiles.isPresent()) {
                Profiles profile = profiles.get();
                return new ResponseResult(200, "success", profile.getUserPoints());
            } else {
                return new ResponseResult(400, "fail");
            }
        } else {
            return new ResponseResult(400, "fail");
        }
    }

    /**
     * Retrieves a profile image URL by user ID.
     * @param userId The ID of the user.
     * @return ResponseResult object containing status, message and the profile image URL.
     */
    @Override
    public ResponseResult getImageURLByUserId(ObjectId userId) {
        Optional<Users> users = usersRepository.findById(userId);
        if (users.isPresent()){
            Users user = users.get();

            Optional<Profiles> profiles = profilesRepository.findById(user.getProfileId());

            if (profiles.isPresent()) {
                Profiles profile = profiles.get();
                String profileImageURL = containerEndpoint + "/" + profile.getProfileImageURL();
                return new ResponseResult(200, "success", profileImageURL);
            } else {
                return new ResponseResult(400, "fail");
            }
        } else {
            return new ResponseResult(400, "fail");
        }
    }



}
