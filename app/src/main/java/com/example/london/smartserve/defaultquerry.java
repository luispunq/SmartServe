package com.example.london.smartserve;

public class defaultquerry {
    private String date;
    private String memberid;
    private String groupid;
    private String meetid;
    private String fac;
    private String status;
    private String particulars;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getMeetid() {
        return meetid;
    }

    public void setMeetid(String meetid) {
        this.meetid = meetid;
    }

    public String getFac() {
        return fac;
    }

    public void setFac(String fac) {
        this.fac = fac;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public defaultquerry() {
    }

    public defaultquerry(String date, String memberid, String groupid, String meetid, String fac, String status, String particulars) {
        this.date = date;
        this.memberid = memberid;
        this.groupid = groupid;
        this.meetid = meetid;
        this.fac = fac;
        this.status = status;
        this.particulars = particulars;
    }
}
