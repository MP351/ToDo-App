package com.example.maxpayne.mytodoapp.recycler_view;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.maxpayne.mytodoapp.databinding.TaskItemBinding;
import com.example.maxpayne.mytodoapp.ui.DetailTaskDialog;
import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.db.dao.Task;

import java.util.List;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> implements ItemTouchHelperAdapter {
    private AsyncListDiffer<Task> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);
    private FragmentManager fm;
    private dbWorkListener mDbWorkListener;
    private Activity activity;

    public final int ACTION_CODE_CANCEL = 0;
    public final int ACTION_CODE_TO_ARCHIVE = 1;

    public ListRecyclerViewAdapter(Activity activity, FragmentManager fm) {
        this.fm = fm;

        this.activity = activity;
        try {
            mDbWorkListener = (dbWorkListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TaskItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.task_item,
                viewGroup,
                false
        );

        binding.setOnClickHandler((TaskItemClickListener) activity);
        return new RecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
        Task task = mDiffer.getCurrentList().get(i);

        recyclerViewHolder.bindView(task);
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public void setData(List<Task> data) {
        mDiffer.submitList(data);
    }

    @Override
    public void onLeftSwipe(int position) {
        Task task = new Task(mDiffer.getCurrentList().get(position));

        if (task.archived == DbContract.ToDoEntry.NOT_ARCHIVED_CODE) {
            switch (task.complete) {
                case DbContract.ToDoEntry.INCOMPLETE_CODE:
                    task.complete = DbContract.ToDoEntry.CANCEL_CODE;
                    mDbWorkListener.archiveOrCancelTask(task, ACTION_CODE_CANCEL);
                    break;
                case DbContract.ToDoEntry.COMPLETE_CODE:
                    task.archived = DbContract.ToDoEntry.ARCHIVED_CODE;
                    mDbWorkListener.archiveOrCancelTask(task, ACTION_CODE_TO_ARCHIVE);
                    break;
            }
        }
    }


    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK
            = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task task, @NonNull Task t1) {
            return task._id.equals(t1._id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task task, @NonNull Task t1) {
            return task.equals(t1);
        }
    };

    public interface dbWorkListener {
        void deleteTask(Task task);
        void updateTask(Task task);
        void archiveOrCancelTask(Task task, int CODE);
    }
}
