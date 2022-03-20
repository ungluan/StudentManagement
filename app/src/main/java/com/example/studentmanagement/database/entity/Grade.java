package com.example.studentmanagement.database.entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "LOP")
public class Grade {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "MALOP")
    final int id;
    @ColumnInfo(name = "LOP")
    String gradeName;
    @ColumnInfo(name = "CHUNHIEM")
    String teacherName;

    public Grade(int id, String gradeName, String teacherName) {
        this.id = id;
        this.gradeName = gradeName;
        this.teacherName = teacherName;
    }

    public int getId() {
        return id;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj==null || obj.getClass() != Grade.class) return false;
        return ((Grade) obj).gradeName.equals(this.gradeName)
                && ((Grade) obj).teacherName.equals(this.teacherName);
    }
}
