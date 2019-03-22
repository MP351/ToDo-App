package com.example.maxpayne.mytodoapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.databinding.TaskDetailBinding;
import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.db.dao.Task;
import com.example.maxpayne.mytodoapp.recycler_view.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class DetailTaskFragment extends Fragment {
    private Task task;
    private TaskViewModel tvm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() == null)
            return;


        tvm = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
        this.task = tvm.getCurrentTask().getValue();
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

    public String convertDate(long timestamp) {
        SimpleDateFormat sdt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdt.format(new Date(timestamp));
    }

    public void onCloseTaskClick() {
        if (task.end_date != null)
            return;

        Task closedTask = new Task(task);
        closedTask.complete = DbContract.ToDoEntry.COMPLETE_CODE;
        closedTask.end_date = System.currentTimeMillis();
        tvm.updateTask(closedTask);
        getFragmentManager().popBackStack();
    }
}
