package com.example.demo;

import java.util.Date;

public class Applications {
    private int applicationID;
    private String companyName;
    private Date date;
    private String Status;
    private String Notes;


    public Applications (String companyName, Date date, String Status, String Notes){
        this.companyName = companyName;
        this.date = date;
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
        date = date;
    }

    public Date getDate() {
        return date;
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
