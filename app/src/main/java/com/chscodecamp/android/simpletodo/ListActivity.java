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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ListActivity extends AppCompatActivity implements TaskRecyclerAdapter.Callback {

    private final Object syncTasks = new Object();
    private RecyclerView recyclerView;
    private EditText addItemEditText;
    private TaskRecyclerAdapter taskRecyclerAdapter;
    private List<Task> tasks = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gson = new Gson();
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
        sharedPreferences = getApplication().getSharedPreferences(getApplication().getPackageName(), Context.MODE_PRIVATE);

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
        synchronized (syncTasks) {
            for (int i = 0; i < taskList.size(); i++) {
                sharedPreferences.edit().putString(String.format(Locale.ENGLISH, "%d", i), gson.toJson(taskList.get(i))).apply();
            }
        }
    }

    @NonNull
    public List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        synchronized (syncTasks) {
            Map<String, ?> keys = sharedPreferences.getAll();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                tasks.add(gson.fromJson((String) entry.getValue(), Task.class));
            }
        }
        return tasks;
    }

    @Override
    public void onTaskUpdated() {
        saveTasks(tasks);
    }
}
