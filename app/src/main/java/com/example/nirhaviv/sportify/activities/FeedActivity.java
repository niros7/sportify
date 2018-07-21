package com.example.nirhaviv.sportify.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.nirhaviv.sportify.R;
import com.example.nirhaviv.sportify.drawer.DrawerUtils;
import com.example.nirhaviv.sportify.fragments.FeedFragment;
import com.example.nirhaviv.sportify.fragments.PostsListFragment;

import java.io.IOException;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.feed_actionbar_title);
        setSupportActionBar(toolbar);
        try {
            DrawerUtils.getDrawer(this, toolbar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();

        PostsListFragment feedFragment= new PostsListFragment();

        fragmentTransaction.add(R.id.feed_content, feedFragment);
        fragmentTransaction.commit();
    }
}
