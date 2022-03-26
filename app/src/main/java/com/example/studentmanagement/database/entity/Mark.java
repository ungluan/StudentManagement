package com.example.studentmanagement.database.entity;


import static androidx.room.ForeignKey.CASCADE;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;


// onDelete = NO_ACTION is Default
@Entity(
    tableName = "DIEM",
    primaryKeys = {"MAHOCSINH", "MAMONHOC"}, foreignKeys = {
        @ForeignKey(entity = Student.class, parentColumns = "MAHOCSINH",
            childColumns = "MAHOCSINH", onUpdate = CASCADE),
        @ForeignKey(entity = Subject.class, parentColumns = "MAMONHOC",
            childColumns = "MAMONHOC", onUpdate = CASCADE)
    },
        indices = {@Index(value = "MAHOCSINH"),
        @Index(value = "MAMONHOC")}
)
public class Mark {
    @ColumnInfo(name = "MAHOCSINH")
    int studentId;
    @ColumnInfo(name = "MAMONHOC")
    @NonNull
    String subjectId;
    @ColumnInfo(name = "DIEM")
    double score;

    public Mark(int studentId, @NonNull String subjectId, double score) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.score = score;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "studentId=" + studentId +
                ", subjectId='" + subjectId + '\'' +
                ", score=" + score +
                '}';
    }
}