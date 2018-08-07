package com.example.nirhaviv.sportify.model.daos;

import android.os.AsyncTask;

import com.example.nirhaviv.sportify.model.AppLocalDb;
import com.example.nirhaviv.sportify.model.entities.User;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class UsersAsyncDao {

    public static void getUserByUid(String uid, final OnSuccessListener<User> listener) {
        class GetUserByIdTask extends AsyncTask<String, String, User> {

            @Override
            protected User doInBackground(String... uid) {
                User user = AppLocalDb.db.usersDao().getUserByUid(uid[0]);
                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                listener.onSuccess(user);
            }
        }

        GetUserByIdTask task = new GetUserByIdTask();
        task.execute(uid);
    }

    public static void insertAllUsers(List<User> users, OnSuccessListener<Boolean> listener) {
        class InsertAllUsersTask extends AsyncTask<List<User>, String, Boolean> {
            @Override
            protected Boolean doInBackground(List<User>... users) {
                for (User u : users[0]) {
                    AppLocalDb.db.usersDao().insertAllUsers(u);
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onSuccess(success);
            }
        }
        InsertAllUsersTask task = new InsertAllUsersTask();
        task.execute(users);
    }

    public static void getAllUsers(OnSuccessListener<List<User>> listener) {
        class GetAllUsersTask extends AsyncTask<String, String, List<User>> {

            @Override
            protected List<User> doInBackground(String... strings) {
                List<User> users = AppLocalDb.db.usersDao().getAllUsers();
                return users;
            }

            @Override
            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);
                listener.onSuccess(users);
            }
        }

        GetAllUsersTask task = new GetAllUsersTask();
        task.execute();
    }
}
