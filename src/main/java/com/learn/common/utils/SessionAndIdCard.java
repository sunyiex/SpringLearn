package com.learn.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yi on 2015/6/8.
 */
public class SessionAndIdCard {
    static Map<String,String> userMap = new HashMap<String,String>();

    public static boolean isTrue(String userId, String sessionId){
        String userIdAtSessiong = userMap.get(userId);
        if(userIdAtSessiong == null){
            return false;
        }
        return userIdAtSessiong.equals(sessionId);
    }
    public static void addUser(String userId, String sessionId){
        userMap.put(userId,sessionId);
    }
    public static void removeUser(String userId, String sessionId){
        String userIdAtSessiong = userMap.get(userId);
        if(userIdAtSessiong.equals(sessionId)){
            userMap.remove(userId);
        }

    }
}
