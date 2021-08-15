package com.example.taskmaster.api;

import com.example.taskmaster.tasks.TaskDetails;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    String BASE_URL = "http://192.168.8.1:8080/" ;

    @GET("tasks")
    Call<ApiTaskResponse> getAllTasks();

    @POST("tasks")
    Call<TaskDetails> addNewTask(@Body TaskDetails task) ;
}
