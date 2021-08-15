package com.example.taskmaster.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.taskmaster.R;
import com.example.taskmaster.TaskDetailsActivity;
import com.example.taskmaster.api.ApiTaskResponse;
import com.example.taskmaster.api.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TasksActivity extends AppCompatActivity implements Serializable {

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
            public void onMoreClicked(int position) {
                Intent intent = new Intent(getApplicationContext() , TaskDetailsActivity.class);
                intent.putExtra("taskTitle" , tasksList.get(position).getTitle());
                startActivity(intent);
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDeleteTask(int position) {
            taskDao.deleteTask(tasksList.get(position));
            tasksList.remove(position);
                Toast toast = Toast.makeText(getApplicationContext() ,
                        "task has been deleted" ,
                        Toast.LENGTH_LONG);
                toast.show();

//                Intent intent = getIntent() ;
//                finish();
//                startActivity(intent);
//                recreate();
                notifyDatasetChanged();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this ,
                LinearLayoutManager.VERTICAL ,
                false) ;

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(taskAdapter);

    }

    private void getTasks() {
        Call<ApiTaskResponse> call = RetrofitClient.getInstance().getMyApi().getAllTasks();
        call.enqueue(new Callback<ApiTaskResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiTaskResponse> call, @NotNull Response<ApiTaskResponse> response) {
                if (response.isSuccessful() && response.body().getData().getTasks() != null) {
                    Log.i("test", "onResponse: => " + response.body());
                    ApiTaskResponse apiTaskResponse = response.body();
                    ApiTaskResponse.EmbeddedData embeddedData = ApiTaskResponse.getData();
                    Log.i("test", "onResponse: => " + embeddedData.getTasks());

                    tasksList.addAll(embeddedData.getTasks());
                    notifyDatasetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiTaskResponse> call, Throwable t) {
                Log.i("test", "onFailure: called => " + t.toString());
            }


        });
    }

    private void notifyDatasetChanged() {
        taskAdapter.notifyDataSetChanged();
    }

}