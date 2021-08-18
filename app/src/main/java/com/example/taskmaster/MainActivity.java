package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
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

//        try {
//            Amplify.addPlugin(new AWSDataStorePlugin());
//            Amplify.configure(getApplicationContext());
//
//            Log.i("Tutorial", "Initialized Amplify");
//        } catch (AmplifyException e) {
//            Log.e("Tutorial", "Could not initialize Amplify", e);
//        }
//
//        Task item = Task.builder().title("Qusay").build();
//        Amplify.DataStore.save(item,
//                success -> Log.i("Tutorial","Item Saved "+ success.item().getTitle()),
//                error -> Log.e("Tutorial","not Saved",error)
//        );
//
//
//        Amplify.DataStore.query(Task.class,
//                todos -> {
//                    while (todos.hasNext()) {
//                        Task todo = todos.next();
//                        Log.i("Tutorial", "==== Todo ====");
//                        Log.i("Tutorial", "Name: " + todo.getTitle());
//                    }
//                },
//                failure -> Log.e("Tutorial", "Could not query DataStore", failure)
//        );

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