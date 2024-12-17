package com.kula.kula_project_backend.controller;

import com.kula.kula_project_backend.dto.requestdto.LoginDTO;
import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.UsersDTO;
import com.kula.kula_project_backend.query.UsersQuery;
import com.kula.kula_project_backend.service.impl.EmailServiceImpl;
import com.kula.kula_project_backend.service.impl.UsersServiceImpl;
import com.kula.kula_project_backend.util.EmailUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
/**
 * UsersController is a REST controller that provides endpoints for managing users.
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersServiceImpl usersService;

    @Autowired
    private EmailServiceImpl emailService;
    /**
     * Endpoint to login a user.
     * @param loginRequest The login request containing the user's email or phone number and password.
     * @return The result of the login operation.
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginDTO loginRequest) {
        return usersService.login(loginRequest.getEmailOrPhoneNumber(), loginRequest.getPassword());
    }
    /**
     * Endpoint to save a new user.
     * @param usersDTO The user data transfer object containing the user details.
     * @return The result of the save operation.
     */
    @PostMapping("/save")
    public ResponseResult save(@RequestBody @Validated UsersDTO usersDTO) {
        return usersService.save(usersDTO);
    }
    /**
     * Endpoint to get all users.
     * @return The result of the get operation.
     */
    @GetMapping("/getAll")
    public ResponseResult getAll() {
        return usersService.getAll();
    }
    /**
     * Endpoint to update a user.
     * @param usersDTO The user data transfer object containing the updated user details.
     * @return The result of the update operation.
     */
    @PostMapping("/update")
    public ResponseResult update(@RequestBody @Validated UsersDTO usersDTO) {
//        return usersService.update(usersDTO);
        return null;
    }
    /**
     * Endpoint to get a list of users by parameters.
     * @param usersQuery The users query object containing the query parameters.
     * @return The result of the get operation.
     */
    @PostMapping("/listByParams")
    public ResponseResult listByParams(@RequestBody UsersQuery usersQuery) {
    return usersService.listByParams(usersQuery);
    }
    /**
     * Endpoint to get a user by its id.
     * @param id The id of the user.
     * @return The result of the get operation.
     */
    @GetMapping("/{id}")
    public ResponseResult getById(@PathVariable ObjectId id) {
        return usersService.getById(id);
    }
    /**
     * Endpoint to assign a profile to a user.
     * @param userId The id of the user.
     * @param profileId The id of the profile.
     * @return The result of the assign operation.
     */
    @PostMapping("/assignProfile")
    public ResponseResult assignProfile(@RequestParam ObjectId userId, @RequestParam ObjectId profileId) {
        return usersService.assignProfile(userId, profileId);
    }
    /**
     * Endpoint to send an email.
     * @param to The recipient's email address.
     * @param subject The subject of the email.
     * @param text The text of the email.
     * @return The result of the send operation.
     */
    @PostMapping("/sendEmail")
    public ResponseResult sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        if (!EmailUtil.isValidEmail(to)) {
            return new ResponseResult(400, "Invalid email address");
        }
        return usersService.sendEmail(to, subject, text);
    }

    @GetMapping("/verifyEmailCode")
    public ResponseResult verifyEmailCode(@RequestParam String email, @RequestParam String code) {
        return usersService.checkEmailCode(email, code);
    }
    /**
     * Endpoint to get the average rating of a user.
     * @param userId The id of the user.
     * @return The result of the get operation.
     */
    @GetMapping("/getAverageRating/{userId}")
    public ResponseResult getAverageRating(@PathVariable ObjectId userId) {
        return usersService.getAverageRating(userId);
    }
    /**
     * Endpoint to send an SMS verification code.
     * @param request The request containing the phone number.
     * @return The result of the send operation.
     */
    @PostMapping("/sendSMSVerificationCode")
    public ResponseResult sendSMSVerificationCode(@RequestBody Map<String, String> request) {
        String phoneNumber = request.get("phoneNumber");
        return usersService.sendSMS(phoneNumber);
    }
    /**
     * Endpoint to verify an SMS code.
     * @param request The request containing the phone number and the code.
     * @return The result of the verify operation.
     */
    @PostMapping("/verifySMSCode")
    public ResponseResult verifySMSCode(@RequestBody Map<String, String> request) {
        String phoneNumber = request.get("phoneNumber");
        String code = request.get("code");
        return usersService.verifyCode(phoneNumber, code);
    }
    /**
     * Endpoint to get a user's bookmarks.
     * @param userId The id of the user.
     * @return The result of the get operation.
     */
    @GetMapping("/getBookmarks/{userId}")
    public ResponseResult getBookmarks(@PathVariable ObjectId userId) {
        return usersService.getBookmarks(userId);

    }

}
