package com.tusofia.flagguessinggame;

import android.util.Base64;

import com.google.android.material.textfield.TextInputEditText;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DBCryptService {

    private static final String SECRET_KEY = "SECRET_KEY";
    private static final String INITIALIZATION_VECTOR = "INITIALIZATION_VECTOR";


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

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptEmail(TextInputEditText emailEditText) {
        try {
            // Извличане на имейл от TextInputEditText
            String email = emailEditText.getText().toString();

            // Генериране на ключ и инициализационен вектор
            byte[] key = SECRET_KEY.getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            byte[] iv = INITIALIZATION_VECTOR.getBytes();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Инициализация на криптографския обект
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            // Криптиране на имейла
            byte[] encryptedBytes = cipher.doFinal(email.getBytes());

            // Base64 кодиране на криптираните данни за по-лесно съхранение и предаване
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
