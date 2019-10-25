package com.example.london.smartserve;

public class meetings {
    private String groupName;
    private String groupImage;
    private String groupid;
    private String facilitator;
    private String cashfromoffice;
    private String date;


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

    public meetings(String groupName, String groupImage, String groupid, String facilitator, String cashfromoffice, String date) {
        this.groupName = groupName;
        this.groupImage = groupImage;
        this.groupid = groupid;
        this.facilitator = facilitator;
        this.cashfromoffice = cashfromoffice;
        this.date = date;
    }

    public meetings() {

    }


    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getCashfromoffice() {
        return cashfromoffice;
    }

    public void setCashfromoffice(String cashfromoffice) {
        this.cashfromoffice = cashfromoffice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
