package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class AddViewController {
    public TextField companyNameTXTFLD;
    public ComboBox<Date> dateCOMBO;
    public TextField statusTEXTFLD;
    public TextArea notesTXTAREA;

    public void saveBTNPRESS(ActionEvent actionEvent) throws SQLException, IOException {
        Alert save = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to save this application", ButtonType.YES, ButtonType.NO);
        save.showAndWait();


        if (save.getResult() == ButtonType.YES) {
            String name = companyNameTXTFLD.getText();
//            Date date = (Date) dateCOMBO.getItems();
            String status = statusTEXTFLD.getText();
            String notes = notesTXTAREA.getText();

//            Applications application = new Applications(name, date, status, notes);

            String insert = "INSERT INTO Applications (CompanyName,Date,Status,Notes) VALUES (?,?,?,?)";
            PreparedStatement ps = InitCon.connection.prepareStatement(insert);
            ps.setString(1,name);
            //TODO: Fix this to add combo-box. Need to add the values there first.
            // The date might be able to autopopulate or use user data.
            ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            ps.setString(3, status);
            ps.setString(4, notes);
            ps.executeUpdate();

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Main_View.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Applications");
            stage.setScene(scene);
            stage.show();

        }
        else {
            return;
        }
    }

    public void backBTNPRESS(ActionEvent actionEvent) throws IOException {
        Alert back = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you wish to exit?", ButtonType.YES,ButtonType.NO);
        back.showAndWait();

        if (back.getResult() == ButtonType.YES) {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Main_View.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Applications");
            stage.setScene(scene);
            stage.show();
        }
        else {
            return;
        }
    }
}
