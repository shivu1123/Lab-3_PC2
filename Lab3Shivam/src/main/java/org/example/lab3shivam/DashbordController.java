package org.example.lab3shivam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashbordController {

    // Handle the Admin button click
    @FXML
    protected void handleAdminButton(ActionEvent event) {
        try {
            Parent secondScene = FXMLLoader.load(getClass().getResource("adminpage.fxml"));

            Stage secondStage = new Stage();
            secondStage.setTitle("Admin Scene");
            secondStage.setScene(new Scene(secondScene));

            // Close the current stage using event source
            Stage firstSceneStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            firstSceneStage.close();

            secondStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle the User button click
    @FXML
    protected void handleUserButton(ActionEvent event) {
        try {
            Parent secondScene = FXMLLoader.load(getClass().getResource("employeepage.fxml"));

            Stage secondStage = new Stage();
            secondStage.setTitle("Employee Page");
            secondStage.setScene(new Scene(secondScene));

            // Close the current stage using event source
            Stage firstSceneStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            firstSceneStage.close();

            secondStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle the Logout button click
    @FXML
    protected void handleLogoutButton(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Login Page");
            loginStage.setScene(new Scene(loginPage));

            // Close the current stage using event source
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle the Exit button click
    @FXML
    protected void handleExitButton() {
        System.exit(0);
    }
}