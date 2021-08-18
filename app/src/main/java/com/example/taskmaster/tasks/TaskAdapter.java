package com.example.taskmaster.tasks;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmaster.R;

import java.util.List;



public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
    private List<TaskDetails> taskDetailsList ;
    private OnTaskClickListener onTaskClickListener ;

    public interface OnTaskClickListener{
        void onMoreClicked(int position) ;
        void onDeleteTask(int position) ;
    }

    public TaskAdapter(List<TaskDetails> taskDetailsList , OnTaskClickListener onTaskClickListener){
        this.taskDetailsList = taskDetailsList ;
        this.onTaskClickListener = onTaskClickListener ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card_layout , parent , false);
        return new ViewHolder(view , onTaskClickListener);
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
        private Button deleteButton ;
        private Button moreButton ;

        public ViewHolder(@NonNull View itemView , OnTaskClickListener onTaskClickListener) {
            super(itemView);
            taskTitle  = itemView.findViewById(R.id.textViewTaskTitle);
            taskDetails = itemView.findViewById(R.id.textViewTaskDetails);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
            moreButton = itemView.findViewById(R.id.buttonMore);

            deleteButton.setOnClickListener(view -> {

                onTaskClickListener.onDeleteTask(getAdapterPosition());
            });

            moreButton.setOnClickListener(view -> {
                onTaskClickListener.onMoreClicked(getAdapterPosition());
            });
        }
    }
}