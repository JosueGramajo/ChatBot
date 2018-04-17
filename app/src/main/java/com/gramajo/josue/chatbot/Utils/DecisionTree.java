package com.gramajo.josue.chatbot.Utils;

import com.gramajo.josue.chatbot.Objects.Node;

import java.util.ArrayList;

/**
 * Created by josuegramajo on 4/17/18.
 */

public class DecisionTree {
    public static DecisionTree INSTANCE = new DecisionTree();

    public void saveTree(){
        Node masterNode = new Node();
        ArrayList<String> keyw1 = new ArrayList<>();
        keyw1.add("hola");
        keyw1.add("buenos dias");
        keyw1.add("buenos días");
        keyw1.add("buen dia");
        keyw1.add("buen día");
        masterNode.setKeyWords(keyw1);
        masterNode.setDecisionType("contains");
        masterNode.setResponse("Buen dia, como puedo ayudarle?");
        masterNode.setLevel(1);


        Node node_card_validation_failed = new Node();
        ArrayList<String> kcvf = new ArrayList<>();
        kcvf.add("validation_failed");
        node_card_validation_failed.setKeyWords(kcvf);
        node_card_validation_failed.setDecisionType("repeat");
        node_card_validation_failed.setResponse("El numero de tarjeta ingresado es invalido, favor de tratar nuevamente");
        node_card_validation_failed.setLevel(3);

        //****************************************************************
        Node node1 = new Node();
        ArrayList<String> keyw2 = new ArrayList<>();
        keyw2.add("bloquear");
        keyw2.add("bloqueo");
        keyw2.add("bloqueen");
        node1.setKeyWords(keyw2);
        node1.setDecisionType("contains");
        node1.setResponse("Con mucho gusto puedo bloquear su tarjeta, podria porfavor brindarme el numero de su tarjeta?");
        node1.setLevel(2);

        Node node1_1 = new Node();
        ArrayList<String> kw1_1 = new ArrayList<>();
        kw1_1.add("");
        node1_1.setKeyWords(kw1_1);
        node1_1.setDecisionType("card");
        node1_1.setResponse("Su tarjeta fue bloqueada exitosamente");
        node1_1.setLevel(3);

        ArrayList<Node> node1_children = new ArrayList<>();
        node1_children.add(node1_1);
        node1_children.add(node_card_validation_failed);
        node1.setChildren(node1_children);
        //****************************************************************

        //****************************************************************
        Node  node2 = new Node();
        ArrayList<String> keyw3 = new ArrayList<>();
        keyw3.add("puntos");
        keyw3.add("cuantos");
        node2.setKeyWords(keyw3);
        node2.setDecisionType("contains");
        node2.setResponse("Para brindarle los puntos acumulados en su tarjeta, podria porfavor brindarme el numero de su tarjeta?");
        node2.setLevel(2);

        Node node2_1 = new Node();
        ArrayList<String> kw2_1 = new ArrayList<>();
        kw2_1.add("validtaion_successful");
        node2_1.setKeyWords(kw2_1);
        node2_1.setDecisionType("card");
        node2_1.setResponse("Usted tiene |random_number| de puntos");
        node2_1.setLevel(3);

        ArrayList<Node> node2_children = new ArrayList<>();
        node2_children.add(node2_1);
        node2_children.add(node_card_validation_failed);
        node2.setChildren(node2_children);
        //****************************************************************

        //****************************************************************
        Node node3 = new Node();
        ArrayList<String> keyw4 = new ArrayList<>();
        keyw4.add("saldo");
        node3.setKeyWords(keyw4);
        node3.setDecisionType("contains");
        node3.setResponse("Si desea su saldo actual, favor de brindarme el numero de su tarjeta por favor");
        node3.setLevel(2);

        Node node3_1 = new Node();
        ArrayList<String> kw3_1 = new ArrayList<>();
        kw3_1.add("validtaion_successful");
        node3_1.setKeyWords(kw3_1);
        node3_1.setDecisionType("card");
        node3_1.setResponse("Su saldo actual es de Q.|random_number|");
        node3_1.setLevel(3);

        ArrayList<Node> node3_children = new ArrayList<>();
        node3_children.add(node3_1);
        node3_children.add(node_card_validation_failed);
        node3.setChildren(node3_children);
        //****************************************************************

        ArrayList<Node> master_node_children = new ArrayList<>();
        master_node_children.add(node1);
        master_node_children.add(node2);
        master_node_children.add(node3);
        masterNode.setChildren(master_node_children);

        FirebaseUtils.INSTANCE.saveTreeInFirestore(masterNode);
    }
}
