package com.papaya.scotthanberg.papaya;

/**
 * Created by ChristianLock on 4/20/17.
 */

public class commentPost {
    public String USERNAME;
    public String COMMENT;

    public commentPost(String USERNAME, String COMMENT) {
        this.USERNAME = USERNAME;
        this.COMMENT = COMMENT;
    }

    public String getUserName(){ return USERNAME;}

    public String getComment() { return COMMENT;}

}
