package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmaster.api.RetrofitClient;
import com.example.taskmaster.tasks.TaskDao;
import com.example.taskmaster.tasks.TaskDatabase;
import com.example.taskmaster.tasks.TaskDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTaskActivity extends AppCompatActivity {

    private TaskDatabase taskDatabase ;
    private TaskDao taskDao ;
    private EditText taskTitle ;
    private EditText taskDescription ;
    private Button addTask ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskTitle = findViewById(R.id.editTextTaskTitle);
        taskDescription = findViewById(R.id.editTextTaskDescription);
        addTask = findViewById(R.id.buttonAddTask) ;

        taskDatabase = Room.databaseBuilder(this , TaskDatabase.class , "tasks")
                .allowMainThreadQueries().build();
        taskDao = taskDatabase.taskDao();

        addTask.setOnClickListener(view -> {
            Toast toast = Toast.makeText(AddTaskActivity.this , "Task has been added" , Toast.LENGTH_LONG);
            toast.show();
            TaskDetails newTask = new TaskDetails(taskTitle.getText().toString() ,
                    taskDescription.getText().toString()) ;

            taskDao.insertOneTask(newTask);


            //send the new task to the Api
            Call<TaskDetails> call = RetrofitClient.getInstance().getMyApi().addNewTask(newTask);
            call.enqueue(new Callback<TaskDetails>() {
                @Override
                public void onResponse(@NonNull Call<TaskDetails> call, @NonNull Response<TaskDetails> response) {
                    if (response.isSuccessful()) {
                        Toast toast1 = Toast.makeText(AddTaskActivity.this,
                                "The task has been saved on the Api Successfully",
                                Toast.LENGTH_LONG);
                        toast1.show();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<TaskDetails> call, @NonNull Throwable t) {
                    Toast.makeText(AddTaskActivity.this, "failed to upload it but it's saved on the data base",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}