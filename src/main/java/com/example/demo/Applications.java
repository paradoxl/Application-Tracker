package com.example.demo;

import java.time.LocalDate;

/**
 * This class is used to store information pertaining to applications.
 */
public class Applications {
    private int applicationID;
    private String companyName;
    private LocalDate date;
    private String Status;
    private String Notes;
    private String URL;



    public Applications (String companyName, LocalDate date, String Status, String Notes, String URL){
        this.companyName = companyName;
        this.date = date;
        this.Status = Status;
        this.Notes = Notes;
        this.URL = URL;
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

    public  LocalDate getDate() {
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

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

}
