package com.gramajo.josue.chatbot.Utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gramajo.josue.chatbot.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by josuegramajo on 4/13/18.
 */

public class FirebaseUtils {
    public static FirebaseUtils INSTANCE = new FirebaseUtils();

    public void saveUnansweredQuestion(String q){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("questions");
        ref.setValue(q);
    }
    public void saveUnansweredQuestionInFirestore(String q){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        ArrayList<String> s = new ArrayList<String>();
        s.add(q);
        s.add(MainActivity.user);

        Map<String, Object> user = new HashMap<>();
        user.put("unanswered", s);

        db.collection("firestore_q")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
