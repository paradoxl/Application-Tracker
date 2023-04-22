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
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

public class AddViewController implements Initializable {
    @FXML
    public TextField companyNameTXTFLD;
    @FXML
    public ComboBox<Date> dateCOMBO;
    @FXML
    public TextArea notesTXTAREA;
    @FXML
    public ChoiceBox<String> statusChoice;
    @FXML
    public DatePicker datePicker;
    @FXML
    public TextField urlTextFLD;
    @FXML
    public TextField userNameTextFLD;
    @FXML
    public TextField passwordTextFLD;

    /**
     * This method will save documents to the mySQL database
     * confirmation will be provided before writing.
     * Issues: One way encrpytion of password requires something to check against.
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void saveBTNPRESS(ActionEvent actionEvent) throws SQLException, IOException, NoSuchAlgorithmException {
        Alert save = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to save this application", ButtonType.YES, ButtonType.NO);
        save.showAndWait();

        //TODO: Date is showing correct values but does not actually display them in the table.
        if (save.getResult() == ButtonType.YES) {
            String name = companyNameTXTFLD.getText();
            LocalDate date = datePicker.getValue();
            System.out.println(datePicker.getValue());
            String status = statusChoice.getSelectionModel().getSelectedItem();
            String notes = notesTXTAREA.getText();



            String insert = "INSERT INTO Applications (CompanyName,Date,Status,Notes,URL) VALUES (?,?,?,?,?)";
            PreparedStatement ps = InitCon.connection.prepareStatement(insert);
            ps.setString(1,name);
            //TODO: Fix this to add combo-box. Need to add the values there first.
            // The date might be able to autopopulate or use user data.
            ps.setDate(2, java.sql.Date.valueOf(date));
            ps.setString(3, status);
            ps.setString(4, notes);
            if (urlTextFLD.getText().isEmpty()){
                ps.setString(5,"No URL available");
            }
            else {
                ps.setString(5, urlTextFLD.getText());
            }
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

    /**
     * This method will send the user back to the main screen
     * @param actionEvent
     * @throws IOException
     */
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
    /**
     * This method is used to set up the initial values within the choicebox pertaining to offer status.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
       ObservableList<String> statusList = FXCollections.observableArrayList();
       statusList.add("None");
       statusList.add("Ghosted");
       statusList.add("Call Back");
       statusList.add("Interview");
       statusList.add("Offer");
       statusList.add("Rejected");

       statusChoice.setValue("None"); // Default value for the status will always be none
       statusChoice.setItems(statusList);
    }
}
