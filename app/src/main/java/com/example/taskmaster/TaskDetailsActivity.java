package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetailsActivity extends AppCompatActivity {

    private TextView taskTitle ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        taskTitle = findViewById(R.id.textViewTaskTitle) ;
        Intent intent = getIntent() ;
        String title = intent.getExtras().getString("taskTitle");
        taskTitle.setText(title);
    }
}