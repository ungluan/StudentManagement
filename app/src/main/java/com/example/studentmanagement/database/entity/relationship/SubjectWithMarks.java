package com.example.studentmanagement.database.entity.relationship;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Subject;

import java.util.List;

public class SubjectWithMarks {
    @Embedded public Subject subject;
    @Relation(
            parentColumn = "MAMONHOC",
            entityColumn = "MAMONHOC"
    )
    public List<Mark> marks;
}
