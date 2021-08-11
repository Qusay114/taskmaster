package com.example.taskmaster.tasks;

public class TaskDetails {
    private String title ;
    private String description ;

    public TaskDetails(String title , String description){
        this.title = title ;
        this.description = description ;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
