package com.example.demo;

import javafx.collections.FXCollections;
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

public class editViewController implements Initializable {
    @FXML
    public TextField companyNameTXTFLD;
    @FXML
    public ChoiceBox<String> statusChoice;
    @FXML
    public TextArea notesTXTAREA;
    @FXML
    public Label idLabel;
    @FXML
    public DatePicker datePicker;
    @FXML
    public TextField urlTextFLD;
    @FXML
    public TextField userNameTextFLD;
    @FXML
    public TextField passwordTextFLD;

    public void saveBTNPRESS(ActionEvent actionEvent) throws SQLException, IOException, NoSuchAlgorithmException {
        Alert continueSave = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to update this application", ButtonType.YES,ButtonType.NO);
        continueSave.showAndWait();


        if (continueSave.getResult() == ButtonType.YES) {

            //TODO: Update query has an error.
            String query = "Update Applications Set CompanyName = ?, Status = ?, Notes = ?, URL = ?, Username = ?, Password = ? Where ApplicationId = ? ";
            PreparedStatement ps = InitCon.connection.prepareStatement(query);
            ps.setInt(4, Integer.parseInt(idLabel.getText()));
            ps.setString(1, companyNameTXTFLD.getText());
            ps.setString(2, statusChoice.getSelectionModel().getSelectedItem());
            ps.setString(3, notesTXTAREA.getText());
            ps.setString(4,urlTextFLD.getText());
            ps.setString(5, userNameTextFLD.getText());

            SecureRandom rand = new SecureRandom();
            byte[] salt = new byte[16];
            rand.nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);

            byte[] hashed = md.digest(passwordTextFLD.getText().getBytes(StandardCharsets.UTF_8));

            ps.setString(6, Arrays.toString(hashed));

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
      
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> statusList = FXCollections.observableArrayList();
        statusList.add("None");
        statusList.add("Ghosted");
        statusList.add("Call Back");
        statusList.add("Interview");
        statusList.add("Offer");
        statusList.add("Rejected");
        statusChoice.setItems(statusList);
    }

    public void populate(Applications selected) throws SQLException {
        idLabel.setText(String.valueOf(selected.getApplicationID()));
        companyNameTXTFLD.setText(selected.getCompanyName());
        statusChoice.setValue(selected.getStatus());
        notesTXTAREA.setText(selected.getNotes());
        urlTextFLD.setText(selected.getURL());
    }
}
