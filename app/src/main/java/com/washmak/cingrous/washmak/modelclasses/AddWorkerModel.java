package com.washmak.cingrous.washmak.modelclasses;


import java.util.Map;

public class AddWorkerModel {
    private String name;
    private String address;
    private String phonenumber;
    private String email;
    private String from_time;
    private String to_time;
    private String type;
    private String image;
    private String messageId;

    public AddWorkerModel() {
    }

    public AddWorkerModel(String name, String address, String phonenumber, String email, String from_time, String to_time, String type, String image,String messageId) {
        this.name = name;
        this.address = address;
        this.phonenumber = phonenumber;
        this.email = email;
        this.from_time = from_time;
        this.to_time = to_time;
        this.type = type;
        this.image = image;
        this.messageId = messageId;
    }

    public AddWorkerModel(Map<String, Object> data) {

        this.name = (String) data.get("name");
        this.address = (String) data.get("address");
        this.phonenumber = (String) data.get("phonenumber");
        this.email = (String) data.get("email");
        this.from_time = (String) data.get("from_time");
        this.to_time = (String) data.get("to_time");
        this.type = (String) data.get("type");
        this.image = (String) data.get("image");

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
