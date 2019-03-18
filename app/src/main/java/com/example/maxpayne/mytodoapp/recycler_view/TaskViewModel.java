package com.example.maxpayne.mytodoapp.recycler_view;

import android.app.Application;

import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.db.dao.Task;
import com.example.maxpayne.mytodoapp.db.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private LiveData<List<Task>> tasks;
    private TaskRepository taskRepository;
    private LiveData<List<Task>> queryTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);

        taskRepository = new TaskRepository(application);
        tasks = taskRepository.getTasks();
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void query(int completeCode, int archiveCode) {

    }

    public void queryArchived() {
        tasks = taskRepository.getArchived();
    }

    public void queryActive() {
        tasks = taskRepository.getActive();
    }

    public void queryIncomplete() {
        tasks = taskRepository.getTasks(DbContract.ToDoEntry.NOT_ARCHIVED_CODE,
                DbContract.ToDoEntry.INCOMPLETE_CODE);
    }

    public void queryComplete() {
        tasks = taskRepository.getTasks(DbContract.ToDoEntry.NOT_ARCHIVED_CODE,
                DbContract.ToDoEntry.COMPLETE_CODE);
    }

    public void queryCancelled() {
        tasks = taskRepository.getTasks(DbContract.ToDoEntry.NOT_ARCHIVED_CODE,
                DbContract.ToDoEntry.CANCEL_CODE);
    }

    public void addTask(Task task) {
        taskRepository.addTask(task);
    }

    public void deleteTask(Task task) {
        taskRepository.deleteTask(task);
    }

    public void updateTask(Task task) {
        taskRepository.updateTask(task);
    }
}
