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
import android.widget.Toast;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.core.Amplify;

import java.util.Date;
import java.util.Random;

public class VerificationActivity extends AppCompatActivity {

    private static final String TAG = "VerificationActivity" ;
    private Handler toastHandler ;
    private EditText code ;
    private Button confirmBtn ;
    private String username ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        toastHandler = new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Toast.makeText(getApplicationContext(), "your account has been confirmed successfully", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        code = findViewById(R.id.confirm_account);
        confirmBtn = findViewById(R.id.confirmButton);

        confirmBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                username = getIntent().getExtras().getString("username");
                verificateUser(username , code.getText().toString());
            }
        });

    }

    private void verificateUser(String username , String confirmationCode){
        Amplify.Auth.confirmSignUp(username , confirmationCode ,
                success -> {
            Log.i(TAG, "your account has been confirmed successfully --> " + success.toString());
            toastHandler.sendEmptyMessage(1);
                    Intent goToHome = new Intent(getApplicationContext() , MainActivity.class);
                    recordAnEvent("NavigateToMainActivity");
                    startActivity(goToHome);
                } ,
                failure -> Log.i(TAG, "failed to verificate the account --> " + failure.toString())
                );
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