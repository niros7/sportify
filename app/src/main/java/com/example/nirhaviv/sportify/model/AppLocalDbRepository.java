package com.example.nirhaviv.sportify.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.nirhaviv.sportify.model.daos.PostsDao;
import com.example.nirhaviv.sportify.model.daos.UsersDao;
import com.example.nirhaviv.sportify.model.entities.Post;
import com.example.nirhaviv.sportify.model.entities.User;

@Database(entities = {User.class, Post.class}, version = 2)
public abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract UsersDao usersDao();

    public abstract PostsDao postsDao();
}
