package com.washmak.cingrous.washmak.modelclasses;

public class SupervisorTriggerModel {
    private int boolean_token;
    private String location;
    private String worker_assign;
    private String time_fixed;
    private String messageId;

    public SupervisorTriggerModel() {
    }

    public SupervisorTriggerModel(int boolean_token, String location, String worker_assign, String time_fixed, String messageId) {
        this.boolean_token = boolean_token;
        this.location = location;
        this.worker_assign = worker_assign;
        this.time_fixed = time_fixed;
        this.messageId = messageId;
    }

    public int getBoolean_token() {
        return boolean_token;
    }

    public void setBoolean_token(int boolean_token) {
        this.boolean_token = boolean_token;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWorker_assign() {
        return worker_assign;
    }

    public void setWorker_assign(String worker_assign) {
        this.worker_assign = worker_assign;
    }

    public String getTime_fixed() {
        return time_fixed;
    }

    public void setTime_fixed(String time_fixed) {
        this.time_fixed = time_fixed;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
