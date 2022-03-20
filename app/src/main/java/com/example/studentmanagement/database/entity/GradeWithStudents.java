package com.example.studentmanagement.database.entity;

import androidx.room.Embedded;

import androidx.room.Relation;

import java.util.List;

public class GradeWithStudents {
    @Embedded public Grade grade;
    @Relation(
            parentColumn = "LOP",
            entityColumn = "LOP"
    )
    public List<Student> students;

}
