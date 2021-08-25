package com.example.taskmaster.tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TasksActivity extends AppCompatActivity {

    private static final String TAG = "TasksActivity";
    private TaskDatabase taskDatabase ;
    private TaskDao taskDao ;
    private List<TaskDetails> tasksList ;
    private TaskAdapter taskAdapter ;
    private Handler handler ;
    private RecyclerView recyclerView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

         recyclerView = findViewById(R.id.recycleViewList);

        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                return false;
            }
        }) ;


        tasksList = new ArrayList<>();

//            getTasksItemFromApi();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String teamName = sharedPreferences.getString("teamName" , "TeamA");
        getTasksByTeam(teamName);



         taskAdapter = new TaskAdapter(tasksList , new TaskAdapter.OnTaskClickListener(){

            @Override
            public void onTaskClicked(int position) {




                recordAnEvent("NavigateToTaskDetailssActivity");
            }

            @Override
            public void onDeleteTask(int position) {
            taskDao.deleteTask(tasksList.get(position));
            tasksList.remove(position);
                Toast toast = Toast.makeText(getApplicationContext() ,
                        "task has been deleted" ,
                        Toast.LENGTH_LONG);
                toast.show();
                notifyDataSetChanged();

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this ,
                LinearLayoutManager.VERTICAL ,
                false) ;

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(taskAdapter);

    }

    @SuppressLint("NotifyDataSetChanged")
    private void notifyDataSetChanged() {
        taskAdapter.notifyDataSetChanged();
    }

    void getTasksItemFromApi(){
        List<TaskDetails> taskDetailsList = new ArrayList<>();
        Amplify.API.query(ModelQuery.list(TaskItem.class) ,
                    response -> {
                            for (TaskItem taskItem : response.getData())
                            {
                                tasksList.add(new TaskDetails(taskItem.getTitle() , taskItem.getDescription()));
                                Log.i(TAG, "onCreate: the TaskItems titles are  => " + taskItem.getTitle());
                            }
                        handler.sendEmptyMessage(1);

                    } ,
                error -> {
            Log.e(TAG, "onCreate: Failed to get tasksitems => " + error.toString());
            tasksList = showTasksSavedInDataBase();
                    handler.sendEmptyMessage(1);
        });

    }

    private void getTasksByTeam(String teamName){
        Amplify.API.query(ModelQuery.list(Team.class , Team.NAME.contains(teamName)) ,
                response -> {
                        for (Team team : response.getData())
                            for (TaskItem taskItem : team.getTasks())
                                tasksList.add(new TaskDetails(taskItem.getTitle() , taskItem.getDescription()));
                        handler.sendEmptyMessage(1);
                } ,
                error -> Log.e(TAG, "getTasksByTeam: " + error.toString() )
                ) ;
    }


    private List<TaskDetails> showTasksSavedInDataBase(){
        TaskDatabase taskDatabase = Room.databaseBuilder(this , TaskDatabase.class , "tasks")
                .allowMainThreadQueries().build();
        TaskDao taskDao = taskDatabase.taskDao() ;
        return taskDao.findAllTasks();
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


