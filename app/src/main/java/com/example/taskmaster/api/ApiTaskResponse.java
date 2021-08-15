package com.example.taskmaster.api;

import com.example.taskmaster.tasks.TaskDetails;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiTaskResponse {
    @SerializedName("_embedded")
    private EmbeddedData data;

    public static EmbeddedData getData() {
        return data;
    }

    public ApiTaskResponse() {
    }

    public static final class EmbeddedData {
        private List<TaskDetails> tasks;

        public EmbeddedData() {
        }

        public List<TaskDetails> getTasks() {
            return tasks;
        }
    }
}
