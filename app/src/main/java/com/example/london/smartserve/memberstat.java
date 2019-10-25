package com.example.london.smartserve;

public class memberstat {
    private String name;
    private String savings;
    private String advance;
    private String loans;
    private String riskfund;
    private String arreas;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSavings() {
        return savings;
    }

    public void setSavings(String savings) {
        this.savings = savings;
    }

    public String getAdvance() {
        return advance;
    }

    public void setAdvance(String advance) {
        this.advance = advance;
    }

    public String getLoans() {
        return loans;
    }

    public void setLoans(String loans) {
        this.loans = loans;
    }

    public String getRiskfund() {
        return riskfund;
    }

    public void setRiskfund(String riskfund) {
        this.riskfund = riskfund;
    }

    public memberstat(String name, String savings, String advance, String loans, String riskfund, String arreas) {
        this.name = name;
        this.savings = savings;
        this.advance = advance;
        this.loans = loans;
        this.riskfund = riskfund;
        this.arreas = arreas;
    }

    public memberstat() {
    }

    public String getArreas() {
        return arreas;
    }

    public void setArreas(String arreas) {
        this.arreas = arreas;
    }
}
