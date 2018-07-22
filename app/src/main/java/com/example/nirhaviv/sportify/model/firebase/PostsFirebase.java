package com.example.nirhaviv.sportify.model.firebase;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.example.nirhaviv.sportify.model.entities.Post;
import com.example.nirhaviv.sportify.model.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PostsFirebase {
    private static PostsFirebase instance = null;

    protected PostsFirebase() {
        // Exists only to defeat instantiation.
    }

    public static PostsFirebase getInstance() {
        if(instance == null) {
            instance = new PostsFirebase();
        }
        return instance;
    }

    public void savePost(Post post) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("posts").child(post.getUid()).setValue(post);
    }

    ValueEventListener eventListener;

    public void getPost(String uid, final OnSuccessListener<Post> listener) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");

        postsRef.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                listener.onSuccess(post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onSuccess(null);
            }
        });

    }

    public void getAllPosts(final OnSuccessListener<List<Post>> listener) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");

        eventListener = postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> postsList = new LinkedList<Post>();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Post st = postSnapshot.getValue(Post.class);
                    postsList.add(st);
                }

                listener.onSuccess(postsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onSuccess(null);
            }
        });
    }

    public void getAllCollections(final OnSuccessListener listener) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference();

        eventListener = postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot postsRef = dataSnapshot.child("posts");
                DataSnapshot usersRef = dataSnapshot.child("users");
                List<User> users = new ArrayList<>();
                List<Post> posts = new ArrayList<>();

                for (DataSnapshot postSnapshot: postsRef.getChildren()) {
                    Post p = postSnapshot.getValue(Post.class);
                    posts.add(p);
                }

                for (DataSnapshot postSnapshot: usersRef.getChildren()) {
                    User u = postSnapshot.getValue(User.class);
                    users.add(u);
                }

                listener.onSuccess(new Pair(posts, users));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onSuccess(null);
            }
        });
    }

}
