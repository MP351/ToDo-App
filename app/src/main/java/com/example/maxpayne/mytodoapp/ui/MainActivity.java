package com.example.maxpayne.mytodoapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;

import com.example.maxpayne.mytodoapp.recycler_view.ItemTouchHelperCallback;
import com.example.maxpayne.mytodoapp.recycler_view.ListRecyclerViewAdapter;
import com.example.maxpayne.mytodoapp.recycler_view.TaskViewModel;
import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.db.dao.Task;

public class MainActivity extends AppCompatActivity implements AddDialog.NoticeDialogListener,
        DetailTaskDialog.NoticeDialogListener, ListRecyclerViewAdapter.dbWorkListener {
    RecyclerView rv;
    ListRecyclerViewAdapter lrva;
    FloatingActionButton fab;
    Toolbar myTb;
    TaskViewModel tvm;
    ItemTouchHelperCallback ithc;
    ConstraintLayout cl;
    AppCompatActivity activity = this;
    NavigationView nv;
    DrawerLayout dl;
    Integer lastActiveAction = R.id.nav_active;
    final String LAST_ACTION = "ACTION___";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rv);
        initViews();

        tvm = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(TaskViewModel.class);

        fab.setOnClickListener((view -> openAddingDialog()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        query(lastActiveAction);
        tvm.getTasks().observe(this, (tasks) -> lrva.setData(tasks));
    }

    public void openAddingDialog() {
        AddDialog addDialog = new AddDialog();
        addDialog.show(getSupportFragmentManager(), "AddDiag");
    }

    private void initViews() {
        rv = findViewById(R.id.rvList);
        myTb = findViewById(R.id.RvTb);
        fab = findViewById(R.id.RvFab);
        cl = findViewById(R.id.cl_main);
        nv = findViewById(R.id.nv);
        dl = findViewById(R.id.drawer_layout);

        setSupportActionBar(myTb);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        nv.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            query(menuItem.getItemId());
            dl.closeDrawers();
            return true;
        });

        ithc = new ItemTouchHelperCallback(lrva);
        ItemTouchHelper touchHelper = new ItemTouchHelper(ithc);
        touchHelper.attachToRecyclerView(rv);

        lrva = new ListRecyclerViewAdapter(this, getSupportFragmentManager());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(lrva);
    }

    private void query(Integer menuItem) {
        tvm.getTasks().removeObservers(activity);
        switch (menuItem) {
            //Active
            case R.id.nav_active:
                ithc.setSwipeEnabled(true);
                tvm.queryActive();
                myTb.setTitle(R.string.title_active);
                break;
            //Incomplete
            case R.id.nav_incomplete:
                ithc.setSwipeEnabled(true);
                tvm.queryIncomplete();
                myTb.setTitle(R.string.title_incomplete);
                break;
            //Complete
            case R.id.nav_complete:
                ithc.setSwipeEnabled(true);
                tvm.queryComplete();
                myTb.setTitle(R.string.title_complete);
                break;
            //Cancelled
            case R.id.nav_cancelled:
                ithc.setSwipeEnabled(false);
                tvm.queryCancelled();
                myTb.setTitle(R.string.title_cancelled);
                break;
            //Archived
            case R.id.nav_archive:
                ithc.setSwipeEnabled(false);
                tvm.queryArchived();
                myTb.setTitle(R.string.title_active);
                break;
        }

        tvm.getTasks().observe(activity, (tasks) -> lrva.setData(tasks));
    }

    @Override
    public void onDialogPositiveClick(String taskName, String description) {
        tvm.addTask(new Task(taskName, description));
    }

    @Override
    public void closeTask(Task task) {
        Task t1 = new Task(task);
        t1.end_date = System.currentTimeMillis();
        t1.complete = DbContract.ToDoEntry.COMPLETE_CODE;
        tvm.updateTask(t1);
    }

    @Override
    public void deleteTask(Task task) {
        tvm.deleteTask(task);
    }

    @Override
    public void updateTask(Task task) {
        tvm.updateTask(task);
    }

    @Override
    public void archiveOrCancelTask(Task task, int code) {
        StringBuilder sb = new StringBuilder("Task ");
        Snackbar snb;
        if (code == lrva.ACTION_CODE_CANCEL) {
            snb = Snackbar.make(cl, sb.append(" cancelled.").toString(), Snackbar.LENGTH_SHORT);
            snb.setDuration(3000);
            updateTask(task);
        } else {
            snb = Snackbar.make(cl, sb.append(" archived.").toString(), Snackbar.LENGTH_SHORT);
            snb.setDuration(3000);
            updateTask(task);
        }


        snb.setAction("UNDO", (view -> {
            if (code == lrva.ACTION_CODE_CANCEL) {
                task.complete = DbContract.ToDoEntry.INCOMPLETE_CODE;
                updateTask(task);
            } else {
                task.archived = DbContract.ToDoEntry.NOT_ARCHIVED_CODE;
                updateTask(task);
            }
        }));
        snb.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(LAST_ACTION, lastActiveAction);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        lastActiveAction = savedInstanceState.getInt(LAST_ACTION);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dl.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
