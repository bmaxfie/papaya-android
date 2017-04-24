package com.papaya.scotthanberg.papaya;

/**
 * Created by ChristianLock on 4/20/17.
 */

public class commentPost {
    public String USERNAME;
    public String COMMENT;
    public String postId;

    public commentPost(String USERNAME, String COMMENT, String postId) {
        this.USERNAME = USERNAME;
        this.COMMENT = COMMENT;
        this.postId = postId;
    }

    public String getUserName(){ return USERNAME;}

    public String getComment() { return COMMENT;}

    public String getPostId() { return postId; }

}
