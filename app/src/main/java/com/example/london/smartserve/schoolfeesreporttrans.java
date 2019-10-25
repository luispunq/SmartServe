package com.example.london.smartserve;

public class schoolfeesreporttrans {
    private String totalamount;
    private String date;
    private String lsf;
    private String lsfcf;
    private String advcf;
    private String advancepayment;
    private String advgvn;
    private String memberid;
    private String paymentmode;
    private String advanceinterest;
    private String id;

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLsf() {
        return lsf;
    }

    public void setLsf(String lsf) {
        this.lsf = lsf;
    }


    public String getAdvancepayment() {
        return advancepayment;
    }

    public void setAdvancepayment(String advancepayment) {
        this.advancepayment = advancepayment;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public schoolfeesreporttrans(String totalamount, String date, String lsf, String lsfcf, String loaninstallments, String loangvn, String loancf, String advcf, String advancepayment, String advgvn, String memberid, String paymentmode, String loaninterest, String advanceinterest, String id) {
        this.totalamount = totalamount;
        this.date = date;
        this.lsf = lsf;
        this.lsfcf = lsfcf;
        this.advcf = advcf;
        this.advancepayment = advancepayment;
        this.advgvn = advgvn;
        this.memberid = memberid;
        this.paymentmode = paymentmode;
        this.advanceinterest = advanceinterest;
        this.id = id;
    }

    public schoolfeesreporttrans() {
    }

    public String getAdvanceinterest() {
        return advanceinterest;
    }

    public void setAdvanceinterest(String advanceinterest) {
        this.advanceinterest = advanceinterest;
    }

    public String getLsfcf() {
        return lsfcf;
    }

    public void setLsfcf(String lsfcf) {
        this.lsfcf = lsfcf;
    }

    public String getAdvgvn() {
        return advgvn;
    }

    public void setAdvgvn(String advgvn) {
        this.advgvn = advgvn;
    }

    public String getAdvcf() {
        return advcf;
    }

    public void setAdvcf(String advcf) {
        this.advcf = advcf;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
