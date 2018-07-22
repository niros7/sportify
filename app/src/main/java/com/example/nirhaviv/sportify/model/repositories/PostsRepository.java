package com.example.nirhaviv.sportify.model.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Editable;
import android.util.Log;
import android.util.Pair;

import com.example.nirhaviv.sportify.model.AppLocalDb;
import com.example.nirhaviv.sportify.model.entities.Post;
import com.example.nirhaviv.sportify.model.entities.PostWithUser;
import com.example.nirhaviv.sportify.model.entities.User;
import com.example.nirhaviv.sportify.model.firebase.PostsFirebase;
import com.example.nirhaviv.sportify.model.firebase.UsersFirebase;
import com.example.nirhaviv.sportify.recycler.adapters.PostForList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class PostsRepository {
    public static final PostsRepository instance = new PostsRepository();
    public MutableLiveData<List<Post>> allPosts;
    public MutableLiveData<List<PostForList>> allPostsForList;
    public MutableLiveData<PostWithUser> lastPostWithUser;
    public MutableLiveData<List<PostForList>> allPostsForProfile;


    PostsRepository() {
    }

//    public LiveData<PostWithUser> getPostWithUser(final String postUid) {
//        synchronized (this) {
//            lastPostWithUser = new MutableLiveData<>();
//            GetPostWithUserTask task = new GetPostWithUserTask();
//            task.execute(postUid);
//        }
//
//        return lastPostWithUser;
//    }

//    public LiveData<List<Post>> getAllPosts() {
//        this.allPosts = new AllPosts();
//        return this.allPosts;
//    }

    public LiveData<List<PostForList>> getAllPostsForList() {
        synchronized (this) {
            allPostsForList = new MutableLiveData<>();
            AsyncTask task = new GetAllPostsForListTask();
            task.execute(new String[]{});
        }

        return this.allPostsForList;
    }

    public LiveData<List<PostForList>> getAllPostsForProfile(String uid) {
        synchronized (this) {
            allPostsForProfile = new MutableLiveData<>();
            AsyncTask task = new GetAllPostsForProfileTask();
            task.execute(new String[]{uid});
        }

        return this.allPostsForProfile;
    }

    public void savePost(String text, Bitmap background) {
        Post newPost = new Post();
        String newPostUri = UUID.randomUUID().toString();
        newPost.setUid(newPostUri);
        newPost.setUserUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        newPost.setTimestamp(new Date().getTime());
        newPost.setContent(text);

        Tasks.call(Executors.newSingleThreadExecutor(), () -> {
            final TaskCompletionSource<Uri> source = new TaskCompletionSource<>();
            PostsFirebase.saveImage(background, newPostUri, (newDownloadUri) -> source.setResult(newDownloadUri));
            Task<Uri> task = source.getTask();
            task.addOnCompleteListener(Executors.newSingleThreadExecutor() ,(newDownloadUri) -> {
                Uri newUri = newDownloadUri.getResult();
                newPost.setImageUri(newUri.toString());
                PostsFirebase.savePost(newPost, o -> {});
                AppLocalDb.db.postsDao().insertPost(newPost);
            });
            return null;
        });
    }

//    class AllPosts extends MutableLiveData<List<Post>> {
//        public AllPosts() {
//            super();
//            setValue(new LinkedList<Post>());
//        }
//
//        @Override
//        protected void onActive() {
//            super.onActive();
//            PostsAsyncDao.getAllPosts(new OnSuccessListener<List<Post>>() {
//                @Override
//                public void onSuccess(List<Post> posts) {
//                    setValue(posts);
//                    PostsFirebase.getInstance().getAllPosts(new OnSuccessListener<List<Post>>() {
//                        @Override
//                        public void onSuccess(List<Post> posts) {
//                            setValue(posts);
//                            PostsAsyncDao.insertAllPosts(posts, new OnSuccessListener<Boolean>() {
//                                @Override
//                                public void onSuccess(Boolean b) {
//                                }
//                            });
//                        }
//                    });
//                }
//            });
//        }
//    }


//    private class GetPostWithUserTask extends AsyncTask<String, String, PostWithUser> {
//
//        @Override
//        protected PostWithUser doInBackground(String... uid) {
//            Post post = AppLocalDb.db.postsDao().GetPostByUid(uid[0]);
//            final User[] user = new User[1];
//            PostWithUser postWithUser = new PostWithUser();
//
//            UsersFirebase.getInstance().getUserByUid(post.getUserUid(), new OnSuccessListener<User>() {
//                @Override
//                public void onSuccess(User resultUser) {
//                    user[0] = resultUser;
//                }
//            });
//
//            postWithUser.post = post;
//            postWithUser.user = user[0];
//            return postWithUser;
//        }
//
//        @Override
//        protected void onPostExecute(PostWithUser postWithUser) {
//            super.onPostExecute(postWithUser);
//            lastPostWithUser.setValue(postWithUser);
//        }
//    }

    private class GetAllPostsForListTask extends AsyncTask<String, String, List<PostForList>> {

        @Override
        protected List<PostForList> doInBackground(String... strings) {
            final TaskCompletionSource<Pair<List<Post>, List<User>>> source = new TaskCompletionSource<>();
            final TaskCompletionSource<List<PostForList>> source2 = new TaskCompletionSource<>();

            PostsFirebase.getInstance().getAllCollections((pair) -> {
                source.setResult((Pair<List<Post>, List<User>>) pair);

            });

            Task<Pair<List<Post>, List<User>>> task = source.getTask();
            task.addOnCompleteListener(Executors.newSingleThreadExecutor(), task1 -> {
                List<PostForList> list = new ArrayList<>();
                Pair<List<Post>, List<User>> tuple = task1.getResult();
                List<Post> posts = tuple.first;
                List<User> users = tuple.second;

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
                    try {
                        postForList.Background = new BitmapDrawable(Picasso.get().load(post.getImageUri()).get());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        postForList.ProfileImage = new BitmapDrawable(Picasso.get().load(user.getProfileUri()).get());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    list.add(postForList);
                });
                source2.setResult(list);
            });
            Task<List<PostForList>> task2 = source2.getTask();
            task2.addOnCompleteListener(result -> {
                allPostsForList.setValue(result.getResult());
            });

            return null;
        }
    }

    private class GetAllPostsForProfileTask extends AsyncTask<String, Void, List<PostForList>> {
        @Override
        protected List<PostForList> doInBackground(String... uid) {
            final TaskCompletionSource<Pair<List<Post>, User>> source = new TaskCompletionSource<>();
            final TaskCompletionSource<List<PostForList>> source2 = new TaskCompletionSource<>();

            PostsFirebase.getInstance().getAllCollectionsForProfile(uid[0], (pair) ->
                    source.setResult((Pair<List<Post>, User>) pair));

            Task<Pair<List<Post>, User>> task = source.getTask();
            task.addOnCompleteListener(Executors.newSingleThreadExecutor(), task1 -> {
                List<PostForList> list = new ArrayList<>();
                Pair<List<Post>, User> tuple = task1.getResult();
                List<Post> posts = tuple.first;
                User user = tuple.second;

                posts.stream().forEach((post) -> {
                    PostWithUser postWithUser = new PostWithUser();
                    postWithUser.user = user;
                    postWithUser.post = post;
                    PostForList postForList = new PostForList();
                    postForList.UserName = postWithUser.user.getName();
                    postForList.PostText = post.getContent();
                    postForList.timestamp = new Date(post.getTimestamp());
                    try {
                        postForList.Background = new BitmapDrawable(Picasso.get().load(post.getImageUri()).get());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        postForList.ProfileImage = new BitmapDrawable(Picasso.get().load(user.getProfileUri()).get());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    list.add(postForList);
                });
                source2.setResult(list);
            });
            Task<List<PostForList>> task2 = source2.getTask();
            task2.addOnCompleteListener(result -> {
                allPostsForProfile.setValue(result.getResult());
            });

            return null;
        }
    }
}
