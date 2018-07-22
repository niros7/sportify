package com.example.nirhaviv.sportify.model.firebase;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.example.nirhaviv.sportify.model.entities.Post;
import com.example.nirhaviv.sportify.model.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PostsFirebase {
    private static PostsFirebase instance = null;
    ValueEventListener eventListener;

    protected PostsFirebase() {
        // Exists only to defeat instantiation.
    }

    public static PostsFirebase getInstance() {
        if (instance == null) {
            instance = new PostsFirebase();
        }
        return instance;
    }

    public static void saveImage(Bitmap imageBmp, String uid, final OnSuccessListener<Uri> listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference imagesRef = storage.getReference().child("images").child(uid);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!urlTask.isSuccessful());
            Uri downloadUrl = urlTask.getResult();
            listener.onSuccess(downloadUrl);
        });
    }

    public static void savePost(Post newPost, OnSuccessListener onSuccessListener) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("posts").child(newPost.getUid()).setValue(newPost).addOnSuccessListener(onSuccessListener);
    }

    public void savePost(Post post) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("posts").child(post.getUid()).setValue(post);
    }

    public void getPost(String uid, final OnSuccessListener<Post> listener) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");

        ValueEventListener valueEventListener =
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

        postsRef.removeEventListener(valueEventListener);
    }

    public void getAllPosts(final OnSuccessListener<List<Post>> listener) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");

        eventListener = postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> postsList = new LinkedList<Post>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
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
        DatabaseReference fbPostsRef = FirebaseDatabase.getInstance().getReference();

        eventListener = fbPostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot postsRef = dataSnapshot.child("posts");
                DataSnapshot usersRef = dataSnapshot.child("users");
                List<User> users = new ArrayList<>();
                List<Post> posts = new ArrayList<>();

                for (DataSnapshot postSnapshot : postsRef.getChildren()) {
                    Post p = postSnapshot.getValue(Post.class);
                    posts.add(p);
                }

                for (DataSnapshot postSnapshot : usersRef.getChildren()) {
                    User u = postSnapshot.getValue(User.class);
                    users.add(u);
                }

                listener.onSuccess(new Pair(posts, users));
                fbPostsRef.removeEventListener(eventListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onSuccess(null);
            }
        });

    }

    public void getAllCollectionsForProfile(String userUid, final OnSuccessListener listener) {
        DatabaseReference fbPostsRef = FirebaseDatabase.getInstance().getReference();

        eventListener = fbPostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot postsRef = dataSnapshot.child("posts");
                DataSnapshot usersRef = dataSnapshot.child("users");
                List<User> users = new ArrayList<>();
                List<Post> posts = new ArrayList<>();

                for (DataSnapshot postSnapshot : postsRef.getChildren()) {
                    Post p = postSnapshot.getValue(Post.class);
                    posts.add(p);
                }

                for (DataSnapshot postSnapshot : usersRef.getChildren()) {
                    User u = postSnapshot.getValue(User.class);
                    users.add(u);
                }

                posts = posts.stream().filter(post -> post.getUserUid().equals(userUid)).collect(Collectors.toList());
                User user = users.stream().filter(currUser -> currUser.getUserUid().equals(userUid)).findFirst().get();

                listener.onSuccess(new Pair(posts, user));
                fbPostsRef.removeEventListener(eventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onSuccess(null);
            }
        });

    }
}
