package com.gramajo.josue.chatbot.Utils;

import java.security.SecureRandom;

/**
 * Created by josuegramajo on 4/17/18.
 */

public class RandomID {
    public static RandomID INSTANCE = new RandomID();
    final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    SecureRandom rnd = new SecureRandom();

    public String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}
