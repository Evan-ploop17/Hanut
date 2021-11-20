package com.example.hanut.models;

public class User {

    public String id;
    public String userName;
    public String email;
    public String phone;
    public String imageProfile;
    public String imageCover;
    public long timestamp;

    public User() {
    }

    public User(String id, String userName, String password, String phone, String imageProfile, String imageCover, long timestamp) {
        this.id = id;
        this.userName = userName;
        this.email = password;
        this.phone = phone;
        this.imageProfile = imageProfile;
        this.imageCover = imageCover;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public long getTimestamp() { return timestamp; }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }
}
