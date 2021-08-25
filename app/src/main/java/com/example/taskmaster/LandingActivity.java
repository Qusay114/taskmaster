package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.util.Date;
import java.util.Random;

public class LandingActivity extends AppCompatActivity {

    private static final String TAG = "LandingActivity";
    private Button signUpBtn ;
    private Button signInBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        configureAmplify();

        //record events , AWS Analytics



        signUpBtn = findViewById(R.id.signUpButton);
        signInBtn = findViewById(R.id.signInButton);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignUp = new Intent(getApplicationContext() , SignUpActivity.class);
                recordAnEvent("NavigateToSignUpActivity");
                startActivity(goToSignUp);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignIn = new Intent(getApplicationContext() , SignInActivity.class);
                recordAnEvent("NavigateToSignInActivity");
                startActivity(goToSignIn);
            }
        });
    }

    void configureAmplify(){
        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.addPlugin(new AWSPinpointAnalyticsPlugin(getApplication()));
            Amplify.configure(getApplicationContext());

        } catch(AmplifyException exception){
            Log.e(TAG, "onCreate: Failed to initialize Amplify plugins => " + exception.toString());
        }

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