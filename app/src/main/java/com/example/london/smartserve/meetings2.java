package com.example.london.smartserve;

public class meetings2 {
    private String groupName;
    private String groupImage;
    private String groupid;
    private String facilitator;
    private String date;
    private String meetid;


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getFacilitator() {
        return facilitator;
    }

    public void setFacilitator(String facilitator) {
        this.facilitator = facilitator;
    }

    public meetings2(String groupName, String groupImage, String groupid, String facilitator, String date, String meetid) {
        this.groupName = groupName;
        this.groupImage = groupImage;
        this.groupid = groupid;
        this.facilitator = facilitator;
        this.date = date;
        this.meetid = meetid;
    }

    public meetings2() {

    }


    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMeetid() {
        return meetid;
    }

    public void setMeetid(String meetid) {
        this.meetid = meetid;
    }
}
