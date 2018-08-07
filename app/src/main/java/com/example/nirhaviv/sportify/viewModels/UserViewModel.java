package com.example.nirhaviv.sportify.viewModels;

import android.arch.lifecycle.ViewModel;

import com.example.nirhaviv.sportify.model.AppLocalDb;
import com.example.nirhaviv.sportify.model.entities.User;
import com.example.nirhaviv.sportify.model.firebase.UsersFirebase;
import com.example.nirhaviv.sportify.model.repositories.UsersRepository;
import com.google.android.gms.tasks.Tasks;

import java.util.concurrent.Executors;

public class UserViewModel extends ViewModel {

    public void saveUser(User user) {
        UsersRepository.instance.saveUser(user);
    }
}
