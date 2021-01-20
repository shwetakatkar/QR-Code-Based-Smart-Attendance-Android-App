package com.example.smartattendance;

public class StudentDataModel {
    String studentname, studentid;
    public StudentDataModel(String studentname, String studentid) {
        this.studentname = studentname;
        this.studentid = studentid;
    }
    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }


}
