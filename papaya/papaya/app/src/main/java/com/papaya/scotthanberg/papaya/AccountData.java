package com.papaya.scotthanberg.papaya;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bmaxf on 3/30/2017.
 */

public class AccountData
{

    public static final String ACCOUNT_DATA = "ACCOUNT_DATA";

    public enum AccountDataType {
        //todo: add arraylist study sessions that is used a lot in Homescreen.java
        CLASSES,
        FRIENDS,
        CURRENT_SESSION,
        TAPPED_SESSION,
        SERVICE,
        USERID,
        USERNAME,
        EMAIL,
        PHONE,
        AUTH_KEY
    }

    public static final HashMap<AccountDataType, Object> data = new HashMap<AccountDataType, Object>();
    /*
        GETTERS:
     */
    public static ArrayList<Class> getClasses() {
        return (ArrayList<Class>) data.get(AccountDataType.CLASSES);
    }
    public static ArrayList<User> getFriends() {
        return (ArrayList<User>) data.get(AccountDataType.FRIENDS);
    }
    public static String getService() {
        return (String) data.get(AccountDataType.SERVICE);
    }
    public static String getUserID() {
        return (String) data.get(AccountDataType.USERID);
    }
    public static StudySession getCurrentSession() {
        return (StudySession) data.get(AccountDataType.CURRENT_SESSION);
    }
    public static StudySession getTappedSession() {
        return (StudySession) data.get(AccountDataType.TAPPED_SESSION);
    }
    public static String getUsername() {
        return (String) data.get(AccountDataType.USERNAME);
    }
    public static String getEmail() {
        return (String) data.get(AccountDataType.EMAIL);
    }
    public static Long getPhone() {
        return (Long) data.get(AccountDataType.PHONE);
    }
    public static String getAuthKey() {
        return (String) data.get(AccountDataType.AUTH_KEY);
    }


    /*
        SETTERS:
     */
    public static void setClasses(ArrayList<Class> classes) {
        data.put(AccountDataType.CLASSES, classes);
    }
    public static void setFriends(ArrayList<User> friends) {
        data.put(AccountDataType.FRIENDS, friends);
    }
    public static void setUserID(String user_id) {
        data.put(AccountDataType.USERID, user_id);
    }
    public static void setService(String service) {
        data.put(AccountDataType.SERVICE, service);
    }
    public static void setCurrentSession(String currentSession) {
        data.put(AccountDataType.CURRENT_SESSION, currentSession);
    }
    public static void setTappedSession(StudySession tappedSessionId) {
        data.put(AccountDataType.TAPPED_SESSION, tappedSessionId);
    }
    public static void setUsername(String username) {
        data.put(AccountDataType.USERNAME, username);
    }
    public static void setEmail(String email) {
        data.put(AccountDataType.EMAIL, email);
    }
    public static void setPhone(Long phone) {
        data.put(AccountDataType.PHONE, phone);
    }
    public static void setAuthKey(String key) { data.put(AccountDataType.AUTH_KEY, key); }

}
