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
        CLASSES,
        FRIENDS,
        CURRENT_SESSION,
        USERID,
        USERNAME,
        EMAIL,
        PHONE
    }

    public static HashMap<AccountDataType, Object> data = new HashMap<AccountDataType, Object>();

    /*
        GETTERS:
     */
    public static ArrayList<Class> getClasses() {
        return (ArrayList<Class>) data.get(AccountDataType.CLASSES);
    }
    public static ArrayList<User> getFriends() {
        return (ArrayList<User>) data.get(AccountDataType.FRIENDS);
    }
    public static String getUserID() {
        return (String) data.get(AccountDataType.USERID);
    }
    public static String getCurrentSession() {
        return (String) data.get(AccountDataType.CURRENT_SESSION);
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
    public static void setCurrentSession(String currentSession) {
        data.put(AccountDataType.CURRENT_SESSION, currentSession);
    }
    public static void setUsername(String username) {
        data.put(AccountDataType.USERNAME, username);
    }
    public static void setEmail(String email) {
        data.put(AccountDataType.EMAIL, email);
    }
    public static void setPhone(String phone) {
        data.put(AccountDataType.PHONE, phone);
    }

}
