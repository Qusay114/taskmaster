package com.example.taskmaster.tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.example.taskmaster.R;

import java.io.File;

public class TaskDetailsActivity extends AppCompatActivity {
    private static final String TAG = "TaskDetailsActivity";
    private String taskTitle ;
    private TextView taskTitleView ;
    private String taskDescription ;
    private TextView taskDescriptionView ;
    private File downloadedImage ;
    private ImageView taskImage ;
    private Handler handleImageView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        handleImageView = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                setTaskImage() ;
                return false;
            }
        });

        Intent intent = getIntent();
        taskTitle = intent.getExtras().getString("taskTitle");
        taskDescription = intent.getExtras().getString("taskDescription") ;

        taskTitleView = findViewById(R.id.taskTitle);
        taskDescriptionView = findViewById(R.id.taskDescription);

        taskTitleView.setText(taskTitle);
        taskDescriptionView.setText(taskDescription);

        taskImage = findViewById(R.id.downloadedImage);


        Log.i(TAG, "onCreate:  DIRCTORY -->   " +getApplicationContext().getFilesDir());
        getFileFromApi();

    }

    private void setTaskImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8; // down sizing image as it throws OutOfMemory  Exception for larger images
        Bitmap bitmap = BitmapFactory.decodeFile(downloadedImage.getAbsolutePath() , options);
        taskImage.setImageBitmap(bitmap);
    }


    private void getFileFromApi(){
        Amplify.Storage.downloadFile(
                taskTitle+".jpg" ,
                new File(getApplicationContext().getFilesDir() + "test.jpg") ,
                success -> {
                    Log.i(TAG, "getFileFromApi: successfully   ----> " + success.toString());
                    downloadedImage = success.getFile();
                    handleImageView.sendEmptyMessage(1);
                },
                failure -> Log.i(TAG, "getFileFromApi:  failed  ---> " + failure.toString())
        ) ;
    }
}