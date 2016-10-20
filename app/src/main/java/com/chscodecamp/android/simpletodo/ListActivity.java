package com.chscodecamp.android.simpletodo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ListActivity extends AppCompatActivity implements TaskRecyclerAdapter.Callback {

    private static final String SAVED_TASKS = "savedTasks";
    private RecyclerView recyclerView;
    private EditText addItemEditText;
    private TaskRecyclerAdapter taskRecyclerAdapter;
    private List<Task> tasks = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        sharedPreferences = getApplication().getSharedPreferences(getApplication().getPackageName(), Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        addItemEditText = (EditText) findViewById(R.id.add_item_edit_text);
        ImageButton addItem = (ImageButton) findViewById(R.id.add_item);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = addItemEditText.getText().toString();
                if (title.length() > 0) {
                    addTask(new Task(title));
                    taskRecyclerAdapter.notifyItemInserted(taskRecyclerAdapter.getItemCount() - 1);
                    addItemEditText.setText("");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        tasks = loadTasks();

        if (tasks.isEmpty()) {
            // Create a default task so the list isn't empty on first use.
            addTask(new Task("Start Learning Android!"));
        }

        if (taskRecyclerAdapter == null) {
            taskRecyclerAdapter = new TaskRecyclerAdapter(tasks, this);
            recyclerView.setAdapter(taskRecyclerAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private void addTask(Task task) {
        tasks.add(task);
        saveTasks(tasks);
    }

    public void saveTasks(@NonNull List<Task> taskList) {
        sharedPreferences.edit().putString(SAVED_TASKS, new Gson().toJson(taskList)).apply();
    }

    @NonNull
    public List<Task> loadTasks() {
        Task[] savedTasks = new Gson().fromJson(sharedPreferences.getString(SAVED_TASKS, null), Task[].class);
        if (savedTasks != null && savedTasks.length > 0) {
            return new ArrayList<>(Arrays.asList(savedTasks));
        } else return new ArrayList<>();
    }

    @Override
    public void onTaskUpdated() {
        saveTasks(tasks);
    }
}
