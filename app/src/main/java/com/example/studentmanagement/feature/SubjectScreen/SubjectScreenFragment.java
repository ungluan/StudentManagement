package com.example.studentmanagement.feature.SubjectScreen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.databinding.DialogAddSubjectBinding;
import com.example.studentmanagement.databinding.FragmentSubjectScreenBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.example.studentmanagement.utils.ItemMargin;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

public class SubjectScreenFragment extends Fragment {
    private FragmentSubjectScreenBinding binding;
    private OmegaRecyclerView recyclerView;
    private SubjectViewModel subjectViewModel;
    private  SubjectListAdapter adapter;
    public SubjectScreenFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubjectScreenBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subjectViewModel = new
                ViewModelProvider(requireActivity()).get(com.example.studentmanagement.feature.SubjectScreen.SubjectViewModel.class);

        recyclerView = binding.recyclerViewSubject;

        //set data to recycler view
         adapter = new
                SubjectListAdapter(subjectViewModel, new SubjectListAdapter.SubjectDiff());
        recyclerView.setAdapter(adapter);

        // add margin to recyccler view item

        recyclerView.addItemDecoration(new ItemMargin(
                16, 0, 0, 16
        ));
        adapter.submitList(subjectViewModel.getAllSubject());
        recyclerView.setAdapter(adapter);
        binding.btnAddSubject.setOnClickListener(v -> showDialogAddSubject(requireContext()));
        binding.btnBackSubjectScreen.setOnClickListener(v->{
            NavDirections action = SubjectScreenFragmentDirections.actionSubjectScreenFragmentToHomeFragment();
            Navigation.findNavController(v).navigate(action);
        });


    }

    private void showDialogAddSubject(Context context) {

        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogAddSubjectBinding binding = DialogAddSubjectBinding.inflate(
                LayoutInflater.from(context)
        );
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);

        binding.dialogTitleAddSubject.setText("Thêm môn học");
        binding.btnConfirmAddSubject.setText("LƯU");
        binding.editTextSubjectId.setEnabled(true);

        // get data

        binding.btnCancelAddSubject.setOnClickListener(v -> dialog.dismiss());
        binding.btnConfirmAddSubject.setOnClickListener(view -> {
            String id = binding.editTextSubjectId.getText().toString();
            String name = binding.editTextSubjectName.getText().toString();
            int factor = Integer.parseInt(binding.editTextSubjectCoefficient
                    .getText().toString());

            boolean success = subjectViewModel.insert(new Subject(id, name, factor));
            if (success) {
                AppUtils.showNotificationDialog(context
                        , "INSERT"
                        , "Add subject successfully!");
                dialog.dismiss();
                adapter.submitList(subjectViewModel.getAllSubject());
            } else {
                AppUtils.showNotificationDialog(context
                        , "INSERT"
                        , "Add subject failed!");
            }

        });

        dialog.show();

    }
}
