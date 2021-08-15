package com.example.taskmaster.tasks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_details")
public class TaskDetails {

    @PrimaryKey(autoGenerate = true)
    private Long id ;

    @ColumnInfo(name = "task_title")
    private String title ;
    @ColumnInfo(name = "task_description")
    private String description ;

    public TaskDetails(String title , String description){
        this.title = title ;
        this.description = description ;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
