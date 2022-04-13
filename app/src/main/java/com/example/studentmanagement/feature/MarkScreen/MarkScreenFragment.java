package com.example.studentmanagement.feature.MarkScreen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.databinding.FragmentMarkGreenBinding;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MarkScreenFragment extends Fragment {
    private FragmentMarkGreenBinding binding;
    private MarkViewModel markViewModel;
    private AutoCompleteTextView editTextGradeName,editTextSubjectName;
    private ArrayAdapter<Grade> adapterGrade;
    private ArrayAdapter<Subject> adapterSubject;
    private List<Grade> dropdownItemsGrade= new ArrayList<>();
    private List<Subject> dropdownItemsSubject=new ArrayList<>();
    private OmegaRecyclerView recyclerView;
    private MarkListAdapter markListAdapter;
    private TextView txtListEmpty;
    private ImageButton btnBack;

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
        btnBack = binding.btnBackMarkScreen;


        markListAdapter = new MarkListAdapter( markViewModel, new MarkListAdapter.MarkDiff());

        recyclerView = binding.recyclerViewStudentMarkScreen;
        recyclerView.setAdapter(markListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        txtListEmpty = binding.txtListEmptyMarkScreen;
        initialMarkScreen();
        editTextGradeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Clicked", "Position: " + position + " ID: " + id);
                if(checkGradeAndSubject()){
                    loadRecyclerViewStudent(dropdownItemsGrade.get(position).getGradeId()
                            , editTextSubjectName.getText().toString().split("-")[0].trim());
                }
            }
        });

        editTextSubjectName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Clicked", "Position: " + position + " ID: " + id);
                //
                if(checkGradeAndSubject()){
                    loadRecyclerViewStudent(editTextGradeName.getText().toString().split("-")[0].trim()
                            , dropdownItemsSubject.get(position).getId());
                }

            }
        });

        btnBack.setOnClickListener(v->
        {
            NavDirections action = MarkScreenFragmentDirections.actionMarkScreenFragmentToHomeFragment();
            Navigation.findNavController(v).navigate(action);
        });

    }
    private boolean checkGradeAndSubject(){
        if(dropdownItemsGrade.size()!=0 && dropdownItemsSubject.size()!=0)
            return true;
            else return false;
    }

    private void loadRecyclerViewStudent(String gradeId, String subjectId) {

        List<Mark> marks = markViewModel.getMarks(gradeId, subjectId);
        if(marks.size()>0) {
            markListAdapter.submitList(markViewModel.getMarks(gradeId, subjectId));
            txtListEmpty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else {
            txtListEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
//        markViewModel.getMarkByStudentAndSubject(gradeId, subjectId)
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(
//                marks -> {
//                    markListAdapter.submitList(marks);
//                    if(marks.size()!=0) txtListEmpty.setVisibility(View.INVISIBLE);
//                    else txtListEmpty.setVisibility(View.VISIBLE);
//                },
//                throwable -> Log.d("MarkFragment", throwable.getLocalizedMessage())
//        );

    }


    private void initialDropdownGrade(List<Grade> gradeName){
        dropdownItemsGrade.addAll(gradeName);

        adapterGrade = new ArrayAdapter<Grade>(requireContext(), R.layout.dropdown_item, dropdownItemsGrade);
        if(dropdownItemsGrade.size()!=0) {
            editTextGradeName.setText(dropdownItemsGrade.get(0).toString());
        }
        editTextGradeName.setAdapter(adapterGrade);

    }

    private void initialDropdownSubject(List<Subject> subjects){
        dropdownItemsSubject.addAll(subjects);

        adapterSubject = new ArrayAdapter<Subject>(requireContext(), R.layout.dropdown_item, dropdownItemsSubject);

        if(dropdownItemsSubject.size()!=0) {
            editTextSubjectName.setText(dropdownItemsSubject.get(0).toString());
        }

        editTextSubjectName.setAdapter(adapterSubject);


    }

    private void initialMarkScreen(){

        initialDropdownGrade(markViewModel.getGrades());
        initialDropdownSubject(markViewModel.getSubjects());
        if(dropdownItemsGrade.size()!=0 && dropdownItemsSubject.size()!=0){
            loadRecyclerViewStudent(dropdownItemsGrade.get(0).getGradeId(), dropdownItemsSubject.get(0).getId());}
    }


}
