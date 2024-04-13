package com.tusofia.flagguessinggame;

import com.google.android.material.textfield.TextInputEditText;

public class PasswordEncoder {
    public static String encodePassword(TextInputEditText editTextPassword) {
        return String.valueOf(editTextPassword.getText());
    }
}
