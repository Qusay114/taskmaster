package com.example.taskmaster.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmaster.R;

import java.util.List;



public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
    private List<TaskDetails> taskDetailsList ;

    public TaskAdapter(List<TaskDetails> taskDetailsList){
        this.taskDetailsList = taskDetailsList ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card_layout , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        TaskDetails taskDetails = taskDetailsList.get(position);
        holder.taskTitle.setText(taskDetails.getTitle());
        holder.taskDetails.setText(taskDetails.getDescription());

    }



    @Override
    public int getItemCount() {
        return this.taskDetailsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView taskTitle ;
        private TextView taskDetails ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle  = itemView.findViewById(R.id.textViewTaskTitle);
            taskDetails = itemView.findViewById(R.id.textViewTaskDetails) ;
        }
    }
}
