package com.example.london.smartserve;

public class ledgerbook {
    private String groupname;
    private String amountbanked;
    private String amountout;
    private String user;
    private String status;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getAmountbanked() {
        return amountbanked;
    }

    public void setAmountbanked(String amountbanked) {
        this.amountbanked = amountbanked;
    }

    public ledgerbook(String groupname, String amountbanked, String amountout, String user, String status) {
        this.groupname = groupname;
        this.amountbanked = amountbanked;
        this.amountout = amountout;
        this.user = user;
        this.status = status;
    }

    public ledgerbook() {
    }

    public String getAmountout() {
        return amountout;
    }

    public void setAmountout(String amountout) {
        this.amountout = amountout;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
