package com.example.nirhaviv.sportify.model.firebase;

import android.support.annotation.NonNull;

import com.example.nirhaviv.sportify.model.entities.Post;
import com.example.nirhaviv.sportify.model.entities.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class UsersFirebase {

    private static UsersFirebase instance = null;

    protected UsersFirebase() {
        // Exists only to defeat instantiation.
    }

    public static UsersFirebase getInstance() {
        if (instance == null) {
            instance = new UsersFirebase();
        }
        return instance;
    }

    public void getAllUsers(OnSuccessListener<List<User>> listener) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("users");

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> UsersList = new LinkedList<User>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User u = postSnapshot.getValue(User.class);
                    UsersList.add(u);
                }

                listener.onSuccess(UsersList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onSuccess(null);
            }
        });
    }

    public void getUserByUid(String uid, final OnSuccessListener<User> listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.orderByChild("userUid").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                listener.onSuccess(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onSuccess(null);
            }
        });

    }

    public void saveUser(User user) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(user.getUid()).setValue(user);

    }
}
