package com.example.maxpayne.mytodoapp.recycler_view;

import androidx.recyclerview.widget.RecyclerView;

import com.example.maxpayne.mytodoapp.databinding.TaskItemBinding;
import com.example.maxpayne.mytodoapp.databinding.TaskItemNewBinding;
import com.example.maxpayne.mytodoapp.db.dao.Task;

class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TaskItemNewBinding binding;

        RecyclerViewHolder(TaskItemNewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void bindView(Task task) {
            binding.setTask(task);
            binding.executePendingBindings();
        }
}
