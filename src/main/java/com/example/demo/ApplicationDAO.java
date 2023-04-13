package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ApplicationDAO {


    public static ObservableList<Applications> getApplications() throws SQLException {
        ObservableList<Applications> applicationsList = FXCollections.observableArrayList();
        String query = "Select * From Applications";
        PreparedStatement ps = InitCon.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            int applicationID = rs.getInt("ApplicationID");
            String companyName = rs.getString("CompanyName");
            String status = rs.getString("Status");
            String notes = rs.getString("Notes");

            Applications input = new Applications(companyName, Date.valueOf(LocalDate.now()),status,notes);
            input.setApplicationID(applicationID);
            applicationsList.add(input);
        }

        return applicationsList;
    }
}
