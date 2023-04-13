package com.example.demo;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    public TableView<Applications> mainTableView;
    @FXML
    public TableColumn<?,?> applicationIDTC;
    @FXML
    public TableColumn<?,?> comapnyNameTC;
    @FXML
    public TableColumn<?,?> dateTC;
    @FXML
    public TableColumn<?,?> statusTC;
    @FXML
    public TableColumn<?,?> notesTC;



    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Applications> applicationInputList = ApplicationDAO.getApplications();

            mainTableView.setItems(applicationInputList);
            applicationIDTC.setCellValueFactory(new PropertyValueFactory<>("ApplicationID"));
            comapnyNameTC.setCellValueFactory(new PropertyValueFactory<>("companyName"));
            dateTC.setCellValueFactory(new PropertyValueFactory<>("Date"));
            statusTC.setCellValueFactory(new PropertyValueFactory<>("Status"));
            notesTC.setCellValueFactory(new PropertyValueFactory<>("Notes"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void addApplication(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("add_view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Application");
        stage.setScene(scene);
        stage.show();
    }

    public void deleteApplication(ActionEvent actionEvent) throws SQLException, IOException {

        int highLighted = mainTableView.getSelectionModel().getSelectedItem().getApplicationID();
        Alert delete = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to Delete this application, ID Number: " + highLighted,ButtonType.YES, ButtonType.NO);
        delete.showAndWait();
        if (delete.getResult() == ButtonType.YES){
            // delete query
            String query = "DELETE FROM Applications WHERE ApplicationID = ?";
            PreparedStatement ps = InitCon.connection.prepareStatement(query);
            ps.setInt(1,highLighted);
            ps.executeUpdate();

            // Refresh the page
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Main_View.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Applicationsg");
            stage.setScene(scene);
            stage.show();
        }

    }

    public void editApplication(ActionEvent actionEvent) {
    }
}