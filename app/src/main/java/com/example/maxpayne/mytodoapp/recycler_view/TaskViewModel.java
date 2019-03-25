package com.example.maxpayne.mytodoapp.recycler_view;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.db.dao.Task;
import com.example.maxpayne.mytodoapp.db.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final String ACTIVE      = "ACTIVE";
    private final String INCOMPLETE  = "INCOMPLETE";
    private final String COMPLETE    = "COMPLETE";
    private final String CANCEL      = "CANCEL";
    private final String ARCHIVED    = "ARCHIVED";

    private MutableLiveData<Boolean>    swipeEnabled = new MutableLiveData<>();
    private MutableLiveData<String>     title = new MutableLiveData<>();
    private MutableLiveData<Task>       currentTask = new MutableLiveData<>();
    private TaskRepository              taskRepository;
    private MutableLiveData<String>     queryTrigger = new MutableLiveData<>();
    private final LiveData<List<Task>>  tasks = Transformations.switchMap(queryTrigger,
            code -> {
                switch (code) {
                    case ACTIVE:
                        swipeEnabled.setValue(true);
                        title.postValue(getApplication().getString(R.string.title_active));
                        return taskRepository.getActive();
                    case INCOMPLETE:
                        swipeEnabled.setValue(true);
                        title.postValue(getApplication().getString(R.string.title_incomplete));
                        return taskRepository.getTasks(DbContract.ToDoEntry.NOT_ARCHIVED_CODE,
                                DbContract.ToDoEntry.INCOMPLETE_CODE);
                    case COMPLETE:
                        swipeEnabled.setValue(true);
                        title.postValue(getApplication().getString(R.string.title_complete));
                        return taskRepository.getTasks(DbContract.ToDoEntry.NOT_ARCHIVED_CODE,
                                DbContract.ToDoEntry.COMPLETE_CODE);
                    case CANCEL:
                        swipeEnabled.setValue(false);
                        title.postValue(getApplication().getString(R.string.title_cancelled));
                        return taskRepository.getTasks(DbContract.ToDoEntry.NOT_ARCHIVED_CODE,
                                DbContract.ToDoEntry.CANCEL_CODE);
                    case ARCHIVED:
                        swipeEnabled.setValue(false);
                        title.postValue(getApplication().getString(R.string.title_archived));
                        return taskRepository.getArchived();
                }
                return taskRepository.getActive();
            });

    public TaskViewModel(@NonNull Application application) {
        super(application);

        taskRepository = new TaskRepository(application);
        queryTrigger.setValue(ACTIVE);
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void queryArchived() {
        queryTrigger.setValue(ARCHIVED);
    }

    public void queryActive() {
        queryTrigger.setValue(ACTIVE);
    }

    public void queryIncomplete() {
        queryTrigger.setValue(INCOMPLETE);
    }

    public void queryComplete() {
        queryTrigger.setValue(COMPLETE);
    }

    public void queryCancelled() {
        queryTrigger.setValue(CANCEL);
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

    public LiveData<Boolean> isSwipeEnabled() {
        return swipeEnabled;
    }

    public LiveData<Task> getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task task) {
        currentTask.setValue(task);
    }

    public LiveData<String> getTitle() {
        return title;
    }
}
