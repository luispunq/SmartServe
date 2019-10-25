package com.example.london.smartserve;

public class bankacc {
    private String chequenumber;
    private String date;
    private String details;
    private String status;
    private String deposit;
    private String withdraw;
    private long timestamp;

    public String getChequenumber() {
        return chequenumber;
    }

    public void setChequenumber(String chequenumber) {
        this.chequenumber = chequenumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(String withdraw) {
        this.withdraw = withdraw;
    }

    public bankacc(String chequenumber, String date, String details, String status, String deposit, String withdraw, long timestamp) {
        this.chequenumber = chequenumber;
        this.date = date;
        this.details = details;
        this.status = status;
        this.deposit = deposit;
        this.withdraw = withdraw;
        this.timestamp = timestamp;
    }

    public bankacc() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
