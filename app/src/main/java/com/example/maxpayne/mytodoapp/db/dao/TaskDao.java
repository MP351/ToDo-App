package com.example.maxpayne.mytodoapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.maxpayne.mytodoapp.db.DbContract;

import java.util.List;

@Dao
public interface TaskDao {
    String GET_All_SQL = ("SELECT * FROM " + DbContract.ToDoEntry.TABLE_NAME);
    String GET_ACTIVE_SQL = ("SELECT * FROM " + DbContract.ToDoEntry.TABLE_NAME + " WHERE " +
            DbContract.ToDoEntry.COLUMN_NAME_COMPLETE + " <> " + DbContract.ToDoEntry.CANCEL_CODE +
            " AND " +
            DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED + " <> " + DbContract.ToDoEntry.ARCHIVED_CODE +
            " ORDER BY " + DbContract.ToDoEntry.COLUMN_NAME_DEADLINE + "," +
            DbContract.ToDoEntry._ID + " ASC");

    String GET_NOT_ARCHIVED = "SELECT * FROM " + DbContract.ToDoEntry.TABLE_NAME + " WHERE " +
            DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED + " = " + DbContract.ToDoEntry.NOT_ARCHIVED_CODE +
            " AND " +
            DbContract.ToDoEntry.COLUMN_NAME_COMPLETE + " = :completeCode";

    String GET_ARCHIVED = "SELECT * FROM " + DbContract.ToDoEntry.TABLE_NAME + " WHERE " +
            DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED + " = " + DbContract.ToDoEntry.ARCHIVED_CODE +
            " ORDER BY " + DbContract.ToDoEntry.COLUMN_NAME_DEADLINE + "," + DbContract.ToDoEntry._ID +
            " ASC";

    String GET_TEMPLATE = "SELECT * FROM " + DbContract.ToDoEntry.TABLE_NAME + " WHERE " +
            DbContract.ToDoEntry.COLUMN_NAME_ARCHIVED + " = :archivedCode AND " +
            DbContract.ToDoEntry.COLUMN_NAME_COMPLETE + " = :completeCode ORDER BY " +
            DbContract.ToDoEntry.COLUMN_NAME_DEADLINE + "," + DbContract.ToDoEntry._ID + " ASC";

    @Query(GET_All_SQL)
    LiveData<List<Task>> getAll();

    @Query(GET_TEMPLATE)
    LiveData<List<Task>> getTasks(int archivedCode, int completeCode);

    @Query(GET_ACTIVE_SQL)
    LiveData<List<Task>> getActiveTasks();

    @Query(GET_ARCHIVED)
    LiveData<List<Task>> getArchived();

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);
}
