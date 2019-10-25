package com.example.london.smartserve;

public class defaults {
    private String date;
    private String groupid;
    private String groupname;
    private String memberid;
    private String cf;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public defaults(String date, String groupid, String groupname, String memberid, String cf) {
        this.date = date;
        this.groupid = groupid;
        this.groupname = groupname;
        this.memberid = memberid;
        this.cf = cf;
    }

    public defaults() {
    }
}
