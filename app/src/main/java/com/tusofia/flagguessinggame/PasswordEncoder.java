package com.tusofia.flagguessinggame;

import com.google.android.material.textfield.TextInputEditText;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {
    public static String encodePassword(TextInputEditText editTextPassword) {
        String password = String.valueOf(editTextPassword.getText());

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

            byte[] hashBytes = digest.digest(passwordBytes);

            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Return the hexadecimal representation of the hash
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle error (e.g., return null or throw an exception)
            return null;
        }
    }
}
