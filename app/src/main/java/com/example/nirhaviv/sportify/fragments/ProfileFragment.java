package com.example.nirhaviv.sportify.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.nirhaviv.sportify.R;
import com.example.nirhaviv.sportify.activities.AddPostActivity;
import com.example.nirhaviv.sportify.recycler.adapters.PostAdapter;
import com.example.nirhaviv.sportify.recycler.adapters.PostForList;
import com.example.nirhaviv.sportify.viewModels.PostViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment {

    @BindView(R.id.plus_fav)
    FloatingActionButton plusBtn;

    @BindView(R.id.profile_busy)
    ProgressBar progressBar;

    @BindView(R.id.posts_recycler)
    RecyclerView recyclerView;

    private PostViewModel postViewModel;
    private LinearLayoutManager layoutManager;
    private PostAdapter postAdapter;

    private boolean isBinded = false;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        postAdapter = new PostAdapter(null);

        postAdapter.deletePostListener = item -> {
            postViewModel.deletePost(item.postUid);
        };

        turnOnProgressBar();
        bindAdapterToLivedata();

        recyclerView.setAdapter(postAdapter);

        return view;
    }

    @OnClick(R.id.plus_fav)
    public void addPost(View view) {
        Intent intent = new Intent(getContext(), AddPostActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        postViewModel = PostViewModel.instance;
    }

    private void bindAdapterToLivedata() {
        if (!isBinded) {
            isBinded = true;

            postViewModel.profileBusy.observe(this, (isBusy) -> {
                if (isBusy) {
                    turnOnProgressBar();
                } else {
                    turnOffProgressBar();
                }
            });

            postViewModel.getAllProfilePosts(
                    FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(this, this::updatePosts);
            }
        }

    private void turnOnProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        plusBtn.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private void turnOffProgressBar() {
        progressBar.setVisibility(View.GONE);
        plusBtn.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void updatePosts(List<PostForList> posts) {
        postAdapter.Posts = posts;
        postAdapter.notifyDataSetChanged();
        postViewModel.profileBusy.setValue(false);
        turnOffProgressBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        postViewModel.profileBusy.removeObservers(this);
        postViewModel.postsForProfile.removeObservers(this);
    }
}
