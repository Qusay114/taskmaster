package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private String[] teamsNames ;
    private Spinner spinner ;
    private String teamName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor =  sharedPreferences.edit();

        EditText usernameEditText = findViewById(R.id.editTextUsername);
        ImageButton saveImageButton = findViewById(R.id.imageButtonSave);
        ImageButton goHomeImageButton = findViewById(R.id.goHome);

         spinner = findViewById(R.id.spinner);
        teamsNames = getResources().getStringArray(R.array.team_names_array);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this ,
                R.array.team_names_array, android.R.layout.simple_spinner_item
        ) ;
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teamName = (String) parent.getItemAtPosition(position);
                preferenceEditor.putString("teamName" , teamName ) ;
                preferenceEditor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                teamName = (String) parent.getItemAtPosition(0);

            }
        });

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