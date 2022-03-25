package com.example.studentmanagement.feature.MarkScreen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.databinding.FragmentMarkGreenBinding;
import com.example.studentmanagement.feature.StudentScreen.StudentViewModel;
import com.google.android.material.chip.ChipGroup;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MarkScreenFragment extends Fragment {
    private FragmentMarkGreenBinding binding;
    private MarkViewModel markViewModel;
    private AutoCompleteTextView editTextGradeName,editTextSubjectName;
    private ArrayAdapter<String> adapterGrade;
    private ArrayAdapter<Subject> adapterSubject;
    private List<String> dropdownItemsGrade= new ArrayList<>();
    private List<Subject> dropdownItemsSubject=new ArrayList<>();
    private OmegaRecyclerView recyclerView;
    private MarkListAdapter markListAdapter;
    private TextView txtListEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMarkGreenBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        markViewModel = new ViewModelProvider(requireActivity())
                .get(MarkViewModel.class);

        editTextGradeName = binding.editTextGradeNameMarkScreen;
        editTextSubjectName = binding.editTextSubjectNameMarkScreen;

        markListAdapter = new MarkListAdapter( markViewModel, new MarkListAdapter.MarkDiff());

        recyclerView = binding.recyclerViewStudentMarkScreen;
        recyclerView.setAdapter(markListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        txtListEmpty = binding.txtListEmptyMarkScreen;

        editTextGradeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Clicked", "Position: " + position + " ID: " + id);
                loadRecyclerViewStudent(dropdownItemsGrade.get(position), editTextSubjectName.getText().toString());
            }
        });

        editTextSubjectName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Clicked", "Position: " + position + " ID: " + id);
                //
                loadRecyclerViewStudent(editTextGradeName.getText().toString(), dropdownItemsSubject.get(position).getSubjectId());
            }
        });
        initialMarkScreen();
    }

    private void loadRecyclerViewStudent(String gradeId, String subjectId) {
        markViewModel.getMarkByStudentAndSubject(gradeId, subjectId)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                marks -> {
                    markListAdapter.submitList(marks);
                    if(marks.size()!=0) txtListEmpty.setVisibility(View.INVISIBLE);
                    else txtListEmpty.setVisibility(View.VISIBLE);
                },
                throwable -> Log.d("MarkFragment", throwable.getLocalizedMessage())
        );
    }


    private void initialDropdownGrade(List<String> gradeName){
        dropdownItemsGrade.addAll(gradeName);

        adapterGrade = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, dropdownItemsGrade);

        editTextGradeName.setText(dropdownItemsGrade.get(0));
        editTextGradeName.setAdapter(adapterGrade);
        if(dropdownItemsGrade.size()!=0 && dropdownItemsSubject.size()!=0){
            loadRecyclerViewStudent(dropdownItemsGrade.get(0), dropdownItemsSubject.get(0).getSubjectId());}

    }

    private void initialDropdownSubject(List<Subject> subjects){
        dropdownItemsSubject.addAll(subjects);

        adapterSubject = new ArrayAdapter<Subject>(requireContext(), R.layout.dropdown_item, dropdownItemsSubject);


        editTextSubjectName.setText(dropdownItemsSubject.get(0).toString());
        editTextSubjectName.setAdapter(adapterSubject);
        if(dropdownItemsGrade.size()!=0 && dropdownItemsSubject.size()!=0){
        loadRecyclerViewStudent(dropdownItemsGrade.get(0), dropdownItemsSubject.get(0).getSubjectId());}

    }

    private void initialMarkScreen(){

        markViewModel.getListGrade().subscribe(
                strings -> Observable.fromIterable(strings)
                        .map(grade -> grade.getGradeId()).toList()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                gradeNames -> {initialDropdownGrade(gradeNames);
                                    },
                                throwable -> Log.d("MarkFragment load grade name", "Error: " + throwable.getMessage())
                        ),
                throwable -> {
                    Log.d("StudentFragment", "Error: " + throwable.getMessage());
                }
        );
        markViewModel.getListSubject().subscribe(
                strings -> Observable.fromIterable(strings)
                        .map(subject -> subject).toList()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                subjectNames -> initialDropdownSubject(subjectNames),
                                throwable -> Log.d("MarkFragment load subject name", "Error: " + throwable.getMessage())
                        ),
                throwable -> {
                    Log.d("MarkFragment", "Error: " + throwable.getMessage());
                }
        );
    }
}
