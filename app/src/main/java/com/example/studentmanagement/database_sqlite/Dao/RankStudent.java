package com.example.studentmanagement.database_sqlite.Dao;

public enum RankStudent {

    Average(0, 5),
    Good(5,8),
    Excellent(8, 10.1f);


    float from, to;

    RankStudent(float from, float to) {
        this.from = from;
        this.to = to;
    }

    public float getFrom() {
        return from;
    }

    public void setFrom(float from) {
        this.from = from;
    }

    public float getTo() {
        return to;
    }

    public void setTo(float to) {
        this.to = to;
    }
}
