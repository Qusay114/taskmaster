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

import com.amplifyframework.core.Amplify;

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
                    startActivity(goToHome);
                } ,
                failure -> Log.i(TAG, "failed to sign in --> " + failure.toString())
                );
    }
}