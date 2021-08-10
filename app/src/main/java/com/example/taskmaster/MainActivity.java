package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.taskmaster.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addTaskButton = findViewById(R.id.buttonAddTask) ;
        Button allTasksButton = findViewById(R.id.buttonAllTasks);
        Button task1Button = findViewById(R.id.buttonTask1);
        Button task2Button = findViewById(R.id.buttonTask2);
        Button task3Button = findViewById(R.id.buttonTask3);
        ImageButton settingsImageButton = findViewById(R.id.imageButtonSettings);

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

        task1Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent task1Intent = new Intent(getApplicationContext() , Task1Activity.class) ;
                startActivity(task1Intent);
            }
        });

        task2Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent task2Intent = new Intent(getApplicationContext() , Task2Activity.class);
                startActivity(task2Intent);
            }
        });

        task3Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent task3Intent = new Intent(getApplicationContext() , Task3Activity.class);
                startActivity(task3Intent);
            }
        });

        settingsImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(getApplicationContext() , SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView test = findViewById(R.id.textViewTitle);
        test.setText(sharedPreferences.getString("username" , "test"));
    }
}