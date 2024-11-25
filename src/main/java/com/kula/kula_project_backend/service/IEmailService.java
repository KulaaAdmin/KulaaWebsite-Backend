package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;

public interface IEmailService {

    ResponseResult sendEmail(String to, String subject, String text);

}
