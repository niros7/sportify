package com.example.nirhaviv.sportify.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nirhaviv.sportify.R;
import com.example.nirhaviv.sportify.activities.AuthActivity;
import com.example.nirhaviv.sportify.activities.FeedActivity;
import com.example.nirhaviv.sportify.model.entities.User;
import com.example.nirhaviv.sportify.viewModels.PostViewModel;
import com.example.nirhaviv.sportify.viewModels.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthFragment extends Fragment {

    private View view;
    private UserViewModel userViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    public AuthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_auth, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.sign_in_btn)
    public void signIn() {
        AuthActivity activity = (AuthActivity) getActivity();
        activity.signIn(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), "login was successful", Toast.LENGTH_SHORT);

                User newUser = new User(FirebaseAuth.getInstance().getCurrentUser());

                userViewModel.saveUser(newUser);

                Intent intent = new Intent(getActivity(), FeedActivity.class);
                startActivity(intent);
            }
        }, getActivity());
    }

}
