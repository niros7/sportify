package com.example.nirhaviv.sportify.activities;

import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.nirhaviv.sportify.R;
import com.example.nirhaviv.sportify.drawer.DrawerUtils;
import com.example.nirhaviv.sportify.fragments.ProfileFragment;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.profile_actionbar_title);
        setSupportActionBar(toolbar);
        try {
            DrawerUtils.getDrawer(this, toolbar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();

        ProfileFragment profileFragment = new ProfileFragment();

        fragmentTransaction.add(R.id.profile_content, profileFragment);
        fragmentTransaction.commit();
    }
}
