package com.example.demo;

import javafx.application.Application;

public class Applications {
    private int applicationID;
    private String companyName;
    private String Date;
    private String Status;
    private String Notes;


    public Applications(int applicationID, String companyName, String Date, String Status, String Notes){
        this.applicationID = applicationID;
        this.companyName = companyName;
        this.Date = Date;
        this.Status = Status;
        this.Notes = Notes;
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    public int getApplicationID() {
        return applicationID;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDate() {
        return Date;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatus() {
        return Status;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getNotes() {
        return Notes;
    }
}
