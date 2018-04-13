package com.gramajo.josue.chatbot.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by josuegramajo on 4/10/18.
 */

public class Message extends BaseObject{
    @JsonProperty("id")
    private int id;

    @JsonProperty("selfMessage")
    private boolean selfMessage;

    @JsonProperty("message")
    private String message;

    @JsonProperty("dateTime")
    private String dateTime;

    public Message(){}

    @JsonCreator
    public Message(@JsonProperty("id") int id,
                   @JsonProperty("selfMessage") boolean selfMessage,
                   @JsonProperty("message") String message,
                   @JsonProperty("dateTime") String dateTime) {

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
