package com.example.nirhaviv.sportify.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.nirhaviv.sportify.model.entities.Post;
import com.example.nirhaviv.sportify.model.repositories.PostsRepository;
import com.example.nirhaviv.sportify.recycler.adapters.PostForList;

import java.util.List;

public class PostViewModel extends ViewModel {
    public LiveData<List<Post>> allPosts;
    public LiveData<List<PostForList>> postsForList;
    public LiveData<List<Post>> postsForProfile;

    public PostViewModel() {
        super();

        allPosts = new MutableLiveData<>();
        postsForList = new MutableLiveData<>();
    }

//    public LiveData<List<Post>> getAllPosts() {
//        allPosts = PostsRepository.instance.getAllPosts();
//        return allPosts;
//    }

    public LiveData<List<PostForList>> getAllPostsForList() {
        postsForList = PostsRepository.instance.getAllPostsForList();
        return postsForList;
    }

    public LiveData<List<Post>> getAllProfilePosts(String uid) {
        postsForProfile = PostsRepository.instance.getAllPostsForProfile(uid);
        return postsForProfile;
    }
}
