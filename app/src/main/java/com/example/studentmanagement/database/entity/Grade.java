package com.example.studentmanagement.database.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "LOP")
public class Grade {
    @PrimaryKey
    @ColumnInfo(name = "LOP")
    @NonNull
    String gradeId;
    @ColumnInfo(name = "CHUNHIEM")
    String teacherName;

    public Grade(String gradeId, String teacherName) {
        this.gradeId = gradeId;
        this.teacherName = teacherName;
    }

    public String getGradeId() {
        return gradeId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    // Vẫn chưa sữa funtion bên gradeAdapter
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj==null || obj.getClass() != Grade.class) return false;
        return ((Grade) obj).gradeId.equals(this.gradeId)
                && ((Grade) obj).teacherName.equals(this.teacherName);
    }

    @Override
    public String toString() {
        return gradeId + "-"+ teacherName;
    }
}
