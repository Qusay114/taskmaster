package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
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

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.tasks.TaskDao;
import com.example.taskmaster.tasks.TaskDatabase;
import com.example.taskmaster.tasks.TaskDetails;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = "AddTaskActivity";
    private static final int REQUEST_FOR_FILE = 188;
    private TaskDatabase taskDatabase ;
    private TaskDao taskDao ;
    private EditText taskTitle ;
    private EditText taskDescription ;
    private Button addTask ;
    private Button chooseFileBtn ;
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
        chooseFileBtn = findViewById(R.id.buttonChooseFile);

        taskDatabase = Room.databaseBuilder(this , TaskDatabase.class , "tasks")
                .allowMainThreadQueries().build();
        taskDao = taskDatabase.taskDao();

        Spinner spinner = findViewById(R.id.spinner);
        teamsNames = getResources().getStringArray(R.array.team_names_array);

//        populateTeams(teamsNames);

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

        chooseFileBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chooseFileFromDevice();
            }
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


    //to get and save file -->

    private void chooseFileFromDevice(){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile , "Choose File");
        startActivityForResult(chooseFile,REQUEST_FOR_FILE);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FOR_FILE && resultCode == RESULT_OK){
            Log.i(TAG, "onActivityResult: returned from file explorer");
            Log.i(TAG, "onActivityResult: => " + data.getData());
            Log.i(TAG, "onActivityResult: " + data.getType());

            File uploadFile = new File(getApplicationContext().getFilesDir() , "uploadFile");

            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                FileUtils.copy(inputStream , new FileOutputStream(uploadFile));

            } catch(Exception exception){
                Log.e(TAG, "onActivityResult: file upload failed" + exception.toString());
            }

            uploadFileToApiStorage(uploadFile);

        }
    }

    private void uploadFileToApiStorage(File uploadFile){
        String key = taskTitle.toString().equals(null) ? "defualtTask.jpg" :taskTitle.getText().toString()+".jpg";
        Amplify.Storage.uploadFile(
                key,
                uploadFile ,
                success -> Log.i(TAG, "uploadFileToS3: succeeded " + success.getKey()) ,
                failure -> Log.e(TAG, "uploadFileToS3: failed " + failure.toString())
        );
    }





}