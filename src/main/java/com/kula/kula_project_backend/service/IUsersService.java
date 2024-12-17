package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.UsersDTO;
import com.kula.kula_project_backend.entity.Users;
import com.kula.kula_project_backend.query.UsersQuery;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUsersService{

    ResponseResult save(UsersDTO usersDTO);

    ResponseResult getAll();

    ResponseResult update(UsersDTO usersDTO);

    ResponseResult listByParams(UsersQuery usersQuery);

    ResponseResult getById(ObjectId id);

    ResponseResult assignProfile(ObjectId userId, ObjectId profileId);

    ResponseResult login(String emailOrPhoneNumber, String password);

    ResponseResult sendEmail(String to, String subject, String text);

    boolean saveEmailToRedis(String email, String code);

    boolean checkEmailCode(String email, String code);


    ResponseResult getAverageRating(ObjectId userId);

    ResponseResult sendSMS(String phoneNumber);

    ResponseResult verifyCode(String phoneNumber, String code);

    ResponseResult getBookmarks(ObjectId userId);

    ResponseResult getUsersBrief(int page, int pageSize);


}
