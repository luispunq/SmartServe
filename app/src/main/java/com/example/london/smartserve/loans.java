package com.example.london.smartserve;

public class loans {
    private String date;
    private String loanbf;
    private String inst;
    private String bal;
    private String fac;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoanbf() {
        return loanbf;
    }

    public void setLoanbf(String loanbf) {
        this.loanbf = loanbf;
    }

    public loans() {

    }

    public loans(String date, String amount, String inst, String bal, String fac) {

        this.date = date;
        this.loanbf = amount;
        this.inst = inst;
        this.bal = bal;
        this.fac = fac;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
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
