package com.example.maxpayne.mytodoapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.db.dao.Task;
import com.example.maxpayne.mytodoapp.recycler_view.ItemTouchHelperCallback;
import com.example.maxpayne.mytodoapp.recycler_view.ListRecyclerViewAdapter;
import com.example.maxpayne.mytodoapp.recycler_view.TaskItemClickListener;
import com.example.maxpayne.mytodoapp.recycler_view.TaskViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TaskListFragment extends Fragment implements ListRecyclerViewAdapter.dbWorkListener {
    private RecyclerView rv;
    private ItemTouchHelperCallback ithc;
    private ListRecyclerViewAdapter adapter;
    private TaskViewModel tvm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null)
            return;

        tvm = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
        adapter = new ListRecyclerViewAdapter(this, (TaskItemClickListener) getActivity());


        tvm.getTasks().observe(getActivity(),
                tasks -> adapter.setData(tasks));
        ithc = new ItemTouchHelperCallback(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getActivity() == null)
            return null;

        rv = (RecyclerView) inflater.inflate(R.layout.rv_layout, container, false);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        ItemTouchHelper th = new ItemTouchHelper(ithc);
        th.attachToRecyclerView(rv);

        tvm.isSwipeEnabled().observe(getActivity(),
                bool -> ithc.setSwipeEnabled(bool));

        return rv;
    }
    /*
    @Override
    public void deleteTask(Task task) {
        tvm.deleteTask(task);
    }*/

    @Override
    public void updateTask(Task task) {
        tvm.updateTask(task);
    }

    @Override
    public void archiveOrCancelTask(Task task, int code) {
        updateTask(task);
        if (code == ListRecyclerViewAdapter.dbWorkListener.CANCEL_CODE) {
            Snackbar.make(rv, getString(R.string.task_cancelled), Snackbar.LENGTH_LONG).setAction(
                    getString(R.string.undo), view -> {
                        task.complete = DbContract.ToDoEntry.INCOMPLETE_CODE;
                        updateTask(task);
                    }
            ).show();
        } else if (code == ListRecyclerViewAdapter.dbWorkListener.ARCHIVE_CODE) {
            Snackbar.make(rv, getString(R.string.task_archived), Snackbar.LENGTH_LONG).setAction(
                    getString(R.string.undo), view -> {
                        task.archived = DbContract.ToDoEntry.NOT_ARCHIVED_CODE;
                        updateTask(task);
                    }
            ).show();
        }
    }
}
