package com.example.nirhaviv.sportify.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nirhaviv.sportify.R;
import com.example.nirhaviv.sportify.recycler.adapters.PostAdapter;
import com.example.nirhaviv.sportify.viewModels.PostViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private PostViewModel postViewModel;
    private LinearLayoutManager layoutManager;
    private PostAdapter postAdapter;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        RecyclerView rv = view.findViewById(R.id.posts_recycler);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        postAdapter = new PostAdapter(null);

//        postViewModel.getAllProfilePosts(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(this, (posts) -> {
//            postAdapter.Posts = posts;
//            postAdapter.notifyDataSetChanged();
//        });

        rv.setAdapter(postAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);

    }
}
