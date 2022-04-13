package com.example.studentmanagement.database.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//@Entity(tableName = "LOP")
public class Grade {
//    @PrimaryKey
//    @ColumnInfo(name = "LOP")
//    @NonNull
    String gradeId;
//    @ColumnInfo(name = "CHUNHIEM")
    int teacherId;

    String image ;

    public Grade() { }

    public Grade(String gradeId, int teacherId, String image) {
        this.gradeId = gradeId;
        this.teacherId = teacherId;
        this.image = image;
    }

    public String getGradeId() {
        return gradeId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Vẫn chưa sữa funtion bên gradeAdapter
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj==null || obj.getClass() != Grade.class) return false;
        return ((Grade) obj).gradeId.equals(this.gradeId)
                && ((Grade) obj).teacherId == this.teacherId
                && ((Grade) obj).image.equals(this.image);
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeId='" + gradeId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
