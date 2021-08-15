package com.example.taskmaster.tasks;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TaskDetails.class} , version = 1)
public abstract class TaskDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();
}
