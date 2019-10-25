package com.example.london.smartserve;

public class loanapps {
    private String requestdate;
    private String userid;
    private String amountrequested;
    private String rate;
    private String period;
    private String monthlypay;
    private String status;
    private String image;
    private String recommendation;
    private String officer;

    public String getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(String requestdate) {
        this.requestdate = requestdate;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAmountrequested() {
        return amountrequested;
    }

    public void setAmountrequested(String amountrequested) {
        this.amountrequested = amountrequested;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getMonthlypay() {
        return monthlypay;
    }

    public void setMonthlypay(String monthlypay) {
        this.monthlypay = monthlypay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public loanapps(String requestdate, String userid, String amountrequested, String rate, String period, String monthlypay, String status, String image, String recommendation, String officer) {

        this.requestdate = requestdate;
        this.userid = userid;
        this.amountrequested = amountrequested;
        this.rate = rate;
        this.period = period;
        this.monthlypay = monthlypay;
        this.status = status;
        this.image = image;
        this.recommendation = recommendation;
        this.officer = officer;
    }

    public loanapps() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getOfficer() {
        return officer;
    }

    public void setOfficer(String officer) {
        this.officer = officer;
    }
}
