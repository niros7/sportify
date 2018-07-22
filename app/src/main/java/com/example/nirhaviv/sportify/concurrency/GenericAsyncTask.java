package com.example.nirhaviv.sportify.concurrency;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.util.concurrent.Callable;

public class GenericAsyncTask<i, b, o> extends AsyncTask<i, b, o> {

    private Callable<o> job;
    private MutableLiveData liveData = null;
    private RequestCreator RC;
    private Target target;

    public GenericAsyncTask(Callable<o> job, MutableLiveData<o> liveData) {
        this.job = job;
        this.liveData = liveData;
    }

    public GenericAsyncTask(Callable<o> job) {
        this.job = job;
    }

    public GenericAsyncTask(Callable<o> job, Target target) {
        this.job = job;
        this.target = target;
    }

    @Override
    protected o doInBackground(i... is) {
        o result = null;
        try {
            result = job.call();
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getStackTrace().toString());
        }

        return result;
    }

    @Override
    protected void onPostExecute(o o) {
        super.onPostExecute(o);
        if (o instanceof RequestCreator) {
            ((RequestCreator)o).into(this.target);
        }

        if (this.liveData != null) {
            liveData.setValue(o);
        }
    }
}
