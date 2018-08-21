package com.example.jnguyen.limechat;

import java.util.Random;

public class LimeChatUtils {
    static final int MAX_LENGTH = 30;

    public static class StringHelper {
        public static String generateRandomString(){
            Random generator = new Random();
            StringBuilder randomStringBuilder = new StringBuilder();
            int randomLength = generator.nextInt(MAX_LENGTH);
            char tempChar;
            for (int i = 0; i < randomLength; i++){
                tempChar = (char) (generator.nextInt(96) + 32);
                randomStringBuilder.append(tempChar);
            }
            return randomStringBuilder.toString();
        }

    }
}
