package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Main method
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main_View.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        stage.setTitle("Currently Submitted Applications");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches application
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        InitCon.openConnection();
        launch();
    }
}