package com.example.maxpayne.mytodoapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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

        binding.detailTvAddDate.setText(
                getDateString(getString(R.string.detail_dial_add_date), task.add_date));
        binding.detailTvEndDate.setText(makeEndDateText());
        binding.detailTvEndDate.setMovementMethod(LinkMovementMethod.getInstance());


        return binding.getRoot();
    }

    private SpannableString makeEndDateText() {
        String completeTask = getString(R.string.complete_task);
        if (task.end_date == null) {
            if (task.complete == DbContract.ToDoEntry.CANCEL_CODE) {
                return new SpannableString(getString(R.string.task_cancelled));
            } else {
                SpannableString complete = new SpannableString(completeTask);
                complete.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        onCloseTaskClick();
                    }
                }, 0, completeTask.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return complete;
            }
        } else {
            return getDateString(getString(R.string.detail_dial_end_date), task.end_date);
        }
    }

    private SpannableString getDateString(String prefix, long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yy", Locale.getDefault());
        String sb = prefix + "\n" + sdf.format(new Date(timestamp));
        return new SpannableString(sb);
    }

    private void onCloseTaskClick() {
        if (task.end_date != null)
            return;
        if (task.complete == DbContract.ToDoEntry.CANCEL_CODE)
            return;

        Task closedTask = new Task(task);
        closedTask.complete = DbContract.ToDoEntry.COMPLETE_CODE;
        closedTask.end_date = System.currentTimeMillis();
        tvm.updateTask(closedTask);
        getFragmentManager().popBackStack();
    }
}
