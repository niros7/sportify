package com.example.nirhaviv.sportify.concurrency;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

public class AsyncImageDownloader {

    AsyncTask task;
    private Uri uri;
    private Target target;

    public AsyncImageDownloader(Uri uri, Target target) {
        this.uri = uri;
        this.target = target;

        this.task = new GenericAsyncTask<Uri, Void, RequestCreator>(() -> {
            return Picasso.get().load(this.uri);
        }, target).execute(uri);
    }
}
