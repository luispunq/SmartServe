package com.example.london.smartserve;

public class member {
    private String name;
    private String id;
    private String profileImage;
    private String gender;
    private String group;
    private String uid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public member(String name, String id, String profileImage, String gender, String group, String uid) {

        this.name = name;
        this.id = id;
        this.profileImage = profileImage;
        this.gender = gender;
        this.group = group;
        this.uid = uid;
    }

    public member() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
