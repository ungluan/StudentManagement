package com.example.studentmanagement.database.entity.relationship;

import androidx.room.Embedded;

import androidx.room.Relation;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Student;

import java.util.List;

public class GradeWithStudents {
    @Embedded public Grade grade;
    @Relation(
            parentColumn = "LOP",
            entityColumn = "LOP"
    )
    public List<Student> students;

}
