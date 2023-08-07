package com.SeanBCS360.WeightTrackerApp;

public class DataModel {
    private int id;
    private int userid;
    private String weight;
    private String date;

    public DataModel(int id, int userid, String weight, String date) {
        this.id = id;
        this.userid = userid;
        this.weight = weight;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userid;
    }

    public String getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }
}

