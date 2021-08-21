package com.example.taskmaster.tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.example.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

            getTasksItemFromApi();



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


    private List<TaskDetails> showTasksSavedInDataBase(){
        TaskDatabase taskDatabase = Room.databaseBuilder(this , TaskDatabase.class , "tasks")
                .allowMainThreadQueries().build();
        TaskDao taskDao = taskDatabase.taskDao() ;
        return taskDao.findAllTasks();
    }
}


//////////////////////////


//package com.example.taskmaster;
//
//        import android.annotation.SuppressLint;
//        import android.content.Intent;
//        import android.os.Bundle;
//        import android.os.Handler;
//        import android.os.Looper;
//        import android.os.Message;
//        import android.util.Log;
//
//        import androidx.annotation.NonNull;
//        import androidx.appcompat.app.AppCompatActivity;
//        import androidx.recyclerview.widget.LinearLayoutManager;
//        import androidx.recyclerview.widget.RecyclerView;
//        import androidx.room.Room;
//
//        import com.amplifyframework.api.graphql.model.ModelQuery;
//        import com.amplifyframework.core.Amplify;
//        import com.amplifyframework.datastore.generated.model.AmplifyModelProvider;
//        import com.amplifyframework.datastore.generated.model.Task;
//
//
//        import java.util.ArrayList;
//        import java.util.List;
//        import java.util.Objects;
//
//public class TaskActivity extends AppCompatActivity {
//    private static final String TAG = "task";
//    private List<TaskItem> taskItemList;
//    private TaskAdapter adapter;
//    private Handler handler;
//
//    private List<Task> data ;
//
//    public static final String TASK_TITLE = "task_title";
//    public static final String TASK_BODY = "task_body";
//    public static final String TASK_STATE = "task_state";
//
//    private TaskDao taskDao;
//    private AppDataBase db;
//    private RecyclerView itemRecyclerView;
//
//    @Override
//    protected void onCreate( Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_task);
//
//        RecyclerView taskRecyclerView = findViewById(R.id.list);
//
//        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                Objects.requireNonNull(taskRecyclerView.getAdapter()).notifyDataSetChanged();
//                return false;
//            }
//        });
//
//        taskItemList =  new ArrayList<>();
////        try {
////            taskItemList = getDataFromApi();
////            Log.i(TAG, "onCreate: successfully"+ getDataFromApi().get(0).getTitle());
////        }catch (Exception exception){
////            db = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, AddTask.TASK_COLLECTION).allowMainThreadQueries().build();
////            taskDao= db.taskDao();
////            taskItemList = taskDao.findAll();
////            Log.i(TAG, "onCreate: data from data base");
////        }
//        getDataFromApi() ;
//
//
//
//        adapter= new TaskAdapter(taskItemList, new TaskAdapter.OnTaskItemClickListener() {
//            @Override
//            public void onItemClicked(int position) {
//                Intent intent = new Intent(getApplicationContext(), TaskDetailsActivity.class);
//                intent.putExtra(TASK_TITLE, taskItemList.get(position).getTitle());
//                intent.putExtra(TASK_BODY, taskItemList.get(position).getBody());
//                intent.putExtra(TASK_STATE, taskItemList.get(position).getState());
//
//                startActivity(intent);
//            }
//        });
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//
//        taskRecyclerView.setLayoutManager(linearLayoutManager);
//        taskRecyclerView.setAdapter(adapter);
//
//    }
//
//    List<TaskItem> getDataFromApi(){
////        List<TaskItem> taskItems= new ArrayList<>();
//        Amplify.API.query(ModelQuery.list(Task.class),
//                response -> {
//                    for (Task task: response.getData()){
//                        taskItemList.add(new TaskItem(task.getTitle(), task.getBody(), task.getState()));
//                        Log.i(TAG, "getDataFromApi: from api ");
//                    }
//                    handler.sendEmptyMessage(1);
//                },
//                error -> Log.e(TAG, "getDataFromApi: Failed ",error ));
//
//        return taskItemList;
//    }
//
//}