package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.core.Amplify;

import java.util.Date;
import java.util.Random;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    private EditText username ;
    private EditText password ;
    private Button singInBtn ;
    private Handler navigateToHandler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        navigateToHandler = new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Intent goToHome = new Intent(getApplicationContext() , MainActivity.class);
                startActivity(goToHome);
                return false;
            }
        });

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        singInBtn = findViewById(R.id.signInButton);

        singInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(username.getText().toString() , password.getText().toString());
            }
        });
    }

    private void signIn(String username , String password){
        Amplify.Auth.signIn(username , password ,
                success -> {
                    Log.i(TAG, "successfully signed in -->: " + success.toString());
//                    navigateToHandler.sendEmptyMessage(1);
                    Intent goToHome = new Intent(getApplicationContext() , MainActivity.class);
                    recordAnEvent();
                    startActivity(goToHome);
                } ,
                failure -> Log.i(TAG, "failed to sign in --> " + failure.toString())
                );
    }

    private void recordAnEvent(){
        Random random = new Random();
        Integer randomAge = random.nextInt(50) + 15;
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name(TAG)
                .addProperty("Channel", "SMS")
                .addProperty("Successful", true)
                .addProperty("ProcessDuration", 792)
                .addProperty("UserAge", randomAge)
                .addProperty("Date" , String.valueOf(new Date()))
                .build();

        Amplify.Analytics.recordEvent(event);
    }
}