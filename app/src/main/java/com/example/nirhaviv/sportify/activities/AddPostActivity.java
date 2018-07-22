package com.example.nirhaviv.sportify.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.nirhaviv.sportify.R;
import com.example.nirhaviv.sportify.drawer.DrawerUtils;
import com.example.nirhaviv.sportify.fragments.AddPostFragment;
import com.example.nirhaviv.sportify.fragments.FeedFragment;

import java.io.IOException;

public class AddPostActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_post_actionbar_title);
        setSupportActionBar(toolbar);

        try {
            DrawerUtils.getDrawer(this, toolbar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();

        AddPostFragment addPostFragment = new AddPostFragment();

        fragmentTransaction.add(R.id.add_post_content, addPostFragment);
        fragmentTransaction.commit();

    }
}
