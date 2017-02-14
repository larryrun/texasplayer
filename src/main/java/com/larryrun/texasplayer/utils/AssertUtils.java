package com.larryrun.texasplayer.utils;

public class AssertUtils {

    public static void notNull(Object obj, String propertyName) {
        if(obj == null) {
            throw new RuntimeException("Can not be null!");
        }
    }
}
