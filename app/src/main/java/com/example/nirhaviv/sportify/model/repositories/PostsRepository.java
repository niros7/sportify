package com.example.nirhaviv.sportify.model.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Pair;

import com.example.nirhaviv.sportify.model.AppLocalDb;
import com.example.nirhaviv.sportify.model.entities.Post;
import com.example.nirhaviv.sportify.fragments.PostWithUser;
import com.example.nirhaviv.sportify.model.entities.User;
import com.example.nirhaviv.sportify.model.firebase.PostsFirebase;
import com.example.nirhaviv.sportify.recycler.adapters.PostForList;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class PostsRepository {
    public static final PostsRepository instance = new PostsRepository();
    public MutableLiveData<List<PostForList>> allPostsForList;
    public MutableLiveData<List<PostForList>> allPostsForProfile;

    PostsRepository() {
        allPostsForList = new MutableLiveData<>();
        allPostsForProfile = new MutableLiveData<>();
    }

    public LiveData<List<PostForList>> getAllPostsForList() {
        synchronized (this) {
            AsyncTask task = new GetAllPostsForListTask();
            task.execute(new String[]{});
        }

        return this.allPostsForList;
    }

    public LiveData<List<PostForList>> getAllPostsForProfile(String uid) {
        synchronized (this) {
            AsyncTask task = new GetAllPostsForProfileTask();
            task.execute(new String[]{uid});
        }

        return this.allPostsForProfile;
    }

    public void savePost(String text, Bitmap background) {

        Tasks.call(Executors.newSingleThreadExecutor(), () -> {
            Post newPost = new Post();
            String newPostUid = UUID.randomUUID().toString();
            newPost.setUid(newPostUid);
            newPost.setUserUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
            newPost.setTimestamp(new Date().getTime());
            newPost.setContent(text);

            final TaskCompletionSource<Uri> source = new TaskCompletionSource<>();
            PostsFirebase.saveImage(background, newPostUid, (newDownloadUri) -> source.setResult(newDownloadUri));
            Task<Uri> task = source.getTask();
            task.addOnCompleteListener(Executors.newSingleThreadExecutor(), (newDownloadUri) -> {
                Uri newUri = newDownloadUri.getResult();
                newPost.setImageUri(newUri.toString());
                PostsFirebase.savePost(newPost, o -> {});
                // for cache!
                Picasso.get().load(newPost.getImageUri());
                AppLocalDb.db.postsDao().insertPost(newPost);
            });

            return null;
        });
    }

    public void deletePost(String postUid) {
        Tasks.call(Executors.newSingleThreadExecutor(), () -> {
            PostsFirebase.getInstance().deletePost(postUid);
            AppLocalDb.db.postsDao().deletePost(postUid);
            return true;
        });
    }

    private List<PostForList> makePostsForList(List<Post> posts, List<User> users) {
        List<PostForList> result = new ArrayList<>();

        List<PostForList> finalResult = result;

        posts.stream().forEach((post) -> {
            User user = users.stream().filter(
                    (user1) -> user1.getUserUid().equals(post.getUserUid()))
                    .findAny().get();
            PostForList postForList = new PostForList();
            postForList.postUid = post.uid;
            postForList.UserName = user.getName();
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

            finalResult.add(postForList);
        });

        result = finalResult.stream().sorted(Comparator.comparing(PostForList::getTimestamp).reversed()).collect(Collectors.toList());
        return result;
    }

    private List<PostForList> makePostsForList(List<Post> posts, User user) {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        return makePostsForList(posts, userList);
    }

    private class GetAllPostsForListTask extends AsyncTask<String, String, List<PostForList>> {

        @Override
        protected List<PostForList> doInBackground(String... strings) {
            final TaskCompletionSource<Pair<List<Post>, List<User>>> source = new TaskCompletionSource<>();

            PostsFirebase.getInstance().getAllCollections((pair) -> {
                Tasks.call(Executors.newSingleThreadExecutor(), () -> {
                    Pair<List<Post>, List<User>> tuple = (Pair<List<Post>, List<User>>) pair;
                    List<Post> posts = tuple.first;
                    List<User> users = tuple.second;
                    List<PostForList> result = makePostsForList(posts, users);
                    allPostsForList.postValue(result);

                    AppLocalDb.db.usersDao().insertAllUsers(users.toArray(new User[0]));
                    AppLocalDb.db.postsDao().insertAllPosts(posts.toArray(new Post[0]));
                    return true;
                });
            });

            List<Post> postsFromDb = AppLocalDb.db.postsDao().getAllPosts();
            List<User> usersFromDb = AppLocalDb.db.usersDao().getAllUsers();

            return null;
        }
    }

    private class GetAllPostsForProfileTask extends AsyncTask<String, Void, List<PostForList>> {
        @Override
        protected List<PostForList> doInBackground(String... uid) {
            final TaskCompletionSource<Pair<List<Post>, User>> source = new TaskCompletionSource<>();

            PostsFirebase.getInstance().getAllCollectionsForProfile(uid[0], (pair) -> {
                Tasks.call(Executors.newSingleThreadExecutor(), () -> {
                    Pair<List<Post>, User> tuple = (Pair<List<Post>, User>) pair;
                    List<Post> posts = tuple.first;
                    User user = tuple.second;
                    List<PostForList> result = makePostsForList(posts, user);
                    allPostsForProfile.postValue(result);

                    AppLocalDb.db.usersDao().insertAllUsers(user);
                    AppLocalDb.db.postsDao().insertAllPosts(posts.toArray(new Post[0]));

                    return true;
                });
            });

            List<Post> postsFromDb = AppLocalDb.db.postsDao().getAllPosts();
            User usersFromDb = AppLocalDb.db.usersDao().getUserByUid(uid[0]);

            allPostsForProfile.postValue(makePostsForList(postsFromDb, usersFromDb));

            return null;
        }
    }
}
