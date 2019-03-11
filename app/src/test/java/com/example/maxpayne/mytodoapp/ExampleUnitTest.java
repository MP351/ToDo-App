package com.example.maxpayne.mytodoapp;

import com.example.maxpayne.mytodoapp.db.dao.Task;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void equalTest() {
        Integer id = 1;
        String task = "task";
        Long addDate = 123L;
        Long endDate = 154L;
        Integer complete = 1;
        String description  = "description";
        Integer archived  = 1;
        Task task1 = new Task(id, task, addDate, endDate, complete, description, archived);
        Task task2 = new Task(id, task, addDate, endDate, complete, description, archived);

        assertTrue(task1.equals(task2));
    }
}