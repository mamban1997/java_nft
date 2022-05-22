package com.example.crypto.services;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NftHelper {
    public static Boolean isGuid(String lineForCheck){
        Pattern guidPattern =  Pattern.compile("^\\{?\\p{XDigit}{8}-(?:\\p{XDigit}{4}-){3}\\p{XDigit}{12}}?$");
        Matcher m = guidPattern.matcher(lineForCheck);
        return m.matches();
    }

    public static String generateGuid(){
        return UUID.randomUUID().toString();
    }
}
