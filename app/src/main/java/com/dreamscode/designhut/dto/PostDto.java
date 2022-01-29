package com.dreamscode.designhut.dto;

public class PostDto {
    int img_user,img_post;
    String user_name,time,description;

    public PostDto(int img_user, int img_post, String user_name, String time, String description) {
        this.img_user = img_user;
        this.img_post = img_post;
        this.user_name = user_name;
        this.time = time;
        this.description = description;
    }

    public int getImg_user() {
        return img_user;
    }

    public void setImg_user(int img_user) {
        this.img_user = img_user;
    }

    public int getImg_post() {
        return img_post;
    }

    public void setImg_post(int img_post) {
        this.img_post = img_post;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
