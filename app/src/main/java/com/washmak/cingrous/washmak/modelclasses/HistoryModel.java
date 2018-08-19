package com.washmak.cingrous.washmak.modelclasses;

public class HistoryModel {
    private String time;
    private String date;
    private String user;
    private String email;
    private String job;
    private String floor_id;
    private String block_id;
    private String location;
    private String status;

    public HistoryModel() {
    }

    public HistoryModel(String time, String date, String user, String email, String job, String floor_id, String block_id, String location, String status) {
        this.time = time;
        this.date = date;
        this.user = user;
        this.email = email;
        this.job = job;
        this.floor_id = floor_id;
        this.block_id = block_id;
        this.location = location;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
    }

    public String getBlock_id() {
        return block_id;
    }

    public void setBlock_id(String block_id) {
        this.block_id = block_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
