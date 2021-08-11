package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor =  sharedPreferences.edit();

        EditText usernameEditText = findViewById(R.id.editTextUsername);
        ImageButton saveImageButton = findViewById(R.id.imageButtonSave);
        ImageButton goHomeImageButton = findViewById(R.id.goHome);

        saveImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                preferenceEditor.putString("username" , username);
                preferenceEditor.apply();

                Toast toast = Toast.makeText(getApplicationContext(),
                        "your username has been saved!" , Toast.LENGTH_LONG);
                toast.show();

            }
        });

        goHomeImageButton.setOnClickListener(view -> {
            Intent mainActivityIntent = new Intent(SettingsActivity.this , MainActivity.class);
            startActivity(mainActivityIntent);
        });
    }
}