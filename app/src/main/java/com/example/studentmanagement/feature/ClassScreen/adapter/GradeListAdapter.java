package com.example.studentmanagement.feature.ClassScreen.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.databinding.GradeItemBinding;

public class GradeListAdapter extends ListAdapter<Grade, GradeListAdapter.GradeViewHolder> {
    public GradeListAdapter(@NonNull DiffUtil.ItemCallback<Grade> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GradeViewHolder(
                GradeItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

     static class GradeViewHolder extends RecyclerView.ViewHolder {
//        private final TextView textView;
        private GradeItemBinding binding;
        public GradeViewHolder(GradeItemBinding binding) {
            super(binding.getRoot());
//            textView = binding.txtClassName;
            this.binding = binding;
        }

        public void bind(Grade grade) {
//            textView.setText(grade.getGradeName());
            binding.setGrade(grade);
        }

    }

    public static class GradeDiff extends DiffUtil.ItemCallback<Grade>{

        @Override
        public boolean areItemsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
            return oldItem.equals(newItem);
        }
    }
}
