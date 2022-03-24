package com.example.studentmanagement.feature.SubjectScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.databinding.ItemSwipeLeftMenuBinding;
import com.example.studentmanagement.databinding.SubjectItemBinding;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

public class SubjectListAdapter extends ListAdapter<Subject, SubjectListAdapter.SubjectViewHolder> {
    protected SubjectListAdapter(@NonNull DiffUtil.ItemCallback<Subject> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new SubjectViewHolder(
                parent,
                SubjectItemBinding.inflate(
                        LayoutInflater.from(parent.getContext())
                ),
                ItemSwipeLeftMenuBinding.inflate(
                        LayoutInflater.from(parent.getContext())
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class SubjectViewHolder extends SwipeViewHolder implements View.OnClickListener {

        private TextView txtEdit;
        private TextView txtDel;
        private TextView txtSubjectId;
        private TextView txtSubjectName;
        private TextView txtCoefficient;

        public SubjectViewHolder(
                ViewGroup parent,
                SubjectItemBinding subjectItemBinding,
                ItemSwipeLeftMenuBinding itemSwipeLeftMenuBinding) {
            super(parent, R.layout.subject_item, R.layout.item_swipe_left_menu);

            txtSubjectId = findViewById(R.id.txt_subject_id);
            txtSubjectName = findViewById(R.id.txt_subject_name);
            txtCoefficient = findViewById(R.id.txt_subject_coefficient);
            txtEdit = findViewById(R.id.txtEdit);
            txtDel = findViewById(R.id.txtDel);

            txtEdit.setOnClickListener(this);
            txtDel.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(view.getId()== R.id.txtEdit){
                txtEdit.setText("edit subject");
            }else if(view.getId()== R.id.txtDel){
                showToast("Delete subject clicked ");
            }

        }

        private void showToast(String message){
            Toast.makeText(itemView.getContext(),message, Toast.LENGTH_SHORT).show();
        }



        public void bind(Subject item) {
            txtSubjectId.setText(getString(R.string.id_of_the_subject, item.getSubjectId()));
            txtSubjectName.setText(getString(R.string.name_of_the_subject, item.getSubjectName()));
            txtCoefficient.setText(getString(R.string.coefficient_of_the_subject, item.getCoefficient()));
        }
    }

    public static class SubjectDiff extends  DiffUtil.ItemCallback<Subject>{

        @Override
        public boolean areItemsTheSame(@NonNull Subject oldItem, @NonNull Subject newItem) {
            return oldItem.getSubjectId() == newItem.getSubjectId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Subject oldItem, @NonNull Subject newItem) {
            return oldItem.getSubjectName().equals(newItem.getSubjectName())
                    && oldItem.getCoefficient()==(newItem.getCoefficient());
        }
    }
}
