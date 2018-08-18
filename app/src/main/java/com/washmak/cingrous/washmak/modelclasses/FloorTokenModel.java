package com.washmak.cingrous.washmak.modelclasses;

public class FloorTokenModel {
    private String sensor_id;
    private int  boolean_token;

    public FloorTokenModel() {
    }

    public FloorTokenModel(String sensor_id, int boolean_token) {
        this.sensor_id = sensor_id;
        this.boolean_token = boolean_token;
    }

    public String getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(String sensor_id) {
        this.sensor_id = sensor_id;
    }

    public String getBoolean_token() {
        return String.valueOf(boolean_token);
    }

    public void setBoolean_token(int boolean_token) {
        this.boolean_token = boolean_token;
    }
}
