package com.example.studentmanagement.database.entity;

import androidx.room.ColumnInfo;

import java.util.Objects;

public class MarkDTO {
    private int studentId;
    private String firstName;
    private String lastName;
    private String gender;
    private String birthday;
    private String image;
    private String gradeId;
    private String subjectId;
    private Double mark;

    public MarkDTO(int studentId, String firstName, String lastName, String gender,
                   String birthday, String image, String gradeId, String subjectId, Double mark) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.image = image;
        this.gradeId = gradeId;
        this.subjectId = subjectId;
        this.mark = mark;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarkDTO)) return false;
        MarkDTO markDTO = (MarkDTO) o;
        return getStudentId() == markDTO.getStudentId() &&
                Objects.equals(getFirstName(), markDTO.getFirstName()) &&
                Objects.equals(getLastName(), markDTO.getLastName()) &&
                Objects.equals(getGender(), markDTO.getGender()) &&
                Objects.equals(getBirthday(), markDTO.getBirthday()) &&
                Objects.equals(image, markDTO.image) &&
                Objects.equals(getGradeId(), markDTO.getGradeId()) &&
                Objects.equals(getSubjectId(), markDTO.getSubjectId()) &&
                Objects.equals(getMark(), markDTO.getMark());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStudentId(), getFirstName(),
                getLastName(), getGender(), getBirthday(),
                image, getGradeId(), getSubjectId(), getMark());
    }
}
