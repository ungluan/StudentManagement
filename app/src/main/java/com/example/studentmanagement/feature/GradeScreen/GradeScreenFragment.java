package com.example.studentmanagement.feature.GradeScreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentmanagement.databinding.FragmentGradeScreenBinding;
import com.example.studentmanagement.utils.ItemMargin;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;


public class GradeScreenFragment extends Fragment {
    private FragmentGradeScreenBinding binding;
    private OmegaRecyclerView recyclerView;
    private GradeViewModel gradeViewModel;


    public GradeScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGradeScreenBinding.inflate(inflater);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gradeViewModel = new
                ViewModelProvider(requireActivity()).get(GradeViewModel.class);

        recyclerView = binding.recyclerViewGrade;

        // Set data to recycler view
        GradeListAdapter adapter = new GradeListAdapter(new GradeListAdapter.GradeDiff());
        recyclerView.setAdapter(adapter);


        // Add margin to recycler view item
        recyclerView.addItemDecoration(
                new ItemMargin(16, 0, 0, 16));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        gradeViewModel.getAllGrade().observe(this.getViewLifecycleOwner(), grades -> {
            adapter.submitList(grades);
            recyclerView.setAdapter(adapter);
        });
    }
}