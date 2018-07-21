package com.example.nirhaviv.sportify.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import java.util.LinkedList;

public class PostsListFragment extends Fragment {

    private LinearLayoutManager layoutManager;
    private PostViewModel postViewModel;
    private PostAdapter postAdapter;

    public PostsListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_posts_list, container, false);
        RecyclerView rv = view.findViewById(R.id.posts_recycler);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        postAdapter = new PostAdapter(null);

        postViewModel.getAllPostsForList().observe(this, (posts) -> {
            if (posts != null) {
                postAdapter.Posts = posts;
            } else {
                postAdapter.Posts = new LinkedList<>();
            }
            postAdapter.notifyDataSetChanged();

        });

        rv.setAdapter(postAdapter);

        return view;
    }
}
