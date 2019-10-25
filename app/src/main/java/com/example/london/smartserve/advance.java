package com.example.london.smartserve;

public class advance {
    private String date;
    private String advbf;
    private String paid;
    private String bal;
    private String fac;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdvbf() {
        return advbf;
    }

    public void setAdvbf(String advbf) {
        this.advbf = advbf;
    }

    public advance() {

    }

    public advance(String date, String amount, String paid, String bal, String fac) {

        this.date = date;
        this.advbf = amount;
        this.paid = paid;
        this.bal = bal;
        this.fac = fac;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getBal() {
        return bal;
    }

    public void setBal(String bal) {
        this.bal = bal;
    }

    public String getFac() {
        return fac;
    }

    public void setFac(String fac) {
        this.fac = fac;
    }
}
