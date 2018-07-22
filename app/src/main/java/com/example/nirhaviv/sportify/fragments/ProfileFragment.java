package com.example.nirhaviv.sportify.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nirhaviv.sportify.R;
import com.example.nirhaviv.sportify.activities.AddPostActivity;
import com.example.nirhaviv.sportify.activities.AuthActivity;
import com.example.nirhaviv.sportify.recycler.adapters.PostAdapter;
import com.example.nirhaviv.sportify.viewModels.PostViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment {

    private PostViewModel postViewModel;
    private LinearLayoutManager layoutManager;
    private PostAdapter postAdapter;

    @BindView(R.id.plus_fav)
    FloatingActionButton plusBtn;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        RecyclerView rv = view.findViewById(R.id.posts_recycler);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        postAdapter = new PostAdapter(null);

        postViewModel.getAllProfilePosts(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(this, (posts) -> {
            postAdapter.Posts = posts;
            postAdapter.notifyDataSetChanged();
        });

        rv.setAdapter(postAdapter);

        return view;
    }

    @OnClick(R.id.plus_fav)
    public void addpost(View view) {
        Intent intent = new Intent(getContext(), AddPostActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);

    }
}
