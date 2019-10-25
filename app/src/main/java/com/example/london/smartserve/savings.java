package com.example.london.smartserve;

public class savings {
    private String date;
    private String details;
    private String save;
    private String with;
    private String bal;
    private String fac;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFac() {
        return fac;
    }

    public void setFac(String fac) {
        this.fac = fac;
    }

    public savings(String date, String details, String save, String with, String bal, String amount) {

        this.date = date;
        this.details = details;
        this.save = save;
        this.with = with;
        this.bal = bal;
        this.fac = amount;
    }

    public savings() {

    }

    public String getBal() {
        return bal;
    }

    public void setBal(String bal) {
        this.bal = bal;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public String getWith() {
        return with;
    }

    public void setWith(String with) {
        this.with = with;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
