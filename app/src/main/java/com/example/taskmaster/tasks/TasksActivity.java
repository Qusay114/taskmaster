package com.example.taskmaster.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.taskmaster.R;

import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        RecyclerView recyclerView = findViewById(R.id.recycleViewList);

        String lorem = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, \n" +
                "        totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. \n" +
                "        Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, \n" +
                "        sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt." ;

        List<TaskDetails> tasks = new ArrayList<>();
        tasks.add(new TaskDetails("Task 1" , lorem));
        tasks.add(new TaskDetails("Task 2" , lorem));
        tasks.add(new TaskDetails("Task 3" , lorem));
        tasks.add(new TaskDetails("Task 4" , lorem));
        tasks.add(new TaskDetails("Task 5" , lorem));
        tasks.add(new TaskDetails("Task 6" , lorem));
        tasks.add(new TaskDetails("Task 7" , lorem));
        tasks.add(new TaskDetails("Task 8" , lorem));


        TaskAdapter taskAdapter = new TaskAdapter(tasks) ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this ,
                LinearLayoutManager.VERTICAL ,
                false) ;

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(taskAdapter);

    }
}