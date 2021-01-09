package com.example.workmachines;

public class Upload {

    String time_difference;
    long upTime;

    public Upload() {
    }

    public Upload(String time_difference) {
        this.time_difference = time_difference;
    }

    public Upload(long upTime) {
        this.upTime = upTime;
    }

    public String getTime_difference() {
        return time_difference;
    }

    public void setTime_difference(String time_difference) {
        this.time_difference = time_difference;
    }

    public long getUpTime() {
        return upTime;
    }

    public void setUpTime(long upTime) {
        this.upTime = upTime;
    }
}
