package com.example.london.smartserve;

public class meettransaction {
    private String date;
    private String totalamount;
    private String lsf;
    private String loaninstallments;
    private String advancepayment;
    private String paymentmode;
    private String memberid;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
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

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public meettransaction(String date, String totalamount, String lsf, String loaninstallments, String advancepayment, String paymentmode, String memberid) {

        this.date = date;
        this.totalamount = totalamount;
        this.lsf = lsf;
        this.loaninstallments = loaninstallments;
        this.advancepayment = advancepayment;
        this.paymentmode = paymentmode;
        this.memberid = memberid;
    }

    public meettransaction() {

    }
}
