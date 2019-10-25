package com.example.london.smartserve;

public class groupreport {
    private String groupname;
    private String advance;
    private String loans;
    private String lsf;
    private String income;
    private String members;
    private String regs;
    private String officer;
    private int num;

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getAdvance() {
        return advance;
    }

    public void setAdvance(String advance) {
        this.advance = advance;
    }

    public String getLoans() {
        return loans;
    }

    public void setLoans(String loans) {
        this.loans = loans;
    }

    public String getLsf() {
        return lsf;
    }

    public void setLsf(String lsf) {
        this.lsf = lsf;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getRegs() {
        return regs;
    }

    public void setRegs(String regs) {
        this.regs = regs;
    }

    public String getOfficer() {
        return officer;
    }

    public void setOfficer(String officer) {
        this.officer = officer;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public groupreport(String groupname, String advance, String loans, String lsf, String income, String members, String regs, String officer, int num) {

        this.groupname = groupname;
        this.advance = advance;
        this.loans = loans;
        this.lsf = lsf;
        this.income = income;
        this.members = members;
        this.regs = regs;
        this.officer = officer;
        this.num = num;
    }

    public groupreport() {

    }
}
