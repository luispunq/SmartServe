package com.example.london.smartserve;

public class loanapprovals {
    private String dateapproved;
    private String amount;
    private String status;

    public String getDateapproved() {
        return dateapproved;
    }

    public void setDateapproved(String dateapproved) {
        this.dateapproved = dateapproved;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public loanapprovals(String dateapproved, String amount, String status) {

        this.dateapproved = dateapproved;
        this.amount = amount;
        this.status = status;
    }

    public loanapprovals() {

    }
}
