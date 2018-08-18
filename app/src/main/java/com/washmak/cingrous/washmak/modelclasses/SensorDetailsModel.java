package com.washmak.cingrous.washmak.modelclasses;

import java.util.Map;

public class SensorDetailsModel {
    private String block_no ;
    private String floor_no;
    private String location;

    public SensorDetailsModel(Map<String, Object> data) {
        this.block_no = (String) data.get("block_no");
        this.floor_no = (String) data.get("floor_no");
        this.location = (String) data.get("location");
    }

    public SensorDetailsModel(String block_no, String floor_no, String location) {
        this.block_no = block_no;
        this.floor_no = floor_no;
        this.location = location;
    }

    public String getBlock_no() {
        return block_no;
    }

    public void setBlock_no(String block_no) {
        this.block_no = block_no;
    }

    public String getFloor_no() {
        return floor_no;
    }

    public void setFloor_no(String floor_no) {
        this.floor_no = floor_no;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
