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

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private EditText username ;
    private EditText email ;
    private EditText password ;
    private Button signUpBtn ;
    private Handler toastHandler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toastHandler = new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Toast.makeText(getApplicationContext() , "signed up successfully " , Toast.LENGTH_LONG).show();
                return false;
            }
        }) ;

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signUpBtn = findViewById(R.id.signUpButton);

        signUpBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                signUp(username.getText().toString() ,
                        password.getText().toString() ,
                        email.getText().toString()
                        );

                Intent goToConfirmation = new Intent(getApplicationContext() , VerificationActivity.class);
                goToConfirmation.putExtra("username" , username.getText().toString());
                startActivity(goToConfirmation);

            }
        });

    }


    private void signUp(String username ,  String password , String email ){
        Amplify.Auth.signUp(username , password ,
                AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email() , email)
                .build() ,
                success -> {
            Log.i(TAG, "signUp: succeeded --> " + success.toString());
            toastHandler.sendEmptyMessage(1);
                } ,
                failure -> Log.i(TAG, "signUp: failed --> " + failure.toString())
                );
    }


}