package com.example.london.smartserve;

public class groupsgiven {
    private String name;
    private String groupid;
    private String status;
    private String amount;
    private String amountback;
    private String meetid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public groupsgiven(String name, String groupid, String status, String amount, String amountback, String meetid) {

        this.name = name;
        this.groupid = groupid;
        this.status = status;
        this.amount = amount;
        this.amountback = amountback;
        this.meetid = meetid;
    }
    public groupsgiven() {
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountback() {
        return amountback;
    }

    public void setAmountback(String amountback) {
        this.amountback = amountback;
    }

    public String getMeetid() {
        return meetid;
    }

    public void setMeetid(String meetid) {
        this.meetid = meetid;
    }
}
