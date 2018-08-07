package com.example.nirhaviv.sportify.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Editable;

import com.example.nirhaviv.sportify.model.entities.Post;
import com.example.nirhaviv.sportify.model.repositories.PostsRepository;
import com.example.nirhaviv.sportify.recycler.adapters.PostForList;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class PostViewModel extends ViewModel {
    public LiveData<List<PostForList>> postsForList;
    public LiveData<List<PostForList>> postsForProfile;

    public MutableLiveData<Boolean> profileBusy;
    public MutableLiveData<Boolean> feedBusy;

    public PostViewModel() {
        super();

        profileBusy = new MutableLiveData<>();
        feedBusy = new MutableLiveData<>();
    }

    public LiveData<List<PostForList>> getAllPostsForList() {
        postsForList = PostsRepository.instance.getAllPostsForList();
        feedBusy.setValue(true);
        return postsForList;
    }

    public LiveData<List<PostForList>> getAllProfilePosts(String uid) {
        postsForProfile = PostsRepository.instance.getAllPostsForProfile(uid);
        profileBusy.setValue(true);
        return postsForProfile;
    }

    public void savePost(String text, Bitmap background) {
        PostsRepository.instance.savePost(text, background);
    }

    public void deletePost(String postUid) {
        PostsRepository.instance.deletePost(postUid);
    }
}
