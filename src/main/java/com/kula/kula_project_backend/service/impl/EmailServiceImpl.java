package com.kula.kula_project_backend.service.impl;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
/**
 * Service implementation for Email operations.
 * This service is used to send emails.
 * The service uses the JavaMailSender to send emails.
 * The service uses the ResponseResult to return the result of the operation.
 */
@Service
public class EmailServiceImpl implements IEmailService {
    // JavaMailSender interface is used to send Mail via Spring's mail support
    @Autowired
    private JavaMailSender javaMailSender;
    /**
     * Sends an email.
     * @param to The recipient's email address.
     * @param subject The subject of the email.
     * @param text The body of the email.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
        return new ResponseResult(200, "success");
    }



}
