package com.example.nirhaviv.sportify.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.nirhaviv.sportify.R;
import com.example.nirhaviv.sportify.recycler.adapters.PostAdapter;
import com.example.nirhaviv.sportify.viewModels.PostViewModel;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedFragment extends Fragment {

    @BindView(R.id.feed_busy)
    ProgressBar progressBar;

    @BindView(R.id.posts_recycler)
    RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;
    private PostViewModel postViewModel;
    private PostAdapter postAdapter;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, view);

        RecyclerView rv = view.findViewById(R.id.posts_recycler);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        postAdapter = new PostAdapter(null);

        postViewModel.feedBusy.observe(this, (isBusy) -> {
            if (isBusy) {
                turnOnProgressBar();
            } else {
                turnOffProgressBar();
            }
        });

        bindAdapterToLivedata();
        rv.setAdapter(postAdapter);
        return view;
    }


    private void bindAdapterToLivedata() {
        turnOnProgressBar();
        postViewModel.getAllPostsForList().observe(this, (posts) -> {
            postAdapter.Posts = posts;
            postViewModel.feedBusy.setValue(false);
            turnOffProgressBar();
            postAdapter.notifyDataSetChanged();
        });
    }

    private void turnOnProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void turnOffProgressBar() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

    }
}
