package com.rao.common.util;

import java.util.Random;

public class Captcha {

    public static String generateSixDigit() {
        Random random = new Random();

        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 6; i++) captcha.append(random.nextInt(10));
        return captcha.toString();
    }
}
