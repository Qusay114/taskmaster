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
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.tasks.TaskDao;
import com.example.taskmaster.tasks.TaskDatabase;
import com.example.taskmaster.tasks.TaskDetails;

import java.util.ArrayList;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = "AddTaskActivity";
    private TaskDatabase taskDatabase ;
    private TaskDao taskDao ;
    private EditText taskTitle ;
    private EditText taskDescription ;
    private Button addTask ;
    private List<Team> teams ;

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

            teams = new ArrayList<>();
            getTeamFromApiByName("Coders");
//            Team team = Team.builder().name("Coders").build();
            Team team = teams.get(0);
            team = populateTeamToApi(team);
            TaskItem taskItem = TaskItem.builder().team(team).title(title).description(description).build();
            populateTaskToApi(taskItem);

            Toast toast = Toast.makeText(AddTaskActivity.this , "Task has been added" , Toast.LENGTH_LONG);
            toast.show();

        });

    }



     TaskItem populateTaskToApi(TaskItem taskItem){
        Amplify.API.mutate(ModelMutation.create(taskItem) ,
                success -> Log.i(TAG, "populateTaskToApi: taskItem Title --> " + taskItem.getTitle()) ,
                error -> Log.i(TAG, "failed to populateTaskToApi: taskItem Title" + taskItem.getTitle())
                );
        return taskItem ;
    }

    Team populateTeamToApi(Team team){
        Amplify.API.mutate(ModelMutation.create(team) ,
                success -> Log.i(TAG, "populateTeamToApi: team Name --> " + team.getName()),
                error -> Log.i(TAG, "failed to populateTeamToApi: team Name -->  " + team.getName())
                );
        return team ;
    }

    Team getTeamFromApiByName(String name){

        Amplify.API.query(ModelQuery.list(Team.class , Team.NAME.contains(name)) ,
                response -> {
                            for (Team team : response.getData()){
                                Log.i(TAG, "succeed to getTeamFromApiByName: Team Name --> "+ team.getName());
                                teams.add(team) ;
                            }


                } ,
                failure -> Log.i(TAG, "failed to getTeamFromApiByName: Team Name -->" + failure.toString())
                );
        return teams.get(0) ;
    }
}