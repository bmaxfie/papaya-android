package com.papaya.scotthanberg.papaya;

/**
 * Created by ChristianLock on 4/20/17.
 */

public class commentPost {
    public String USERNAME;
    public String COMMENT;
    public int USERACCESS;

    public commentPost(String COMMENT, int USERACCESS) {
        this.USERNAME = AccountData.getUsername();
        this.COMMENT = COMMENT;
        this.USERACCESS = USERACCESS;
    }

    public String getUserName(){ return USERNAME;}

    public String getComment() { return COMMENT;}

    public int getUserAccess() {return USERACCESS;}
}
