package com.example.studentmanagement.feature.SubjectScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studentmanagement.databinding.FragmentSubjectScreenBinding;
import com.example.studentmanagement.utils.ItemMargin;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

public class SubjectScreenFragment extends Fragment {
    private FragmentSubjectScreenBinding binding;
    private OmegaRecyclerView recyclerView;
    private com.example.studentmanagement.feature.SubjectScreen.SubjectViewModel subjectViewModel;

    public SubjectScreenFragment(){

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
        SubjectListAdapter adapter = new
                SubjectListAdapter(new SubjectListAdapter.SubjectDiff());
        recyclerView.setAdapter(adapter);

        // add margin to recyccler view item

        recyclerView.addItemDecoration(new ItemMargin(
                16,0,0,16
        ));


                    adapter.submitList(subjectViewModel.getAllSubject());
                    recyclerView.setAdapter(adapter);


    }
}
