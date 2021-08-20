package com.example.taskmaster.tasks;

import java.util.ArrayList;
import java.util.List;

public class TaskDataManager {
    private TaskDataManager instance = null;
    private List<TaskDetails> taskDetailsList = new ArrayList<>();

    public TaskDataManager(){}

    public TaskDataManager getInstance() {
        if (instance == null)
            instance = new TaskDataManager();
        return instance;
    }

    public List<TaskDetails> getData(){
        return taskDetailsList ;
    }

    public void setData(List<TaskDetails> data){
        taskDetailsList = data ;
    }
}
