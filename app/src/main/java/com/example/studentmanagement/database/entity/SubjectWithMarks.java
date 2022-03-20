package com.example.studentmanagement.database.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SubjectWithMarks {
    @Embedded public Subject subject;
    @Relation(
            parentColumn = "MAMONHOC",
            entityColumn = "MAMONHOC"
    )
    public List<Mark> marks;
}
