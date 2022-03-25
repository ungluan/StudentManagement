package com.example.studentmanagement.database.entity.relationship;


import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;

import java.util.List;

public class StudentWithMarks {
    @Embedded public Student student;
    @Relation(
            parentColumn = "MAHOCSINH",
            entityColumn = "MAHOCSINH"
    )
    public List<Mark> marks;

    public StudentWithMarks(Student student, List<Mark> marks) {
        this.student = student;
        this.marks = marks;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }
}
