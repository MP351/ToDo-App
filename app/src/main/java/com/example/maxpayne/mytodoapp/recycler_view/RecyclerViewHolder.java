package com.example.maxpayne.mytodoapp.recycler_view;

import androidx.recyclerview.widget.RecyclerView;

import com.example.maxpayne.mytodoapp.databinding.TaskItemBinding;
import com.example.maxpayne.mytodoapp.db.dao.Task;

class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TaskItemBinding binding;

        RecyclerViewHolder(TaskItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void bindView(Task task) {
            binding.setItem(task);
            binding.executePendingBindings();
        }
}
