package com.example.studentmanagement.database.entity;


import static androidx.room.ForeignKey.CASCADE;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.example.studentmanagement.database_sqlite.Dao.RankStudent;

import java.util.Arrays;


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

    public String rankStudent(){
        return Arrays.stream(RankStudent.values())
                .filter(rankStudent ->  score < rankStudent.getTo() && score >= rankStudent.getFrom())
                .findFirst().get().name();

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != Mark.class) return false;

        return this.getStudentId()==((Mark) obj).getStudentId()
                && this.getSubjectId()==((Mark) obj).getSubjectId()
                && this.getScore() == ((Mark) obj).getScore();


    }
}