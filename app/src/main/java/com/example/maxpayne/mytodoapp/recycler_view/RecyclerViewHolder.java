package com.example.maxpayne.mytodoapp.recycler_view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.db.dao.Task;

class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber;
        CheckedTextView chtvTask;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNumber = itemView.findViewById(R.id.tv);
            chtvTask = itemView.findViewById(R.id.ctv);
        }

        void bindView(Task task) {
            tvNumber.setText(String.valueOf(task._id));
            chtvTask.setText(task.task);
            chtvTask.setChecked(task.complete == DbContract.ToDoEntry.COMPLETE_CODE);
        }
}
