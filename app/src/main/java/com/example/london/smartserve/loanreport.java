package com.example.london.smartserve;

public class loanreport {
    private String amountcash;
    private String userid;
    private String amountrequested;
    private String monthlypay;
    private String status;
    private String type;
    private String date="";
    private String gmid;
    private String gname;
    private String username;

    public String getAmountcash() {
        return amountcash;
    }

    public void setAmountcash(String amountcash) {
        this.amountcash = amountcash;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAmountrequested() {
        return amountrequested;
    }

    public void setAmountrequested(String amountrequested) {
        this.amountrequested = amountrequested;
    }

    public String getMonthlypay() {
        return monthlypay;
    }

    public void setMonthlypay(String monthlypay) {
        this.monthlypay = monthlypay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGmid() {
        return gmid;
    }

    public void setGmid(String gmid) {
        this.gmid = gmid;
    }

    public loanreport(String amountcash, String userid, String amountrequested, String monthlypay, String status, String type, String date, String gmid, String gname, String username) {

        this.amountcash = amountcash;
        this.userid = userid;
        this.amountrequested = amountrequested;
        this.monthlypay = monthlypay;
        this.status = status;
        this.type = type;
        this.date = date;
        this.gmid = gmid;
        this.gname = gname;
        this.username = username;
    }

    public loanreport() {

    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        if(date != null)
        this.date = date;
    }
}
