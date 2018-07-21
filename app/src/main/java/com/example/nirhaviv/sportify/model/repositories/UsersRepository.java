package com.example.nirhaviv.sportify.model.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.nirhaviv.sportify.model.daos.UsersAsyncDao;
import com.example.nirhaviv.sportify.model.entities.Post;
import com.example.nirhaviv.sportify.model.entities.User;
import com.example.nirhaviv.sportify.model.firebase.UsersFirebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

public class UsersRepository {
    public static final UsersRepository instance = new UsersRepository();
    private LiveData<List<User>> allUsers;

    public UsersRepository() {
    }

    public LiveData<List<User>> getAllUsers() {
        this.allUsers = new AllUsers();
        return this.allUsers;
    }

    class AllUsers extends MutableLiveData<List<User>> {
        public AllUsers() {
            super();
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            UsersAsyncDao.getAllUsers(new OnSuccessListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    setValue(users);
                    UsersFirebase.getInstance().getAllUsers(new OnSuccessListener<List<User>>() {
                        @Override
                        public void onSuccess(List<User> users) {
                            setValue(users);
                            UsersAsyncDao.insertAllUsers(users, new OnSuccessListener<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                }
                            });
                        }
                    });
                }
            });
        }
    }

}
