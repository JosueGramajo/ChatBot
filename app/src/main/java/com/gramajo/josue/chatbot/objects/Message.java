package com.gramajo.josue.chatbot.objects;

/**
 * Created by josuegramajo on 4/10/18.
 */

public class Message {
    private int id;
    private boolean selfMessage;
    private String message;
    private String dateTime;
    

    public Message(){}

    public Message(int id, boolean selfMessage, String message, String dateTime) {
        this.id = id;
        this.selfMessage = selfMessage;
        this.message = message;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelfMessage() {
        return selfMessage;
    }

    public void setSelfMessage(boolean selfMessage) {
        this.selfMessage = selfMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
