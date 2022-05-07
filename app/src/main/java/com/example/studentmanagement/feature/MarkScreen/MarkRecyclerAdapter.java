package com.example.studentmanagement.feature.MarkScreen;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.MarkDTO;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.databinding.DialogEnterMarkBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;
import com.squareup.picasso.Picasso;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

public class MarkRecyclerAdapter extends ListAdapter<MarkDTO, MarkRecyclerAdapter.MarkViewHolder> {

    private MarkViewModel markViewModel;

    protected MarkRecyclerAdapter(MarkViewModel markViewModel, @NonNull DiffUtil.ItemCallback<MarkDTO> diffCallback) {
        super(diffCallback);
        this.markViewModel = markViewModel;
    }

    @NonNull
    @Override
    public MarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MarkViewHolder(
                parent,
                R.layout.student_mark_item,
                R.layout.item_swipe_left_menu,
                markViewModel,
                this
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MarkViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class MarkViewHolder extends SwipeViewHolder implements View.OnClickListener {


        private TextView txtName;
        private TextView txtGenre;
        private TextView txtBirthdate;
        private TextView txtEdit;
        private TextView btnMark;
        private TextView txtMark;
        private MarkDTO markDTO;
        private MarkViewModel markViewModel;
        private MarkRecyclerAdapter adapter;
        private ImageView imageView;

        public MarkViewHolder(ViewGroup parent, int contentRes, int swipeLeftMenuRes,
                              MarkViewModel markViewModel, MarkRecyclerAdapter adapter) {
            super(parent, contentRes, swipeLeftMenuRes);
            txtName = findViewById(R.id.txt_name_mark_item);
            txtGenre = findViewById(R.id.txt_genre_mark_item);
            txtBirthdate = findViewById(R.id.txt_birth_day_mark_item);
            txtMark = findViewById(R.id.txt_mark_mark_item);
            // setting swipe menu
            txtEdit = findViewById(R.id.txtEdit);
            txtEdit.setVisibility(View.GONE);
            btnMark = findViewById(R.id.txtDel);
            btnMark.setText("Nhập điểm");
            btnMark.setOnClickListener(this);
            imageView = findViewById(R.id.image_mark_item);

            this.markDTO = markDTO;
            this.markViewModel = markViewModel;
            this.adapter = adapter;
        }

        public MarkViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.txtDel) {
                Toast.makeText(getContext(), "click nhap diem", Toast.LENGTH_SHORT).show();
                showEditMarkOfStudentDialog(getContext());
            }
        }

        public void bind(MarkDTO markDTO) {
            this.markDTO = markDTO;
            txtName.setText(markDTO.getFirstName() + " " + markDTO.getLastName());
            txtGenre.setText(markDTO.getGender());
            txtBirthdate.setText(markDTO.getBirthday());
            txtMark.setText(Double.toString(markDTO.getMark()));
            if(markDTO.getImage().equals("")){// no image
                imageView.setImageResource(R.drawable.avatar_just_smile);
            }else{
                Picasso.get().load(Uri.parse(markDTO.getImage()))
                        .placeholder(R.drawable.no_image).into(imageView);
            }
        }

        private void showEditMarkOfStudentDialog(Context context) {
            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogEnterMarkBinding binding = DialogEnterMarkBinding.inflate(
                    LayoutInflater.from(context)
            );
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);

            binding.editTextStudentGradeMark.setText(markDTO.getGradeId());
            binding.editTextStudentNameMark.setText(markDTO.getLastName() + " " + markDTO.getFirstName());
            binding.editTextStudentIdMark.setText(markDTO.getStudentId() + "");
            binding.editTextSubjectIdMark.setText(markDTO.getSubjectId());
            binding.editTextStudentMarkMark.setText(markDTO.getMark() + "");


            Button btnEnterMark =  dialog.findViewById(R.id.btn_enter_mark);
            btnEnterMark.setOnClickListener(v -> {
                double score = 0.0;
                try{
                    score = Double.parseDouble(
                            binding.editTextStudentMarkMark.getText().toString());
                    if(score <0 || score>10){
                        binding.textInputMarkUpdateMark.setError("Điểm nẳm trong khoảng 0->10");
                        binding.textInputMarkUpdateMark.requestFocus();
                        return;
                    }else{
                        binding.textInputMarkUpdateMark.setErrorEnabled(false);
                    }
                }catch (Exception e){

                    binding.textInputMarkUpdateMark.setError("Điểm phải là một số");
                    binding.textInputMarkUpdateMark.requestFocus();
                    return;
                }
                if(markViewModel.updateMark(new Mark(markDTO.getStudentId(),markDTO.getSubjectId(), score))){
                    AppUtils.showSuccessDialog(
                            context
                            , "Cập nhật điểm thành công!"
                    );
                    adapter.submitList(markViewModel.getMarkDTOByGradeIdAndSubjectId(markDTO.getGradeId(),markDTO.getSubjectId()));
                }
                dialog.dismiss();
            });

            binding.btnCancelMark.setOnClickListener(v -> dialog.dismiss());
            dialog.show();

        }
    }

    static class MarkDtoDiff extends DiffUtil.ItemCallback<MarkDTO> {


        @Override
        public boolean areItemsTheSame(@NonNull MarkDTO oldItem, @NonNull MarkDTO newItem) {
            return oldItem.getStudentId()==newItem.getStudentId() &&
                    oldItem.getSubjectId()==newItem.getSubjectId();
        }
        @Override
        public boolean areContentsTheSame(@NonNull MarkDTO oldItem, @NonNull MarkDTO newItem) {
            return oldItem.equals(newItem);
        }
    }


}
