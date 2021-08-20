package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.example.taskmaster.tasks.TaskDao;
import com.example.taskmaster.tasks.TaskDatabase;
import com.example.taskmaster.tasks.TaskDetails;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = "AddTaskActivity";
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
            String title = taskTitle.getText().toString() ;
            String description = taskDescription.getText().toString() ;

            taskDao.insertOneTask( new TaskDetails(title , description));

            TaskItem taskItem = TaskItem.builder().title(title).description(description).build();
            populateTaskToApi(taskItem);

            Toast toast = Toast.makeText(AddTaskActivity.this , "Task has been added" , Toast.LENGTH_LONG);
            toast.show();

        });

    }



     TaskItem populateTaskToApi(TaskItem taskItem){
        Amplify.API.mutate(ModelMutation.create(taskItem) ,
                success -> Log.i(TAG, "populateTaskToApi: " + taskItem.getTitle()) ,
                error -> Log.i(TAG, "populateTaskToApi: " + taskItem.getTitle())
                );
        return taskItem ;
    }
}