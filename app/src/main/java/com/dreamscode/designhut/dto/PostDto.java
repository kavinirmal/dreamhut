package com.dreamscode.designhut.dto;

import android.net.Uri;

public class PostDto {
    String PostDescription,postId,UserId,DateTime;

    public PostDto(String postDescription, String postId, String userId, String dateTime) {
        PostDescription = postDescription;
        this.postId = postId;
        UserId = userId;
        DateTime = dateTime;
    }

    public String getPostDescription() {
        return PostDescription;
    }

    public void setPostDescription(String postDescription) {
        PostDescription = postDescription;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }
}
