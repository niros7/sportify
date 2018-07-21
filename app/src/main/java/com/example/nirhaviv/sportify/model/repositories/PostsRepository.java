package com.example.nirhaviv.sportify.model.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.example.nirhaviv.sportify.model.daos.PostsAsyncDao;
import com.example.nirhaviv.sportify.model.entities.Post;
import com.example.nirhaviv.sportify.model.entities.PostWithUser;
import com.example.nirhaviv.sportify.model.entities.User;
import com.example.nirhaviv.sportify.model.firebase.PostsFirebase;
import com.example.nirhaviv.sportify.model.firebase.UsersFirebase;
import com.example.nirhaviv.sportify.recycler.adapters.PostForList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PostsRepository {
    public static final PostsRepository instance = new PostsRepository();
    private LiveData<List<Post>> allPosts;
    private LiveData<List<PostForList>> allPostsForList;


    PostsRepository() {
    }

    public PostWithUser getPostWithUser(final String postUid) {
        final PostWithUser postWithUser = new PostWithUser();

        PostsAsyncDao.getPost(postUid, new OnSuccessListener<Post>() {
            @Override
            public void onSuccess(Post post) {
                if (post != null) {
                    postWithUser.post = post;
                    UsersFirebase.getInstance().getUserByUid(post.getUserUid(), new OnSuccessListener<User>() {
                        @Override
                        public void onSuccess(User user) {
                            postWithUser.user = user;
                        }
                    });
                } else {
                    PostsFirebase.getInstance().getPost(postUid, new OnSuccessListener<Post>() {
                        @Override
                        public void onSuccess(Post post) {
                            postWithUser.post = post;

                            UsersFirebase.getInstance().getUserByUid(post.getUserUid(), new OnSuccessListener<User>() {
                                @Override
                                public void onSuccess(User user) {
                                    postWithUser.user = user;
                                }
                            });
                        }
                    });
                }
            }
        });
        return postWithUser;
    }

    public LiveData<List<Post>> getAllPosts() {
        this.allPosts = new AllPosts();
        return this.allPosts;
    }

    public LiveData<List<PostForList>> getAllPostsForList() {
        this.allPostsForList = new AllPostsForList();
        return this.allPostsForList;
    }

    class AllPostsForList extends MutableLiveData<List<PostForList>> {
        public AllPostsForList() {
            super();
            setValue(new LinkedList<PostForList>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            LinkedList<PostForList> list = new LinkedList<>();

            PostsFirebase.getInstance().getAllPosts(new OnSuccessListener<List<Post>>() {
                @Override
                public void onSuccess(List<Post> posts) {

                    UsersFirebase.getInstance().getAllUsers(new OnSuccessListener<List<User>>() {
                        @Override
                        public void onSuccess(List<User> users) {

                            posts.stream().forEach((post) -> {
                                User user = users.stream().filter(
                                        (user1) -> user1.getUserUid().equals(post.getUserUid()))
                                        .findAny().get();

                                PostWithUser postWithUser = new PostWithUser();
                                postWithUser.user = user;
                                postWithUser.post = post;

                                PostForList postForList = new PostForList();
                                postForList.UserName = postWithUser.user.getName();
                                postForList.PostText = post.getContent();
                                postForList.timestamp = new Date(post.getTimestamp());
//                                try {
//                                    postForList.Background = new BitmapDrawable(Picasso.get().load(post.getImageUri()).get());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//                                try {
//                                    postForList.ProfileImage = new BitmapDrawable(Picasso.get().load(postWithUser.user.getProfileUri()).get());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
                                list.add(postForList);
                            });
                            setValue(list);
                        }
                    });
                }
            });
        }
    }

    class AllPosts extends MutableLiveData<List<Post>> {
        public AllPosts() {
            super();
            setValue(new LinkedList<Post>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            PostsAsyncDao.getAllPosts(new OnSuccessListener<List<Post>>() {
                @Override
                public void onSuccess(List<Post> posts) {
                    setValue(posts);
                    PostsFirebase.getInstance().getAllPosts(new OnSuccessListener<List<Post>>() {
                        @Override
                        public void onSuccess(List<Post> posts) {
                            setValue(posts);
                            PostsAsyncDao.insertAllPosts(posts, new OnSuccessListener<Boolean>() {
                                @Override
                                public void onSuccess(Boolean b) {
                                }
                            });
                        }
                    });
                }
            });
        }
    }
}
