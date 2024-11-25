package com.kula.kula_project_backend.util;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.Array;

public class EmailUtil
{
    public static boolean isValidEmail(String email) {
        boolean result = true;
        try{
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public static String generateVarificationCode() {
        List<String> array = Arrays.asList(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"});
        Collections.shuffle(array);
        String code = "";
        for (int i = 0; i < 6; i++) {
            code += array.get(i);
        }
        return code;
    }

}
