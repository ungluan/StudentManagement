package com.example.studentmanagement.database.entity;


import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class StudentWithMarks {
    @Embedded public Student student;
    @Relation(
            parentColumn = "MAHOCSINH",
            entityColumn = "MAHOCSINH"
    )
    public List<Mark> marks;
}
