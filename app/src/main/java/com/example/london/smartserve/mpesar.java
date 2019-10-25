package com.example.london.smartserve;

public class mpesar {
    private String date;
    private String txn;
    private String name;
    private String contact;
    private String amount;
    private String group;
    private String rsn;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public mpesar(String date, String txn, String name, String contact, String amount, String group, String rsn) {

        this.date = date;
        this.txn = txn;
        this.name = name;
        this.contact = contact;
        this.amount = amount;
        this.group = group;
        this.rsn = rsn;
    }

    public mpesar() {

    }

    public String getRsn() {
        return rsn;
    }

    public void setRsn(String rsn) {
        this.rsn = rsn;
    }
}
