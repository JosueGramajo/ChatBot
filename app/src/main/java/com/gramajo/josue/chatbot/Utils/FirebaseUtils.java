package com.gramajo.josue.chatbot.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.gramajo.josue.chatbot.Objects.Node;
import com.gramajo.josue.chatbot.Objects.ObjectLists.Messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by josuegramajo on 4/13/18.
 */

public class FirebaseUtils {
    public static FirebaseUtils INSTANCE = new FirebaseUtils();
    private final String collectionID = "firestore_chatbot_q";
    private final String nameID = "name";
    private final String questionID = "questions";
    private final String treeID = "Decision_Tree";

    public void saveUnansweredQuestion(String q){
        if(GlobalAccess.DOCUMENT_ID.equals("")){
            saveUnansweredQuestionInFirestore(q);
        }else{
            updateUnansweredQuestionsInFirestore(q);
        }
    }

    private void saveUnansweredQuestionInFirestore(String q){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        GlobalAccess.DOCUMENT_QUESTIONS.add(q);

        Map<String, Object> user = new HashMap<>();
        user.put(nameID, GlobalAccess.USER);
        user.put(questionID, GlobalAccess.DOCUMENT_QUESTIONS);

        db.collection(collectionID)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        GlobalAccess.DOCUMENT_ID = documentReference.getId();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }
    private void updateUnansweredQuestionsInFirestore(String q){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reference = db.collection(collectionID).document(GlobalAccess.DOCUMENT_ID);

        GlobalAccess.DOCUMENT_QUESTIONS.add(q);

        reference
                .update(questionID, GlobalAccess.DOCUMENT_QUESTIONS)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
    public void checkForExistingTestingUser(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                containsUser(document);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private void containsUser(QueryDocumentSnapshot document){
        boolean correctUser = false;

        for (Map.Entry<String, Object> entry : document.getData().entrySet()) {
            if(entry.getKey().equals(nameID) && entry.getValue().toString().equals(GlobalAccess.USER)){
                GlobalAccess.DOCUMENT_ID = document.getId();
                correctUser = true;
            }
        }
        if(correctUser){
            for (Map.Entry<String, Object> entry : document.getData().entrySet()) {
                if(entry.getKey().equals(questionID)){
                    GlobalAccess.DOCUMENT_QUESTIONS = (ArrayList<String>) entry.getValue();
                }
            }
        }
    }

    public void saveTreeInFirestore(Node node){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionID).document(treeID).set(node);
    }
    public void retrieveDecisionTree(final Context context){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(collectionID).document(treeID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                GlobalAccess.TREE = documentSnapshot.toObject(Node.class);
                JsonUtil.INSTANCE.writeJson(context, GlobalAccess.TREE, JsonUtil.FILE_TYPE.DECISION_TREE);
                Toast.makeText(context, "Arbol actualizado", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
