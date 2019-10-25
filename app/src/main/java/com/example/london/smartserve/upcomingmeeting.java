package com.example.london.smartserve;

public class upcomingmeeting {
    private String groupid;
    private String date;
    private String venue;
    private String groupname;
    private String assign;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public upcomingmeeting(String groupid, String date, String venue, String groupname, String assign) {
        this.groupid = groupid;
        this.date = date;
        this.venue = venue;
        this.groupname = groupname;
        this.assign = assign;
    }

    public upcomingmeeting() {
    }

    public String getGroupName() {
        return groupname;
    }

    public void setGroupName(String groupname) {
        this.groupname = groupname;
    }

    public String getAssign() {
        return assign;
    }

    public void setAssign(String assign) {
        this.assign = assign;
    }
}
