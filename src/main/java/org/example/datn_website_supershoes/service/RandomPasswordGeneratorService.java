package org.example.datn_website_supershoes.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
@Service
public class RandomPasswordGeneratorService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generatePassword(int length) {
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
    public static String getPassword() {
        int passwordLength = 12;

        String randomPassword = generatePassword(passwordLength);

        return randomPassword;
    }
    public static String getCodePromotion() {
        int passwordLength = 10;

        String randomCodePromotion = generatePassword(passwordLength);

        return randomCodePromotion;
    }
}
