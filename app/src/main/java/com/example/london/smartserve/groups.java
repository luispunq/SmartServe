package com.example.london.smartserve;

public class groups {
    private String name;
    private String num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public groups(String name, String num) {

        this.name = name;
        this.num = num;
    }

    public groups() {

    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
