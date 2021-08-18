package com.example.taskmaster.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.example.taskmaster.R;

import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    private static final String TAG = "TasksActivity";
    private TaskDatabase taskDatabase ;
    private TaskDao taskDao ;
    private List<TaskDetails> tasksList ;
    private TaskAdapter taskAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        RecyclerView recyclerView = findViewById(R.id.recycleViewList);


        tasksList = new ArrayList<>();

        try {
            tasksList = getTasksItemFromApi();
            Log.i(TAG, "get The data from the API , Sample " + getTasksItemFromApi().get(0).getTitle() );
        } catch (Exception e){
            taskDatabase = Room.databaseBuilder(this , TaskDatabase.class , "tasks")
                    .allowMainThreadQueries().build() ;
            taskDao = taskDatabase.taskDao();
            tasksList = taskDao.findAllTasks();
            Log.i(TAG, "get the data from room database");
        }



         taskAdapter = new TaskAdapter(tasksList , new TaskAdapter.OnTaskClickListener(){

            @Override
            public void onTaskClicked(int position) {

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

    List<TaskDetails> getTasksItemFromApi(){
        List<TaskDetails> taskDetailsList = new ArrayList<>();
        Amplify.API.query(ModelQuery.list(TaskItem.class) ,
                    response -> {
                            for (TaskItem taskItem : response.getData())
                            {
                                taskDetailsList.add(new TaskDetails(taskItem.getTitle() , taskItem.getDescription()));
                                Log.i(TAG, "onCreate: the TaskItems titles are => " + taskItem.getTitle());
                            }

                    } ,
                error -> Log.e(TAG, "onCreate: Failed to get tasksitems => " + error.toString()));

        return taskDetailsList ;
    }
}