package com.example.maxpayne.mytodoapp.recycler_view;

import android.app.Application;
import android.util.Log;

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
    private TaskRepository taskRepository;
    private MutableLiveData<Integer> queryTrigger = new MutableLiveData<>();
    public final LiveData<List<Task>> tasks = Transformations.switchMap(queryTrigger,
            code -> {
                switch (code) {
                    case 0:
                        return taskRepository.getActive();
                    case 1:
                        return taskRepository.getTasks(DbContract.ToDoEntry.NOT_ARCHIVED_CODE,
                                DbContract.ToDoEntry.INCOMPLETE_CODE);
                    case 2:
                        return taskRepository.getTasks(DbContract.ToDoEntry.NOT_ARCHIVED_CODE,
                                DbContract.ToDoEntry.COMPLETE_CODE);
                    case 3:
                        return taskRepository.getTasks(DbContract.ToDoEntry.NOT_ARCHIVED_CODE,
                                DbContract.ToDoEntry.CANCEL_CODE);
                    case 4:
                        return taskRepository.getArchived();
                }
                return taskRepository.getActive();
            });

    public TaskViewModel(@NonNull Application application) {
        super(application);

        taskRepository = new TaskRepository(application);
        queryTrigger.setValue(0);
        //tasks = taskRepository.getTasks();
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void queryArchived() {
        //tasks = taskRepository.getArchived();
        queryTrigger.setValue(4);
    }

    public void queryActive() {
        //tasks = taskRepository.getActive();
        queryTrigger.setValue(0);
    }

    public void queryIncomplete() {
        /*tasks = taskRepository.getTasks(DbContract.ToDoEntry.NOT_ARCHIVED_CODE,
                DbContract.ToDoEntry.INCOMPLETE_CODE);*/
        queryTrigger.setValue(1);
    }

    public void queryComplete() {
        /*tasks = taskRepository.getTasks(DbContract.ToDoEntry.NOT_ARCHIVED_CODE,
                DbContract.ToDoEntry.COMPLETE_CODE);*/
        queryTrigger.setValue(2);
    }

    public void queryCancelled() {
        /*tasks = taskRepository.getTasks(DbContract.ToDoEntry.NOT_ARCHIVED_CODE,
                DbContract.ToDoEntry.CANCEL_CODE);*/
        queryTrigger.setValue(3);
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
