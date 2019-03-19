package com.example.maxpayne.mytodoapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.databinding.TaskAddBinding;
import com.example.maxpayne.mytodoapp.db.dao.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class AddTaskFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        TaskAddBinding binding = DataBindingUtil.inflate(inflater, R.layout.task_add,
                container, false);
        Task task = new Task();
        binding.setTask(task);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
