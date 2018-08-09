package com.example.nirhaviv.sportify.recycler.adapters;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class PostForList {
    public String postUid;
    public Drawable Background;
    public Drawable ProfileImage;
    public String UserName;
    public Date timestamp;
    public String PostText;

    public String getPostUid() {
        return postUid;
    }

    public void setPostUid(String postUid) {
        this.postUid = postUid;
    }

    public Drawable getBackground() {
        return Background;
    }

    public void setBackground(Drawable background) {
        Background = background;
    }

    public Drawable getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(Drawable profileImage) {
        ProfileImage = profileImage;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getPostText() {
        return PostText;
    }

    public void setPostText(String postText) {
        PostText = postText;
    }
}
