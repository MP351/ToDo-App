package com.example.maxpayne.mytodoapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.databinding.TaskDetailBinding;
import com.example.maxpayne.mytodoapp.db.dao.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class DetailTaskFragment extends Fragment {
    private Task task;

    public void setTask(Task task) {
        this.task = task;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        TaskDetailBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.task_detail,
                container,
                false);

        binding.setTask(task);
        binding.setUt(this);

        return binding.getRoot();
    }

    public String convertDate(Long timestamp) {
        SimpleDateFormat sdt = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        return sdt.format(new Date(timestamp));
    }
}
