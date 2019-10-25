package com.example.london.smartserve;

public class expenseitemadded {
    private String name;
    private String debitac;
    private String creditac;
    private String amount;
    private String description;
    private String status;

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

    public expenseitemadded(String name, String debitac, String creditac, String amount, String description, String status) {

        this.name = name;
        this.debitac = debitac;
        this.creditac = creditac;
        this.amount = amount;
        this.description = description;
        this.status = status;
    }

    public expenseitemadded() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
