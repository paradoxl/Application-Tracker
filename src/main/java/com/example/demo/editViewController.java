package com.example.demo;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class editViewController implements Initializable {
    @FXML
    public TextField companyNameTXTFLD;
    @FXML
    public ComboBox<?> dateCOMBO;
    @FXML
    public TextField statusTEXTFLD;
    @FXML
    public TextArea notesTXTAREA;
    @FXML
    public Label idLabel;

    public void saveBTNPRESS(ActionEvent actionEvent) throws SQLException, IOException {
        Alert continueSave = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to update this application", ButtonType.YES,ButtonType.NO);
        continueSave.showAndWait();


        if (continueSave.getResult() == ButtonType.YES) {

            //TODO: Update query has an error.
            String query = "Update Applications Set CompanyName = ?, Status = ?, Notes = ? Where ApplicationId = ? ";
            PreparedStatement ps = InitCon.connection.prepareStatement(query);
            ps.setInt(4, Integer.parseInt(idLabel.getText()));
            ps.setString(1, companyNameTXTFLD.getText());
            ps.setString(2, statusTEXTFLD.getText());
            ps.setString(3, notesTXTAREA.getText());
            ps.executeUpdate();

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Main_View.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Applications");
            stage.setScene(scene);
            stage.show();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void populate(Applications selected) throws SQLException {
        idLabel.setText(String.valueOf(selected.getApplicationID()));
        int id = selected.getApplicationID();
        String companyName = selected.getCompanyName();
        Date date = selected.getDate();
        String status = selected.getStatus();
        String notes = selected.getNotes();

        companyNameTXTFLD.setText(companyName);
        //TODO: Implement a combo box for the date.
        //dateCOMBO.setItems();
        statusTEXTFLD.setText(status);
        notesTXTAREA.setText(notes);
    }
}
