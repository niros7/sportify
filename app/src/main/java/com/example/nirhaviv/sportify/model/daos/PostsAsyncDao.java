package com.example.nirhaviv.sportify.model.daos;

import android.os.AsyncTask;

import com.example.nirhaviv.sportify.model.AppLocalDb;
import com.example.nirhaviv.sportify.model.entities.Post;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class PostsAsyncDao {

    static public void getAllPosts(final OnSuccessListener<List<Post>> listener) {
        class GetAllPostsTask extends AsyncTask<String, String, List<Post>> {

            @Override
            protected List<Post> doInBackground(String... strings) {
                List<Post> posts = AppLocalDb.db.postsDao().getAllPosts();
                return posts;
            }

            @Override
            protected void onPostExecute(List<Post> posts) {
                super.onPostExecute(posts);
                listener.onSuccess(posts);
            }

        }

        GetAllPostsTask task = new GetAllPostsTask();
        task.execute();
    }

    public static void insertAllPosts(List<Post> posts, final OnSuccessListener<Boolean> listener) {
        class InsertAllPostsTask extends AsyncTask<List<Post>, String, Boolean> {
            @Override
            protected Boolean doInBackground(List<Post>... posts) {
                for (Post st : posts[0]) {
                    AppLocalDb.db.postsDao().insertAllPosts(st);
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onSuccess(success);
            }
        }
        InsertAllPostsTask task = new InsertAllPostsTask();
        task.execute(posts);
    }

    public static void getPost(String uid, final OnSuccessListener<Post> listener) {
        class GetPostTask extends AsyncTask<String, String, Post> {
            @Override
            protected Post doInBackground(String... uid) {
                Post post = AppLocalDb.db.postsDao().GetPostByUid(uid[0]);
                return post;
            }

            @Override
            protected void onPostExecute(Post post) {
                super.onPostExecute(post);
                listener.onSuccess(post);
            }
        }
        GetPostTask task = new GetPostTask();
        task.execute(uid);
    }
}


