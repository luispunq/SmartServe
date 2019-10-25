package com.example.london.smartserve;

public class guarantors {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public guarantors(String name, String id, String contact) {

        this.name = name;
        this.id = id;
        this.contact = contact;
    }

    public guarantors() {

    }

    private String contact;
}
