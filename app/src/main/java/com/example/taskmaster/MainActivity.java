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
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.example.taskmaster.tasks.TaskDetails;
import com.example.taskmaster.tasks.TasksActivity;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView title ;
    private TextView usernameTextView ;

    private static final String TAG = "MainActivity";
    private AppBarConfiguration appBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.textViewTitle);
        title.setText("Welcome " + getUsername() + " To TaskMaster Application");
        usernameTextView = findViewById(R.id.textViewUsernameTasks);
        usernameTextView.setText(getUsername());

        Button tasksButton = findViewById(R.id.buttonTasks);
        Button addTaskButton = findViewById(R.id.buttonAddTask) ;
        Button allTasksButton = findViewById(R.id.buttonAllTasks);
        Button signUpBtn = findViewById(R.id.signUpButton);
        Button signInBtn = findViewById(R.id.signInButton);

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

        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent goToSignUp = new Intent( getApplicationContext(), SignUpActivity.class);
                startActivity(goToSignUp);

            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignIn = new Intent(getApplicationContext() , SignInActivity.class);
                startActivity(goToSignIn);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        usernameTextView = findViewById(R.id.textViewUsernameTasks);
        usernameTextView.setText(getUsername());
//        usernameTextView.setText(sharedPreferences.getString("username" , "set you username"));
    }

    void configureAmplify(){
        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());

        } catch(AmplifyException exception){
            Log.e(TAG, "onCreate: Failed to initialize Amplify plugins => " + exception.toString());
        }

    }

    private String getUsername(){
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        Log.i(TAG, "getUsername: ----------> " + authUser.getUsername());
        return authUser.getUsername();
    }

}