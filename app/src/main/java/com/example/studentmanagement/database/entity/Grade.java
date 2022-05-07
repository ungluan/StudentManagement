package com.example.studentmanagement.database.entity;

import android.net.Uri;

import androidx.annotation.Nullable;

//@Entity(tableName = "LOP")
public class Grade {
//    @PrimaryKey
//    @ColumnInfo(name = "LOP")
//    @NonNull
    String gradeId;
//    @ColumnInfo(name = "CHUNHIEM")
    int teacherId;
    String image ;
    int gradeSchool;

    public Grade() { }

    public Grade(String gradeId, int teacherId, String image, int gradeSchool) {
        this.gradeId = gradeId;
        this.teacherId = teacherId;
        this.image = image;
        this.gradeSchool = gradeSchool;
    }

    public static void getABC(){

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

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public int getGradeSchool() {
        return gradeSchool;
    }

    public void setGradeSchool(int gradeSchool) {
        this.gradeSchool = gradeSchool;
    }

    // Vẫn chưa sữa funtion bên gradeAdapter
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj==null || obj.getClass() != Grade.class) return false;
        return ((Grade) obj).gradeId.equals(this.gradeId)
                && ((Grade) obj).teacherId == this.teacherId
                && ((Grade) obj).image.equals(this.image)
                && ((Grade) obj).gradeSchool == this.gradeSchool;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeId='" + gradeId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", image='" + image + '\'' +
                ", gradeSchool ='" + gradeSchool + '\'' +
                '}';
    }

}
