package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

public class LandingActivity extends AppCompatActivity {

    private static final String TAG = "LandingActivity";
    private Button signUpBtn ;
    private Button signInBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        configureAmplify();

        signUpBtn = findViewById(R.id.signUpButton);
        signInBtn = findViewById(R.id.signInButton);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignUp = new Intent(getApplicationContext() , SignUpActivity.class);
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
}