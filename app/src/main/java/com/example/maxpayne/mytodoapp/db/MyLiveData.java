package com.example.maxpayne.mytodoapp.db;

import com.example.maxpayne.mytodoapp.db.dao.Task;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class MyLiveData extends MutableLiveData {
    private MutableLiveData<List<Task>> data;
    private List<Task> listData;

    public MyLiveData() {
        this.data = new MutableLiveData<>();
        queryActive();
    }

    public void queryActive() {
        //data.postValue();
    }

    public void queryIncomplete() {
        //data.postValue();
    }

    public LiveData<List<Task>> getData() {
        return data;
    }
}
