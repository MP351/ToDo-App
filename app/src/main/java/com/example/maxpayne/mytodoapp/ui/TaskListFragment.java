package com.example.maxpayne.mytodoapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.recycler_view.ItemTouchHelperCallback;
import com.example.maxpayne.mytodoapp.recycler_view.ListRecyclerViewAdapter;
import com.example.maxpayne.mytodoapp.recycler_view.TaskViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TaskListFragment extends Fragment {
    private RecyclerView rv;
    private ItemTouchHelperCallback ithc;
    private ListRecyclerViewAdapter adapter;
    private TaskViewModel tvm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvm = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication()).create(TaskViewModel.class);
        adapter = new ListRecyclerViewAdapter(getActivity());

        tvm.getTasks().observe(this,
                tasks -> adapter.setData(tasks));

        ithc = new ItemTouchHelperCallback(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(R.layout.rv_layout, container, false);
        rv = rv.findViewById(R.id.rvTasks);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        ItemTouchHelper th = new ItemTouchHelper(ithc);
        th.attachToRecyclerView(rv);
        return rv;
    }
}
