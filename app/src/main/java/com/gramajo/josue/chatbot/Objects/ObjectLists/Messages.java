package com.gramajo.josue.chatbot.Objects.ObjectLists;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gramajo.josue.chatbot.Objects.BaseObject;
import com.gramajo.josue.chatbot.Objects.Message;

import java.util.ArrayList;

/**
 * Created by josuegramajo on 4/13/18.
 */

public class Messages extends BaseObject {

    @JsonProperty("messages")
    private ArrayList<Message> messages;

    @JsonCreator
    public Messages(@JsonProperty("messages") ArrayList<Message> messages){
        this.messages = messages;
    }

    public Messages(){}

    public ArrayList<Message> getMessages(){
        return this.messages;
    }

    public void addMessage(Message m){
        if(messages == null) messages = new ArrayList<>();
        this.messages.add(m);
    }
}
