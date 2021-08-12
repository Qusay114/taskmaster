package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.taskmaster.tasks.TasksActivity;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button tasksButton = findViewById(R.id.buttonTasks);
        Button addTaskButton = findViewById(R.id.buttonAddTask) ;
        Button allTasksButton = findViewById(R.id.buttonAllTasks);

        ImageButton settingsImageButton = findViewById(R.id.imageButtonSettings);
        TextView usernameTextView = findViewById(R.id.textViewUsernameTasks);

        addTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(getApplicationContext() , AddTaskActivity.class);
                startActivity(addTaskIntent);
            }
        });

        allTasksButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent allTasksIntent = new Intent(getApplicationContext() , AllTasksActivity.class);
                startActivity(allTasksIntent);
            }
        });



        settingsImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(getApplicationContext() , SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        tasksButton.setOnClickListener(view -> {
            Intent tasksIntent = new Intent(this , TasksActivity.class);
            startActivity(tasksIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView usernameTextView = findViewById(R.id.textViewUsernameTasks);
        usernameTextView.setText(sharedPreferences.getString("username" , "test"));
    }
}