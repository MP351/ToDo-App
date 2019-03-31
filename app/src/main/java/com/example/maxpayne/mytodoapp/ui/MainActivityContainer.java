package com.example.maxpayne.mytodoapp.ui;

import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProviders;

public class MainActivityContainer extends AppCompatActivity
        implements TaskItemClickListener, AddDialog.NoticeDialogListener, FragmentManager.OnBackStackChangedListener {
    Toolbar tb;
    DrawerLayout dl;
    FloatingActionButton fab;
    NavigationView nv;
    TaskViewModel tvm;
    ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tb = findViewById(R.id.RvTb);
        fab = findViewById(R.id.RvFab);
        nv = findViewById(R.id.nv);
        dl = findViewById(R.id.drawer_layout);

        tvm = ViewModelProviders.of(this).get(TaskViewModel.class);

        nv.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            query(menuItem);
            dl.closeDrawer(GravityCompat.START);
            return true;
        });

        setSupportActionBar(tb);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        tvm.getTitle().observe(this, title -> actionBar.setTitle(title));

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

        getSupportFragmentManager().addOnBackStackChangedListener(this);
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
        tvm.setCurrentTask(task);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.container, dtf)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDialogPositiveClick(String taskName, String description, long deadline) {
        tvm.addTask(new Task(taskName, description, deadline));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() < 1)
                    dl.openDrawer(GravityCompat.START);
                else
                    getSupportFragmentManager().popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        } else {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
    }
}
