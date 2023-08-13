package org.utils;

import java.util.Random;

public class RandomDataGeneratorUtil {

    public static String generateRandomNumber() {
        Random random = new Random();
        int id = random.nextInt(100000);
        return String.valueOf(id);
    }

    public static String generateRandomPhoneNumber(int length) {
        Random random = new Random();
        StringBuilder phoneNumber = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            phoneNumber.append(digit);
        }

        return phoneNumber.toString();
    }

    public static String generateRandomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random1 = new Random();

        return random1.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
