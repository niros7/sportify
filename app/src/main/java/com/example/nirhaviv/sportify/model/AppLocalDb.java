package com.example.nirhaviv.sportify.model;

import android.arch.persistence.room.Room;

import com.example.nirhaviv.sportify.Sportify;

public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(Sportify.context,
            AppLocalDbRepository.class, "sportifyDb.db")
            .fallbackToDestructiveMigration().build();
}