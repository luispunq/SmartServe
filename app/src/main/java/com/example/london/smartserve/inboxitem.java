package com.example.london.smartserve;

public class inboxitem {
    private String date;
    private String user;
    private String message;
    private String status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public inboxitem(String date, String user, String message, String status) {

        this.date = date;
        this.user = user;
        this.message = message;
        this.status = status;
    }

    public inboxitem() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
