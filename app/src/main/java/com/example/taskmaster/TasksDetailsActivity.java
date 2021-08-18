package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TasksDetailsActivity extends AppCompatActivity {

    private TextView taskTitle ;
    private TextView taskDetails ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_details);

        Intent intent = getIntent() ;

        taskTitle = findViewById(R.id.textViewTaskTitle) ;
        String title = intent.getExtras().getString("taskTitle");
        taskTitle.setText(title);

        taskDetails = findViewById(R.id.textViewTaskDetails) ;
        String details = intent.getExtras().getString("taskDetails");
        taskDetails.setText(details);
    }
}