package com.SeanBCS360.WeightTrackerApp;

public class DataModel {
    private final int id;
    private final int userid;
    private String weight;
    private final String date;

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

    public void setWeight(String newWeight){
        this.weight = newWeight;
    }
}

