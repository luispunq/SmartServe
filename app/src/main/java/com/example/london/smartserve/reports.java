package com.example.london.smartserve;

public class reports {
    private String id;
    private String facilitator;
    private String attendance;
    private String meetdate;
    private String venue;
    private String fines;
    private String intonadv;
    private String advcontribution;
    private String loancontribution;
    private String feesnfines;
    private String contributiontotal;
    private String cashfromoffice;
    private String cashcollected;
    private String cashpaid;
    private String cashtooffice;
    private String totalLSF;
    private String grandtotal1;
    private String grandtotal2;
    private String totalAdvancesCollected;
    private String totalLoansInstallmentsCollected;
    private String totalAdvancesBroughtForward;
    private String totalAdvancesGiven;
    private String totalAdvancesCarriedForward;
    private String totalLoansBroughtForward;
    private String totalLoansGiven;

    public String getFacilitator() {
        return facilitator;
    }

    public void setFacilitator(String facilitator) {
        this.facilitator = facilitator;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getMeetdate() {
        return meetdate;
    }

    public void setMeetdate(String meetdate) {
        this.meetdate = meetdate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCashfromoffice() {
        return cashfromoffice;
    }

    public void setCashfromoffice(String cashfromoffice) {
        this.cashfromoffice = cashfromoffice;
    }

    public String getCashcollected() {
        return cashcollected;
    }

    public void setCashcollected(String cashcollected) {
        this.cashcollected = cashcollected;
    }

    public String getCashpaid() {
        return cashpaid;
    }

    public void setCashpaid(String cashpaid) {
        this.cashpaid = cashpaid;
    }

    public String getCashtooffice() {
        return cashtooffice;
    }

    public void setCashtooffice(String cashtooffice) {
        this.cashtooffice = cashtooffice;
    }

    public String getTotalLSF() {
        return totalLSF;
    }

    public void setTotalLSF(String totalLSF) {
        this.totalLSF = totalLSF;
    }

    public String getGrandtotal1() {
        return grandtotal1;
    }

    public void setGrandtotal1(String grandtotal1) {
        this.grandtotal1 = grandtotal1;
    }

    public String getGrandtotal2() {
        return grandtotal2;
    }

    public void setGrandtotal2(String grandtotal2) {
        this.grandtotal2 = grandtotal2;
    }

    public String getTotalAdvancesCollected() {
        return totalAdvancesCollected;
    }

    public void setTotalAdvancesCollected(String totalAdvancesCollected) {
        this.totalAdvancesCollected = totalAdvancesCollected;
    }

    public String getTotalLoansInstallmentsCollected() {
        return totalLoansInstallmentsCollected;
    }

    public void setTotalLoansInstallmentsCollected(String totalLoansInstallmentsCollected) {
        this.totalLoansInstallmentsCollected = totalLoansInstallmentsCollected;
    }

    public String getTotalAdvancesBroughtForward() {
        return totalAdvancesBroughtForward;
    }

    public void setTotalAdvancesBroughtForward(String totalAdvancesBroughtForward) {
        this.totalAdvancesBroughtForward = totalAdvancesBroughtForward;
    }

    public String getTotalAdvancesGiven() {
        return totalAdvancesGiven;
    }

    public void setTotalAdvancesGiven(String totalAdvancesGiven) {
        this.totalAdvancesGiven = totalAdvancesGiven;
    }

    public String getTotalAdvancesCarriedForward() {
        return totalAdvancesCarriedForward;
    }

    public void setTotalAdvancesCarriedForward(String totalAdvancesCarriedForward) {
        this.totalAdvancesCarriedForward = totalAdvancesCarriedForward;
    }

    public String getTotalLoansBroughtForward() {
        return totalLoansBroughtForward;
    }

    public void setTotalLoansBroughtForward(String totalLoansBroughtForward) {
        this.totalLoansBroughtForward = totalLoansBroughtForward;
    }

    public String getTotalLoansGiven() {
        return totalLoansGiven;
    }

    public void setTotalLoansGiven(String totalLoansGiven) {
        this.totalLoansGiven = totalLoansGiven;
    }

    public reports(String id, String facilitator, String attendance, String meetdate, String venue, String fines, String intonadv, String advcontribution, String loancontribution, String feesnfines, String contributiontotal, String cashfromoffice, String cashcollected, String cashpaid, String cashtooffice, String totalLSF, String grandtotal1, String grandtotal2, String totalAdvancesCollected, String totalLoansInstallmentsCollected, String totalAdvancesBroughtForward, String totalAdvancesGiven, String totalAdvancesCarriedForward, String totalLoansBroughtForward, String totalLoansGiven) {
        this.id = id;
        this.facilitator = facilitator;
        this.attendance = attendance;
        this.meetdate = meetdate;
        this.venue = venue;
        this.fines = fines;
        this.intonadv = intonadv;
        this.advcontribution = advcontribution;
        this.loancontribution = loancontribution;
        this.feesnfines = feesnfines;
        this.contributiontotal = contributiontotal;
        this.cashfromoffice = cashfromoffice;
        this.cashcollected = cashcollected;
        this.cashpaid = cashpaid;
        this.cashtooffice = cashtooffice;
        this.totalLSF = totalLSF;
        this.grandtotal1 = grandtotal1;
        this.grandtotal2 = grandtotal2;
        this.totalAdvancesCollected = totalAdvancesCollected;
        this.totalLoansInstallmentsCollected = totalLoansInstallmentsCollected;
        this.totalAdvancesBroughtForward = totalAdvancesBroughtForward;
        this.totalAdvancesGiven = totalAdvancesGiven;
        this.totalAdvancesCarriedForward = totalAdvancesCarriedForward;
        this.totalLoansBroughtForward = totalLoansBroughtForward;
        this.totalLoansGiven = totalLoansGiven;
    }

    public reports() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFines() {
        return fines;
    }

    public void setFines(String fines) {
        this.fines = fines;
    }

    public String getIntonadv() {
        return intonadv;
    }

    public void setIntonadv(String intonadv) {
        this.intonadv = intonadv;
    }

    public String getAdvcontribution() {
        return advcontribution;
    }

    public void setAdvcontribution(String advcontribution) {
        this.advcontribution = advcontribution;
    }

    public String getLoancontribution() {
        return loancontribution;
    }

    public void setLoancontribution(String loancontribution) {
        this.loancontribution = loancontribution;
    }

    public String getFeesnfines() {
        return feesnfines;
    }

    public void setFeesnfines(String feesnfines) {
        this.feesnfines = feesnfines;
    }

    public String getContributiontotal() {
        return contributiontotal;
    }

    public void setContributiontotal(String contributiontotal) {
        this.contributiontotal = contributiontotal;
    }
}
