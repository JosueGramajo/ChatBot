package com.gramajo.josue.chatbot.Utils;

import com.gramajo.josue.chatbot.Objects.Node;

import java.util.ArrayList;

/**
 * Created by josuegramajo on 4/16/18.
 */

public class GlobalAccess {
    public static int MESSAGE_ID = 1;

    public static String USER = "";

    public static String DOCUMENT_ID = "";

    public static ArrayList<String> DOCUMENT_QUESTIONS = new ArrayList<String>();

    public static Node TREE;

    public enum ConversationContext{
        BLOCK_CARD("bloquear"),
        POINTS("puntos"),
        BALANCE("saldo"),
        NONE("");

        private String value;
        ConversationContext(String value){
            this.value = value;
        }
        public String rawValue(){
            return this.value;
        }
    }
}
