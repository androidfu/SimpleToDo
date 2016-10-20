package com.chscodecamp.android.simpletodo;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private Callback callback;

    TaskRecyclerAdapter(List<Task> tasks, Callback callback) {
        this.tasks = tasks;
        this.callback = callback;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_todo_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, int position) {

        final Task task = tasks.get(position);

        holder.itemName.setText(task.getTitle());
        holder.checkBox.setChecked(task.isCompleted());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.setCompleted(holder.checkBox.isChecked());
                holder.checkBox.setChecked(task.isCompleted());
                if (callback != null) {
                    callback.onTaskUpdated();
                }
                if (task.isCompleted()) {
                    holder.itemName.setPaintFlags(holder.itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    interface Callback {
        void onTaskUpdated();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private CheckBox checkBox;

        TaskViewHolder(View v) {
            super(v);
            itemName = (TextView) v.findViewById(R.id.item_name);
            checkBox = (CheckBox) v.findViewById(R.id.item_checkbox);
        }
    }
}