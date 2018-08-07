package com.example.nirhaviv.sportify.model.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.nirhaviv.sportify.model.AppLocalDb;
import com.example.nirhaviv.sportify.model.daos.UsersAsyncDao;
import com.example.nirhaviv.sportify.model.entities.Post;
import com.example.nirhaviv.sportify.model.entities.User;
import com.example.nirhaviv.sportify.model.firebase.UsersFirebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

public class UsersRepository {
    public static final UsersRepository instance = new UsersRepository();
    private LiveData<List<User>> allUsers;

    public UsersRepository() {
        this.allUsers = new AllUsers();
    }

    public void saveUser(User user) {
        Tasks.call(Executors.newSingleThreadExecutor(), () -> {
            AppLocalDb.db.usersDao().insertUser(user);
            UsersFirebase.getInstance().saveUser(user);
            return true;
        });
    }

    public LiveData<List<User>> getAllUsers() {
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
