package com.example.arvind.hackathon;

public class Blog2 {

    private String from;
    private String to;
    private String atime;
    private String dtime;

    public  Blog2()
    {

    }

    public Blog2(String from, String to, String atime, String dtime) {
        this.from = from;
        this.to = to;
        this.atime = atime;
        this.dtime = dtime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAtime() {
        return atime;
    }

    public void setAtime(String atime) {
        this.atime = atime;
    }

    public String getDtime() {
        return dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }
}
