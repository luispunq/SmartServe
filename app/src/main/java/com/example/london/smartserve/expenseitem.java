package com.example.london.smartserve;

public class expenseitem {
    private String name;
    private String debitac;
    private String creditac;
    private String period;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDebitac() {
        return debitac;
    }

    public void setDebitac(String debitac) {
        this.debitac = debitac;
    }

    public String getCreditac() {
        return creditac;
    }

    public void setCreditac(String creditac) {
        this.creditac = creditac;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public expenseitem(String name, String debitac, String creditac, String period) {

        this.name = name;
        this.debitac = debitac;
        this.creditac = creditac;
        this.period = period;
    }

    public expenseitem() {

    }
}
