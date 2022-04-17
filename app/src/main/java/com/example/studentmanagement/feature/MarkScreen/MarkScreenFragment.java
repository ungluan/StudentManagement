package com.example.studentmanagement.feature.MarkScreen;

import android.Manifest;
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
import android.widget.Toast;

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
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.databinding.FragmentMarkGreenBinding;
import com.example.studentmanagement.feature.pdf.Common;
import com.example.studentmanagement.feature.pdf.PdfUtil;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MarkScreenFragment extends Fragment {
    private FragmentMarkGreenBinding binding;
    private MarkViewModel markViewModel;
    private AutoCompleteTextView editTextGradeName, editTextSubjectName;
    private ArrayAdapter<String> adapterGrade;
    private ArrayAdapter<Subject> adapterSubject;
    private List<Grade> dropdownItemsGrade = new ArrayList<>();
    private List<Subject> dropdownItemsSubject = new ArrayList<>();
    private OmegaRecyclerView recyclerView;
    private MarkListAdapter markListAdapter;
    private TextView txtListEmpty;
    private ImageButton btnBack;
    private int indexGrade = -1;
    private int indexSubject = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMarkGreenBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initData();
        setControls();
        setEvents();
        markViewModel = new ViewModelProvider(requireActivity())
                .get(MarkViewModel.class);

        editTextGradeName = binding.editTextGradeNameMarkScreen;
        editTextSubjectName = binding.editTextSubjectNameMarkScreen;
        btnBack = binding.btnBackMarkScreen;


        markListAdapter = new MarkListAdapter(markViewModel, new MarkListAdapter.MarkDiff());

        recyclerView = binding.recyclerViewStudentMarkScreen;
        recyclerView.setAdapter(markListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        txtListEmpty = binding.txtListEmptyMarkScreen;
        initialMarkScreen();
        editTextGradeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Clicked", "Position: " + position + " ID: " + id);
                indexGrade = position;
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
                indexSubject = position;
                //
                if(checkGradeAndSubject()){
                    loadRecyclerViewStudent(editTextGradeName.getText().toString()
                            , dropdownItemsSubject.get(position).getId());
                }

            }
        });

        btnBack.setOnClickListener(v ->
        {
            NavDirections action = MarkScreenFragmentDirections.actionMarkScreenFragmentToHomeFragment();
            Navigation.findNavController(v).navigate(action);
        });

    }

    private void setEvents() {
        binding.btnPdfMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        showPdf(Common.getAppPath(getContext()) + "test_pdf.pdf");
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(getContext(), "Permission deny\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };
                TedPermission.create()
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }
        });

    }

    private void showPdf(String path) {
        PdfUtil pdf = new PdfUtil();

        if (new File(path).exists()) {
            new File(path).delete();
        }

        try {
            Document document = new Document();
            // save
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            document.setPageSize(PageSize.A4);
            document.setMargins(5f, 5f, 5f, 5f);
            document.addCreationDate();
            document.addAuthor("TNT_HIEN");
            document.addCreator("Hien Nguyen");

            Paragraph paragraph = new Paragraph("");
            pdf.addNewItem(document, "QUẢN LÍ HỌC SINH", Element.ALIGN_CENTER, pdf.titleFont);
//            specify column widths
            if (indexGrade == -1) {
                return;
            }
            Grade grade = dropdownItemsGrade.get(indexGrade);
            Teacher teacher = markViewModel.findTeacherById(grade.getTeacherId());
            // title pdf
            pdf.addNewItem(document, "LỚP:" + grade.getGradeId() + "           " + "GIÁO VIÊN:" +
                    teacher.getTeacherName(), Element.ALIGN_CENTER, pdf.bf12);
            pdf.addNewItem(document, "                              ", Element.ALIGN_CENTER, pdf.bf12);

           //information student
            List<Student> listStudent = markViewModel.getStudentsByGradeId(grade.getGradeId());
            int i=0;
            for (Student student : listStudent) {
                pdf.addNewItem(document, "STT: " + (++i), Element.ALIGN_CENTER, pdf.bfBold12);
                // student information
                pdf.addNewItem(document,
                        "Thông tin học sinh",
                        Element.ALIGN_CENTER,
                        pdf.bfBold12);

//                pdf.addNewItem(document,"ID:" + student.getId()+
//                                "GENDER:" + (student.getGender()),
//                        Element.ALIGN_LEFT,
//                        pdf.bf12);

                pdf.addNewItem(document,
                        String.format("%10s %-50s %-30s", "",
                                "Mã:" + student.getId(),
                                "Giới tính:" + (student.getGender())),
                        Element.ALIGN_LEFT,
                        pdf.bf12);

                pdf.addNewItem(document,
                        String.format("%10s %-37s %-30s", "",
                                "Tên:" + student.getFirstName() + " " + student.getLastName(),
                                "Ngáy sinh:" + student.getBirthday()),
                        Element.ALIGN_LEFT,
                        pdf.bf12);
                // list mark of student

                pdf.addNewItem(document,
                        "Bảng điểm các môn",
                        Element.ALIGN_CENTER,
                        pdf.bfBold12);
                pdf.addNewItem(document,
                        "                ",
                        Element.ALIGN_CENTER,
                        pdf.bfBold12);
                createTable(pdf, document, student);

                pdf.addNewItem(document,
                        "-------------------------------------------------------------",
                        Element.ALIGN_CENTER,
                        pdf.bfBold12);
            }

            //print
            document.close();
            pdf.printPDF(requireContext());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void createTable(PdfUtil pdf, Document document, Student student) {

        Paragraph paragraph = new Paragraph();
        ArrayList<Mark> listMarkOfStudent = markViewModel.getListMarkOfStudent(student.getId());
        DecimalFormat df = new DecimalFormat("0.00");
        float[] columnWidths = {1f, 3f, 2f, 2f};
        //create PDF table with the given widths
        PdfPTable table = new PdfPTable(columnWidths);
        // set table width a percentage of the page width
        table.setWidthPercentage(100f);

        //insert column headings
        pdf.insertCell(table, "STT", Element.ALIGN_CENTER, 1, pdf.bfBold12);
        pdf.insertCell(table, "Tên môn học", Element.ALIGN_CENTER, 1, pdf.bfBold12);
        pdf.insertCell(table, "Hệ số", Element.ALIGN_CENTER, 1, pdf.bfBold12);
        pdf.insertCell(table, "Điểm", Element.ALIGN_CENTER, 1, pdf.bfBold12);
        table.setHeaderRows(1);


        double totalScore = 0;
        int totalCoefficient=0;

        //  fill data to table
        for (int i = 0; i < listMarkOfStudent.size(); i++) {
            Mark mark = listMarkOfStudent.get(i);
            pdf.insertCell(table, "" + (i + 1), Element.ALIGN_CENTER, 1, pdf.bf12);
            pdf.insertCell(table, mark.getSubject().getSubjectName(), Element.ALIGN_CENTER, 1, pdf.bf12);
            pdf.insertCell(table, mark.getSubject().getCoefficient() + "", Element.ALIGN_CENTER, 1, pdf.bf12);
            pdf.insertCell(table, mark.getScore() + "", Element.ALIGN_CENTER, 1, pdf.bf12);
            totalScore += (mark.getScore() * mark.getSubject().getCoefficient());
            totalCoefficient += mark.getSubject().getCoefficient();

        }
        //merge the cells to create a footer for that section
        pdf.insertCell(table, "Điểm trung bình:", Element.ALIGN_RIGHT, 3, pdf.bfBold12);
        pdf.insertCell(table, df.format(totalScore/totalCoefficient), Element.ALIGN_RIGHT, 1, pdf.bfBold12);

        paragraph.add(table);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void setControls() {

    }

    private void initData() {

    }

    private boolean checkGradeAndSubject() {
        if (dropdownItemsGrade.size() != 0 && dropdownItemsSubject.size() != 0)
            return true;
        else return false;
    }

    private void loadRecyclerViewStudent(String gradeId, String subjectId) {

        List<Mark> marks = markViewModel.getMarks(gradeId, subjectId);
        if (marks.size() > 0) {
            markListAdapter.submitList(marks);
            txtListEmpty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
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


    private void initialDropdownGrade(List<Grade> gradeName) {
        dropdownItemsGrade.addAll(gradeName);

        adapterGrade = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item,
                dropdownItemsGrade.stream().map(Grade::getGradeId).collect(Collectors.toList()));
        if (dropdownItemsGrade.size() != 0) {
            editTextGradeName.setText(adapterGrade.getItem(0));
            indexGrade = 0;
        }
        editTextGradeName.setAdapter(adapterGrade);

    }

    private void initialDropdownSubject(List<Subject> subjects){
        dropdownItemsSubject.addAll(subjects);

        adapterSubject = new ArrayAdapter<Subject>(requireContext(), R.layout.dropdown_item, dropdownItemsSubject);

        if(dropdownItemsSubject.size()!=0) {
            editTextSubjectName.setText(dropdownItemsSubject.get(0).toString());
            indexSubject = 0;
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
