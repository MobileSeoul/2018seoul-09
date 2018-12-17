package com.example.ggavi.ahnsungmin.nomal_mode;


public class Record {

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPedometer() {
        return pedometer;
    }

    public void setPedometer(String pedometer) {
        this.pedometer = pedometer;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String userId;
    String pedometer;
    String distance;
    String calorie;
    String time;
    String speed;
    String date;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    String datetime;
    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    String progress;

    public Record(String userId, String pedometer, String distance, String calorie, String time, String speed, String date, String progress, String datetime) {
        this.userId = userId;
        this.pedometer = pedometer;
        this.distance = distance;
        this.calorie = calorie;
        this.time = time;
        this.speed = speed;
        this.date = date;
        this.progress = progress;
        this.datetime = datetime;
    }


}
