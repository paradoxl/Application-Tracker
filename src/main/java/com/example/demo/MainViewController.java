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
//TODO: Possible additions add ability to track Outisde URL Username and password for each individual application.
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
    @FXML
    public Label callBackLabel;
    @FXML
    public Label interviewLabel;
    @FXML
    public Label rejectionLabel;


    @SuppressWarnings("unchecked")
    @Override
    /**
     * This method will populate the main table and show as many active applications as the table has space.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            manipulateStats();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    /**
     * This method will show a breakdown of how the users applications are working.
     * It will show percentages of callbacks and interviews based on how many applications are in the system.
     * @throws SQLException
     */
    public void manipulateStats() throws SQLException {
        //TODO: Create a method that will update the percentages based on callbacks and interviews.
        // Color code the values <25% == red >25>50 == yellow >50 == Green

        ObservableList<Applications> allApps = ApplicationDAO.getApplications();
        float total = 0;
        float noCall = 0;
        float call = 0;
        float interview = 0;
        float reject = 0;
        for (Applications app : allApps){
            total++;
            if(app.getStatus().equals("None")){
                noCall++;
            }
            if(app.getStatus().equals("Call Back")){
                call++;
            }
            if(app.getStatus().equals("Interview")){
                interview++;
            }
            if(app.getStatus().equals("Rejected")){
                reject++;
            }

        }
        float callbackPercentage = (call/total) * 100;
        float InterviewPercentage = (interview/total) * 100;
        float RejectionPercentage = (reject/total) * 100;

        callBackLabel.setText(String.format("%.0f%%",callbackPercentage));
        interviewLabel.setText(String.format("%.0f%%",InterviewPercentage));
        rejectionLabel.setText(String.format("%.0f%%",RejectionPercentage));


    }

    /**
     * This method will bring the user to a page where they can add applications to the system.
     * @param actionEvent
     * @throws IOException
     */
    public void addApplication(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("add_view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Application");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method will remove the selected application from the program and database.
     * Future iterations could possibly move this to a sepereate database for long term storage.
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
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

    /**
     * This method will send the user to a page where they can edit the application details
     * The method will bring in values and prepopulate the information for the currently selected application
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void editApplication(ActionEvent actionEvent) throws IOException, SQLException {
        try {
            Applications selected = mainTableView.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("edit_view.fxml"));
            Scene scene = new Scene(loader.load());
            editViewController helper = loader.getController();
            helper.populate(selected);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Customer Records");
            stage.setScene(scene);
            stage.show();
        }
        catch (NullPointerException e){
            Alert NothingSelected = new Alert(Alert.AlertType.ERROR, "You have no selected an Application to edit.", ButtonType.OK);
            NothingSelected.showAndWait();
        }
    }
}