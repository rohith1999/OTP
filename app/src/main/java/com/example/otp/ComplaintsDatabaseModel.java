package com.example.otp;

public class ComplaintsDatabaseModel {

    String problemStatement;
    String staffName;
    int status;
    String timeStamp;

    public String getProblemStatement() {
        return problemStatement;
    }

    public String getStaffName() {
        return staffName;
    }

    public int getStatus() {
        return status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setProblemStatement(String problemStatement) {
        this.problemStatement = problemStatement;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ComplaintsDatabaseModel(){

    }

    public ComplaintsDatabaseModel(String problemStatement, String staffName, int status, String timeStamp) {
        this.problemStatement = problemStatement;
        this.staffName = staffName;
        this.status = status;
        this.timeStamp = timeStamp;
    }
}
