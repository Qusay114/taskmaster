package com.example.taskmaster.tasks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insertOneTask(TaskDetails task) ;

    @Query("SELECT * FROM task_details WHERE task_title LIKE :title")
    TaskDetails findTaskByTitle(String title) ;

    @Query("SELECT * FROM task_details")
    List<TaskDetails> findAllTasks() ;

    @Delete
    void deleteTask(TaskDetails task) ;

}
