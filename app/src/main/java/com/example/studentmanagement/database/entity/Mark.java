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
    double mark;

    public Mark(int studentId, String subjectId, double mark) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.mark = mark;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getSubjectId() {
        return subjectId;
    }


    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }
}
