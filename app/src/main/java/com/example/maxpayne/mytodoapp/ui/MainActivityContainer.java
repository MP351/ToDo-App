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

    final String NAV_TAG = "LAST_NAV_TAG";
    final String TITLE_TAG = "LAST_TITLE_TAG";
    int lastNav;
    String title;

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

        if (savedInstanceState == null) {
            lastNav = R.id.nav_active;
            actionBar.setTitle(R.string.title_active);
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
                actionBar.setTitle(R.string.title_active);
                lastNav = R.id.nav_active;
                break;
            case R.id.nav_incomplete:
                tvm.queryIncomplete();
                actionBar.setTitle(R.string.title_incomplete);
                lastNav = R.id.nav_incomplete;
                break;
            case R.id.nav_complete:
                tvm.queryComplete();
                actionBar.setTitle(R.string.title_complete);
                lastNav = R.id.nav_complete;
                break;
            case R.id.nav_cancelled:
                tvm.queryCancelled();
                actionBar.setTitle(R.string.title_cancelled);
                lastNav = R.id.nav_cancelled;
                break;
            case R.id.nav_archive:
                tvm.queryArchived();
                actionBar.setTitle(R.string.title_archived);
                lastNav = R.id.nav_archive;
                break;
        }
    }

    @Override
    public void onItemClick(Task task) {
        DetailTaskFragment dtf = new DetailTaskFragment();
        dtf.setTask(task);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.container, dtf)
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(NAV_TAG, lastNav);
        outState.putString(TITLE_TAG, title);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        lastNav = savedInstanceState.getInt(NAV_TAG);
        title = savedInstanceState.getString(TITLE_TAG);
    }
}
