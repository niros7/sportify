package com.example.nirhaviv.sportify.model.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.nirhaviv.sportify.model.entities.User;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UsersDao {

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Query("SELECT * FROM User WHERE Uid = :uid")
    User GetUserByUid(String uid);

    @Insert(onConflict = REPLACE)
    void insertUser(User user);

    @Delete
    void deleteUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllUsers(User... u);
}
