package com.example.maxpayne.mytodoapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.databinding.DetailTaskBinding;
import com.example.maxpayne.mytodoapp.db.dao.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailTaskDialog extends DialogFragment {
    private Task task;
    private DetailTaskBinding binding;
    private NoticeDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                getActivity().getLayoutInflater(),
                R.layout.detail_task,
                null,
                false);
        binding.setTask(task);
        binding.setDialog(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.detail_head)
                .setView(binding.getRoot())
                .setNeutralButton(R.string.close, (dialogInterface, i) -> dismiss());
        Log.d("TEST_DIALOG", "onCreateDialog");
        return builder.create();
    }

    public void setTask(Task task) {
        this.task = task;
        Log.d("TEST_DIALOG", "setTask");
    }

    public String convertDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        Log.d("TEST_DIALOG", "convert");
        return sdf.format(new Date(timestamp));
    }

    @Override
    public void onAttach(Context context) {
        try {
            mListener = (NoticeDialogListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        super.onAttach(context);
    }

    public void closeAndDismiss(Task task) {
        mListener.closeTask(task);
        dismiss();
    }

    public interface NoticeDialogListener {
        void closeTask(Task task);
    }
}
