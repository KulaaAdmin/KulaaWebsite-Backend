package com.kula.kula_project_backend.dto.requestdto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * LoginDTO is a data transfer object for the login request.
 * It contains the user's email or phone number and password.
 */
@Data
@Accessors(chain = true)
public class LoginDTO {
    /**
     * The user's email or phone number.
     */
    private String emailOrPhoneNumber;

    /**
     * The user's password.
     */
    private String password;
}
