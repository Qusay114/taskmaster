package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.aws.AWSApiPlugin;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

import com.example.taskmaster.tasks.TasksActivity;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import android.content.Context;


import androidx.annotation.NonNull;


import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Date;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private TextView title ;
    private TextView usernameTextView ;

    private static final String TAG = "MainActivity";
    private AppBarConfiguration appBarConfiguration;

    private static PinpointManager pinpointManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize PinpointManager
        getPinpointManager(getApplicationContext());

        title = findViewById(R.id.textViewTitle);
        title.setText("Welcome " + getUsername() + " To TaskMaster Application");
        usernameTextView = findViewById(R.id.textViewUsernameTasks);
        usernameTextView.setText(getUsername());

        Button tasksButton = findViewById(R.id.buttonTasks);
        Button addTaskButton = findViewById(R.id.buttonAddTask) ;
        Button allTasksButton = findViewById(R.id.buttonAllTasks);

        Button signOutBtn = findViewById(R.id.signOutButton);

        ImageButton settingsImageButton = findViewById(R.id.imageButtonSettings);

        addTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(getApplicationContext() , AddTaskActivity.class);
                recordAnEvent("NavigateToAddTasksActivity");
                startActivity(addTaskIntent);
            }
        });

        allTasksButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent allTasksIntent = new Intent(getApplicationContext() , AllTasksActivity.class);
                recordAnEvent("NavigateToAllTasksActivity");
                startActivity(allTasksIntent);
            }
        });



        settingsImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(getApplicationContext() , SettingsActivity.class);
                recordAnEvent("NavigateToSettingsActivity");
                startActivity(settingsIntent);
            }
        });

        tasksButton.setOnClickListener(view -> {
            Intent tasksIntent = new Intent(this , TasksActivity.class);
            recordAnEvent("NavigateToTasksActivity");
            startActivity(tasksIntent);
        });



        signOutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                signOut();
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

    private void signOut(){
        Amplify.Auth.signOut(
                () -> {
                    Intent goToLandingPage = new Intent(getApplicationContext() , LandingActivity.class);
                    startActivity(goToLandingPage);
                },

                failure -> Log.i(TAG, "signOut: failed")
        );
    }


    //for notification

    public static PinpointManager getPinpointManager(final Context applicationContext) {
        if (pinpointManager == null) {
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>(){
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i("INIT", userStateDetails.getUserState().toString());
                }

                @Override
                public void onError(Exception e) {
                    Log.e("INIT", "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig);

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            final String token = task.getResult();
                            Log.d("TAG", "Registering push notifications token: " + token);
                            pinpointManager.getNotificationClient().registerDeviceToken(token);
                        }
                    });
        }
        return pinpointManager;
    }


    private void recordAnEvent(String eventName){
        Random random = new Random();
        Integer randomAge = random.nextInt(50) + 15;
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name(eventName)
                .addProperty("Channel", "SMS")
                .addProperty("Successful", true)
                .addProperty("ProcessDuration", 792)
                .addProperty("UserAge", randomAge)
                .addProperty("Date" , String.valueOf(new Date()))
                .build();

        Amplify.Analytics.recordEvent(event);
    }

}