package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.UsersDTO;
import com.kula.kula_project_backend.query.UsersQuery;
import org.bson.types.ObjectId;

public interface IUsersService {

    ResponseResult save(UsersDTO usersDTO);

    ResponseResult getAll();

    ResponseResult update(UsersDTO usersDTO);

    ResponseResult listByParams(UsersQuery usersQuery);

    ResponseResult getById(ObjectId id);

    ResponseResult assignProfile(ObjectId userId, ObjectId profileId);

    ResponseResult login(String emailOrPhoneNumber, String password);

    ResponseResult suggestFriends(ObjectId userId); // New method

    ResponseResult sendEmail(String to, String subject, String text);

    boolean saveEmailToRedis(String email, String code);

    boolean checkEmailCode(String email, String code);

    ResponseResult getAverageRating(ObjectId userId);

    ResponseResult sendSMS(String phoneNumber);

    ResponseResult verifyCode(String phoneNumber, String code);

    ResponseResult getBookmarks(ObjectId userId);

}
