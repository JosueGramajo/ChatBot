package com.gramajo.josue.chatbot.Utils;

import com.gramajo.josue.chatbot.Objects.Node;

import java.util.ArrayList;

/**
 * Created by josuegramajo on 4/17/18.
 */

public class DecisionTree {
    public static DecisionTree INSTANCE = new DecisionTree();

    private final String CONTAINS = "contains";
    private final String CONTAINS_ALL  = "contains_all";
    private final String CARD = "card";
    private final String REPEAT = "repeat";
    private final String DEFAULT = "default";

    public void saveTree(){
        FirebaseUtils.INSTANCE.saveTreeInFirestore(generateTree());
    }

    public Node generateTree(){
        Node masterNode = new Node();
        masterNode.setDecisionType(CONTAINS);
        masterNode.addKeyWord("buenos dias");
        masterNode.addKeyWord("buenos días");
        masterNode.addKeyWord("buen dia");
        masterNode.addKeyWord("buen día");
        masterNode.addKeyWord("hola");
        masterNode.setLevel(1);
        masterNode.setResponse("Buen dia, como puedo ayudarle?");

        Node validationFailed = new Node();
        validationFailed.setDecisionType(REPEAT);
        validationFailed.setResponse("El numero de tarjeta ingresado es invalido, favor de tratar nuevamente");
        validationFailed.setLevel(3);

        Node exit = new Node();
        exit.setDecisionType(CONTAINS);
        exit.addKeyWord("otra pregunta");
        exit.addKeyWord("nueva pregunta");
        exit.addKeyWord("otra cosa");
        exit.setLevel(3);
        exit.setResponse("Alguna otra cuestion con la cual pueda ayudarle?");

        //****************************************************************
        Node node1 = new Node();
        node1.setDecisionType(CONTAINS);
        node1.addKeyWord("bloquear");
        node1.addKeyWord("bloqueo");
        node1.addKeyWord("bloqueen");
        node1.setLevel(2);
        node1.setResponse("Con mucho gusto puedo bloquear su tarjeta, podria porfavor brindarme el numero de su tarjeta?");

        Node node1_1 = new Node();
        node1_1.setDecisionType(CARD);
        node1_1.setResponse("Su tarjeta fue bloqueada exitosamente");
        node1_1.setLevel(3);

        node1.addChildren(node1_1);
        node1.addChildren(exit);
        node1.addChildren(validationFailed);
        //****************************************************************

        //****************************************************************
        Node node2 = new Node();
        node2.setDecisionType(CONTAINS);
        node2.addKeyWord("puntos");
        node2.addKeyWord("cuantos");
        node2.setLevel(2);
        node2.setResponse("Para brindarle los puntos acumulados en su tarjeta, podria porfavor brindarme el numero de su tarjeta?");

        Node node2_1 = new Node();
        node2_1.setDecisionType(CARD);
        node2_1.setResponse("Usted tiene |random_number| de puntos");
        node2_1.setLevel(3);

        node2.addChildren(node2_1);
        node2.addChildren(exit);
        node2.addChildren(validationFailed);
        //****************************************************************

        //****************************************************************
        Node node3 = new Node();
        node3.setDecisionType(CONTAINS);
        node3.addKeyWord("saldo");
        node3.setLevel(2);
        node3.setResponse("Si desea su saldo actual, favor de brindarme el numero de su tarjeta por favor");

        Node node3_1 = new Node();
        node3_1.setDecisionType(CARD);
        node3_1.setResponse("Su saldo actual es de Q.|random_number|");
        node3_1.setLevel(3);

        node3.addChildren(node3_1);
        node3.addChildren(exit);
        node3.addChildren(validationFailed);
        //****************************************************************

        //****************************************************************
        Node node4 = new Node();
        node4.setDecisionType(CONTAINS_ALL);
        node4.addKeyWord("promocion");
        node4.addKeyWord("tarjeta");
        node4.addKeyWord("hoy");
        node4.setLevel(2);
        node4.setResponse("Con mucho gusto puedo decirle las promociones del dia de hoy, me permite el numero de su tarjeta?");

        Node node4_1 = new Node();
        node4_1.setDecisionType(CARD);
        node4_1.setResponse("Las promociones para su tarjeta el dia de hoy son: \n *2x1 en cines de Cinepolis \n *30% de descuento en Cemaco \n *Doble de millas al usar su tarjeta en cualquier establecimiento por compras mayores a Q.1,00");
        node4_1.setLevel(3);

        node4.addChildren(node4_1);
        node4.addChildren(exit);
        node4.addChildren(validationFailed);
        //****************************************************************


        //****************************************************************
        Node node5 = new Node();
        node5.setDecisionType(CONTAINS_ALL);
        node5.addKeyWord("beneficio");
        node5.addKeyWord("tarjeta");
        node5.setLevel(2);
        node5.setResponse("Los beneficios de nuestras tarjetas son los siguientes: *Aceptación local e internacional \n *Opciones de financiamiento \n *Tarjetas adicionales con límite de crédito diferenciado");
        //****************************************************************

        //****************************************************************
        Node node6 = new Node();
        node6.setDecisionType(CONTAINS);
        node6.addKeyWord("otra tarjeta");
        node6.addKeyWord("nueva tarjeta");
        node6.setLevel(2);
        node6.setResponse("Los pasos para solicitar una nueva tarjeta son: 1.Llamar a nuestro centro de atencion al cliente y contactarse con un asesor \n 2.Presentar los requisitos indicados \n 3.Proporcionar una direccion para el envio de su tarjeta");
        //****************************************************************

        //****************************************************************
        Node node7 = new Node();
        node7.setDecisionType(CONTAINS_ALL);
        node7.addKeyWord("desventaja");
        node7.addKeyWord("tarjeta");
        node7.setLevel(2);
        node7.setResponse("Las desventajas de TODA tarjeta de credito son: \n *Cuando usted usa tarjetas de crédito, usted es tentado a comprar con dinero inexistente en su cuenta bancaria. \n *Cuando usted usa una tarjeta de credito, usted pide prestado dinero a un acreedor, que quiere de vuelta su dinero con intereses.");
        //****************************************************************

        //****************************************************************
        Node node8 = new Node();
        node8.setDecisionType(CONTAINS);
        node8.addKeyWord("duda");
        node8.addKeyWord("consulta");
        node8.addKeyWord("pregunta");
        node8.setLevel(2);
        node8.setResponse("Cuenteme, como puedo ayudarlo?");
        //****************************************************************

        //****************************************************************
        Node node9 = new Node();
        node9.setDecisionType(CONTAINS_ALL);
        node9.addKeyWord("fecha");
        node9.addKeyWord("corte");
        node9.setLevel(2);
        node9.setResponse("Para proporcionarle su fecha de corte, podria por favor indicarme el numero de su tarjeta?");

        Node node9_1 = new Node();
        node9_1.setDecisionType(CARD);
        node9_1.setLevel(3);
        node9_1.setResponse("Su fecha de corte es el dia 05 de cada mes");

        node9.addChildren(node9_1);
        node9.addChildren(exit);
        node9.addChildren(validationFailed);
        //****************************************************************

        //****************************************************************
        Node node10 = new Node();
        node10.setDecisionType(CONTAINS_ALL);
        node10.addKeyWord("fecha");
        node10.addKeyWord("pago");
        node10.setLevel(2);
        node10.setResponse("La fecha de pago para todas nuestras tarjetas es el fin de cada mes");
        //****************************************************************

        //****************************************************************
        Node node98 = new Node();
        node98.setDecisionType(CONTAINS);
        node98.addKeyWord("gracias");
        node98.setLevel(2);
        node98.setResponse("Ha sido un gusto servirle");
        //****************************************************************

        //****************************************************************
        Node node99 = new Node();
        node99.setDecisionType(CONTAINS_ALL);
        node99.addKeyWord("buena");
        node99.addKeyWord("onda");
        node99.setLevel(2);
        node99.setResponse("Vivo :D");
        //****************************************************************

        //****************************************************************
        Node node100 = new Node();
        node100.setDecisionType(CONTAINS_ALL);
        node100.addKeyWord("chiste");
        node100.setLevel(2);
        node100.setResponse("Que es un terapeuta? 1024 gigapeutas (☞ﾟヮﾟ)☞");
        //****************************************************************

        //****************************************************************
        Node node101 = new Node();
        node101.setDecisionType(CONTAINS);
        node101.addKeyWord("\uD83D\uDE31");
        node101.setLevel(2);
        node101.setResponse("Que paso??? \uD83D\uDE31");
        //****************************************************************

        //****************************************************************
        Node node102 = new Node();
        node102.setDecisionType(CONTAINS);
        node102.addKeyWord("\uD83D\uDE02");
        node102.setLevel(2);
        node102.setResponse("jio jio \uD83D\uDE02 \uD83D\uDE02 \uD83D\uDE02");
        //****************************************************************

        masterNode.addChildren(node1);
        masterNode.addChildren(node2);
        masterNode.addChildren(node3);
        masterNode.addChildren(node4);
        masterNode.addChildren(node5);
        masterNode.addChildren(node6);
        masterNode.addChildren(node7);
        masterNode.addChildren(node8);
        masterNode.addChildren(node9);
        masterNode.addChildren(node10);
        masterNode.addChildren(node98);
        masterNode.addChildren(node99);
        masterNode.addChildren(node100);
        masterNode.addChildren(node101);
        masterNode.addChildren(node102);

        return masterNode;
    }
}
