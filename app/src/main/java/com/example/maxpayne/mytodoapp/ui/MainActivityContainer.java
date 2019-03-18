package com.example.maxpayne.mytodoapp.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.dao.Task;
import com.example.maxpayne.mytodoapp.recycler_view.TaskItemClickListener;
import com.example.maxpayne.mytodoapp.recycler_view.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

public class MainActivityContainer extends AppCompatActivity implements TaskItemClickListener, AddDialog.NoticeDialogListener {
    Toolbar tb;
    DrawerLayout dl;
    FloatingActionButton fab;
    NavigationView nv;
    TaskViewModel tvm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rv);
        tb = findViewById(R.id.RvTb);
        fab = findViewById(R.id.RvFab);
        nv = findViewById(R.id.nv);
        dl = findViewById(R.id.drawer_layout);
        tvm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
                .create(TaskViewModel.class);

        nv.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            query(menuItem);
            dl.closeDrawer(GravityCompat.START);
            return true;
        });

        setSupportActionBar(tb);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        if (savedInstanceState == null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .add(R.id.container, new TaskListFragment())
                    .commit();
        }

        fab.setOnClickListener(view -> {
            AddDialog addDialog = new AddDialog();
            addDialog.show(getSupportFragmentManager(), "add");
        });
    }

    private void query(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_active:
                tvm.queryActive();
                break;
            case R.id.nav_incomplete:
                tvm.queryIncomplete();
                break;
            case R.id.nav_complete:
                tvm.queryComplete();
                break;
            case R.id.nav_cancelled:
                tvm.queryCancelled();
                break;
            case R.id.nav_archive:
                tvm.queryArchived();
                break;
        }
    }

    @Override
    public void onItemClick(Task task) {
        DetailTaskFragment dtf = new DetailTaskFragment();
        dtf.setTask(task);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, dtf)
                .setCustomAnimations(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDialogPositiveClick(String taskName, String description) {
        tvm.addTask(new Task(taskName, description));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dl.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
