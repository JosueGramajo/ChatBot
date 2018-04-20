package com.gramajo.josue.chatbot.Objects;

import com.gramajo.josue.chatbot.Utils.RandomID;

import java.util.ArrayList;

/**
 * Created by josuegramajo on 4/17/18.
 */

public class Node extends BaseObject{
    private String id;
    private ArrayList<String> keyWords;
    private String response;
    private int level;
    private String decisionType;
    private ArrayList<Node> children;

    public Node(ArrayList<String> keyWords, String response, ArrayList<Node> children) {
        this.keyWords = keyWords;
        this.response = response;
        this.children = children;
    }

    public Node(ArrayList<String> keyWords, String response){
        this.keyWords = keyWords;
        this.response = response;
    }

    public void addKeyWord(String word){
        if(keyWords == null) keyWords = new ArrayList<>();
        keyWords.add(word);
    }
    public void addChildren(Node node){
        if(children == null) children = new ArrayList<>();
        children.add(node);
    }

    public Node(){
        setId(RandomID.INSTANCE.randomString(20));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(ArrayList<String> keyWords) {
        this.keyWords = keyWords;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }
}
