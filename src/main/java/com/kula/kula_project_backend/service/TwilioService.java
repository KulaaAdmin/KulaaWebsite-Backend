package com.kula.kula_project_backend.service;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TwilioService {

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.service_sid}")
    private String serviceSid;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendVerification(String phoneNumber) {
        Verification verification = Verification.creator(
                        serviceSid,
                        phoneNumber,
                        "sms")
                .create();
        System.out.println(verification.getSid());
    }

    public boolean checkVerification(String phoneNumber, String code) {
        VerificationCheck verificationCheck = VerificationCheck.creator(serviceSid)
                .setTo(phoneNumber)
                .setCode(code)
                .create();

        return verificationCheck.getStatus().equals("approved");
    }
}
