package com.example.london.smartserve;

public class account {
    private String name;
    private String amount;
    private String type;
    private String group;
    private String meet;
    private String debitac;
    private String creditac;
    private String description;
    private long timestamp;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMeet() {
        return meet;
    }

    public void setMeet(String meet) {
        this.meet = meet;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public account(String name, String amount, String type, String group, String meet, String debitac, String creditac, String description, long timestamp) {
        this.name = name;
        this.amount = amount;
        this.type = type;
        this.group = group;
        this.meet = meet;
        this.debitac = debitac;
        this.creditac = creditac;
        this.description = description;
        this.timestamp = timestamp;
    }

    public account() {

    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
