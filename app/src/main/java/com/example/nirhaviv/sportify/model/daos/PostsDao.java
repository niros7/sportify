package com.example.nirhaviv.sportify.model.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.nirhaviv.sportify.model.entities.Post;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PostsDao {

    @Query("SELECT * FROM Post")
    List<Post> getAllPosts();

    @Query("SELECT * FROM Post WHERE Uid = :uid")
    Post GetPostByUid(String uid);

    @Insert(onConflict = REPLACE)
    void insertPost(Post post);

    @Insert(onConflict = REPLACE)
    void insertAllPosts(Post... posts);

    @Delete
    void deletePost(Post post);
}
