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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * This class is used to control the main screen
 */
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
    @FXML
    public Label ghostLabel;
    @FXML
    public Label noResponse;


    @SuppressWarnings("unchecked")
    @Override
    /**
     * This method will populate the main table and show as many active applications as the table has space.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            manipulateStats();
            automaticGhosting();
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
        float ghost = 0;
        float call = 0;
        float interview = 0;
        float reject = 0;
        float none = 0;

        for (Applications app : allApps){
            total++;

            if(app.getStatus().equals("Ghosted")){
                ghost++;
            }
            if(app.getStatus().equals("None")){
                none++;
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
        float ghostPercentage = (ghost/total) * 100;
        float nonePercentage = (none/total) *100;

        callBackLabel.setText(String.format("%.0f%%",callbackPercentage));
        interviewLabel.setText(String.format("%.0f%%",InterviewPercentage));
        rejectionLabel.setText(String.format("%.0f%%",RejectionPercentage));
        ghostLabel.setText(String.format("%.0f%%",ghostPercentage));
        noResponse.setText(String.format("%.0f%%",nonePercentage));


    }

    /**
     * This method will determine if applications have become stale.
     * if the application was sent 30 days previously with no response. The system will automatically update to Ghosted.
     * @throws SQLException
     */
    public void automaticGhosting() throws SQLException {
        ObservableList<Applications> allApps = ApplicationDAO.getApplications();

        for (Applications app : allApps){
        LocalDate date = app.getDate();
        LocalDate ghostSwitch = date.plusDays(30);

        if(LocalDate.now().isAfter(ghostSwitch) && app.getStatus().equals("None")){
            String query = "Update Applications Set Status = ? Where ApplicationId = ?";
            PreparedStatement ps = InitCon.connection.prepareStatement(query);
            ps.setString(1, "Ghosted");
            ps.setInt(2, app.getApplicationID());
            ps.executeUpdate();
        }


        }

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
            System.out.println(selected);
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("edit_view.fxml"));
            Scene scene = new Scene(loader.load());
            editViewController helper = loader.getController();
            helper.populate(selected);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Edit Application");
            stage.setScene(scene);
            stage.show();
        }
        catch (NullPointerException e){
            Alert NothingSelected = new Alert(Alert.AlertType.ERROR, "You have no selected an Application to edit.", ButtonType.OK);
            NothingSelected.showAndWait();
            e.printStackTrace();
        }
    }


    /**
     * This method will allow the user to open the edit page by double-clicking on the entry in the table view.
     * @param mouseEvent
     * @throws IOException
     * @throws SQLException
     */
    public void openEditFromClick(MouseEvent mouseEvent) throws IOException, SQLException {
        try {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {

                    Applications selected = mainTableView.getSelectionModel().getSelectedItem();
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("edit_view.fxml"));
                    Scene scene = new Scene(loader.load());
                    editViewController helper = loader.getController();
                    helper.populate(selected);
                    Stage stage = (Stage) ((TableView<Applications>) mouseEvent.getSource()).getScene().getWindow();
                    stage.setTitle("Edit Application");
                    stage.setScene(scene);
                    stage.show();
                }
            }
        } catch (Exception NullPointerExection) {
            System.out.println("Not actually clicking on anything");
        }


    }
}