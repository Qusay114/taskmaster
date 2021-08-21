package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.tasks.TaskDao;
import com.example.taskmaster.tasks.TaskDatabase;
import com.example.taskmaster.tasks.TaskDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = "AddTaskActivity";
    private TaskDatabase taskDatabase ;
    private TaskDao taskDao ;
    private EditText taskTitle ;
    private EditText taskDescription ;
    private Button addTask ;
    private List<Team> teams ;
    private String teamName;
    private String[] teamsNames ;
    private Handler toastHandler ;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        toastHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Toast toast = Toast.makeText(AddTaskActivity.this , "Task has been added" , Toast.LENGTH_LONG);
                toast.show();
                return false;
            }
        }) ;


        taskTitle = findViewById(R.id.editTextTaskTitle);
        taskDescription = findViewById(R.id.editTextTaskDescription);
        addTask = findViewById(R.id.buttonAddTask) ;

        taskDatabase = Room.databaseBuilder(this , TaskDatabase.class , "tasks")
                .allowMainThreadQueries().build();
        taskDao = taskDatabase.taskDao();

        Spinner spinner = findViewById(R.id.spinner);
        teamsNames = getResources().getStringArray(R.array.team_names_array);

        teams = new ArrayList<>();
         getTeamsFromApiByName() ;


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this ,
                R.array.team_names_array, android.R.layout.simple_spinner_item
                ) ;
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teamName = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                teamName = (String) parent.getItemAtPosition(0);

            }
        });

        addTask.setOnClickListener(view -> {
            String title = taskTitle.getText().toString() ;
            String description = taskDescription.getText().toString() ;

            taskDao.insertOneTask( new TaskDetails(title , description));

            Team team = teams.stream().filter(team1 -> team1.getName().equals(teamName)).collect(Collectors.toList()).get(0);
            TaskItem taskItem = TaskItem.builder().team(team).title(title).description(description).build();
            populateTaskToApi(taskItem);

        });

    }



     TaskItem populateTaskToApi(TaskItem taskItem){
        Amplify.API.mutate(ModelMutation.create(taskItem) ,
                success -> {
            Log.i(TAG, "populateTaskToApi: taskItem Title --> " + taskItem.getTitle());
            toastHandler.sendEmptyMessage(1);
                } ,
                error -> Log.i(TAG, "failed to populateTaskToApi: taskItem Title" + taskItem.getTitle())
                );
        return taskItem ;
    }


    private List<Team> getTeamsFromApiByName(){

        Amplify.API.query(ModelQuery.list(Team.class) ,
                response -> {
                            for (Team team : response.getData()){
                                Log.i(TAG, "succeed to getTeamFromApiByName: Team Name --> "+ team.getName());
                                teams.add(team) ;
                            }


                } ,
                failure -> Log.i(TAG, "failed to getTeamFromApiByName: Team Name -->" + failure.toString())
                );
        return teams ;
    }

    private void populateTeams(String[] teams){
        for (String teamName : teams) {
            Team team = Team.builder().name(teamName).build();
            Amplify.API.mutate(ModelMutation.create(team) ,
                    success -> Log.i(TAG, "successfully populate : " + teamName) ,
                    failure -> Log.i(TAG, "failed to populate : " + teamName)
                    );
        }

    }
}