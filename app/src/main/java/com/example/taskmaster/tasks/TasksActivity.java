package com.example.taskmaster.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.taskmaster.R;

import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    private TaskDatabase taskDatabase ;
    private TaskDao taskDao ;
    private List<TaskDetails> tasksList ;
    private TaskAdapter taskAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        RecyclerView recyclerView = findViewById(R.id.recycleViewList);

        String lorem = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, \n" +
                "        totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. \n" +
                "        Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, \n" +
                "        sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt." ;

        tasksList = new ArrayList<>();

        taskDatabase = Room.databaseBuilder(this , TaskDatabase.class , "tasks")
                .allowMainThreadQueries().build() ;
        taskDao = taskDatabase.taskDao();
        tasksList = taskDao.findAllTasks();


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
}