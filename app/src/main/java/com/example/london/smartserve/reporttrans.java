package com.example.london.smartserve;

public class reporttrans {
    private String totalamount;
    private String date;
    private String lsf;
    private String lsfcf;
    private String loaninstallments;
    private String loangvn;
    private String loancf;
    private String advcf;
    private String advancepayment;
    private String advgvn;
    private String memberid;
    private String paymentmode;
    private String loaninterest;
    private String advanceinterest;
    private String id;
    private String wamount;
    private String wcomm;
    private String regs;

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

    public String getLoaninstallments() {
        return loaninstallments;
    }

    public void setLoaninstallments(String loaninstallments) {
        this.loaninstallments = loaninstallments;
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

    public reporttrans(String totalamount, String date, String lsf, String lsfcf, String loaninstallments, String loangvn, String loancf, String advcf, String advancepayment, String advgvn, String memberid, String paymentmode, String loaninterest, String advanceinterest, String id, String wamount, String wcomm, String regs) {
        this.totalamount = totalamount;
        this.date = date;
        this.lsf = lsf;
        this.lsfcf = lsfcf;
        this.loaninstallments = loaninstallments;
        this.loangvn = loangvn;
        this.loancf = loancf;
        this.advcf = advcf;
        this.advancepayment = advancepayment;
        this.advgvn = advgvn;
        this.memberid = memberid;
        this.paymentmode = paymentmode;
        this.loaninterest = loaninterest;
        this.advanceinterest = advanceinterest;
        this.id = id;
        this.wamount = wamount;
        this.wcomm = wcomm;
        this.regs = regs;
    }

    public reporttrans() {
    }

    public String getAdvanceinterest() {
        return advanceinterest;
    }

    public void setAdvanceinterest(String advanceinterest) {
        this.advanceinterest = advanceinterest;
    }

    public String getLoaninterest() {
        return loaninterest;
    }

    public void setLoaninterest(String loaninterest) {
        this.loaninterest = loaninterest;
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

    public String getLoancf() {
        return loancf;
    }

    public void setLoancf(String loancf) {
        this.loancf = loancf;
    }

    public String getLoangvn() {
        return loangvn;
    }

    public void setLoangvn(String loangvn) {
        this.loangvn = loangvn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWamount() {
        return wamount;
    }

    public void setWamount(String wamount) {
        this.wamount = wamount;
    }

    public String getWcomm() {
        return wcomm;
    }

    public void setWcomm(String wcomm) {
        this.wcomm = wcomm;
    }

    public String getRegs() {
        return regs;
    }

    public void setRegs(String regs) {
        this.regs = regs;
    }
}
