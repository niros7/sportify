package com.example.nirhaviv.sportify.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nirhaviv.sportify.R;
import com.example.nirhaviv.sportify.activities.AuthActivity;
import com.example.nirhaviv.sportify.activities.FeedActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthFragment extends Fragment {

    private View view;

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
        activity.signIn(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "login was successful", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(getActivity(), FeedActivity.class);
                    startActivity(intent);
                }
            }
        }, getActivity());
    }

}
