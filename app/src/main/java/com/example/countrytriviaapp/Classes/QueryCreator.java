package com.example.countrytriviaapp.Classes;

import androidx.annotation.NonNull;

import com.example.countrytriviaapp.Interfaces.IAsyncCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QueryCreator {
    public static void generateTriviaQuery(DatabaseReference databaseReference, int index, final IAsyncCallback callback) {

        String rand = String.valueOf((int) Math.floor(Math.random() * 249));
        databaseReference.child(rand).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Country val = snapshot.getValue(Country.class);
                if (val != null) {
                    callback.processFinished(val);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (index != 0)
            generateTriviaQuery(databaseReference, index - 1, callback);
    }

}
