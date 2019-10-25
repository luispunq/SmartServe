package com.example.london.smartserve;

public class forms {
    private String date;
    private String groupname;
    private String name;
    private String amount;
    private String months;
    private String installments;
    private String total;
    private String status;
    private String officer;
    private long timestamp;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public String getInstallments() {
        return installments;
    }

    public void setInstallments(String installments) {
        this.installments = installments;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public forms(String date, String groupname, String name, String amount, String months, String installments, String total, String status, String officer, long timestamp) {
        this.date = date;
        this.groupname = groupname;
        this.name = name;
        this.amount = amount;
        this.months = months;
        this.installments = installments;
        this.total = total;
        this.status = status;
        this.officer = officer;
        this.timestamp = timestamp;
    }

    public forms() {
    }

    public String getOfficer() {
        return officer;
    }

    public void setOfficer(String officer) {
        this.officer = officer;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
