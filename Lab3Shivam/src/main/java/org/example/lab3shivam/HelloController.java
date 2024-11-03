package org.example.lab3shivam;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private TextField emailField;  // Updated field name from usernameField to emailField

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessage;

    @FXML
    protected void onHelloButtonClick() {
        String email = emailField.getText();  // Get email from emailField
        String password = passwordField.getText();

        // Validate input
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage.setText("Both fields are required.");
        } else if (!email.matches("^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,4}$")) {
            errorMessage.setText("Please enter a valid email.");
        } else if (password.length() < 8) {
            errorMessage.setText("Password must be at least 8 characters long.");
        } else {
            try {
                // Load dashboard if validation passes
                Parent secondScene = FXMLLoader.load(getClass().getResource("dashbord.fxml"));
                Stage secondStage = new Stage();
                secondStage.setTitle("Dashboard");
                secondStage.setScene(new Scene(secondScene));
                Stage firstSceneStage = (Stage) emailField.getScene().getWindow();
                firstSceneStage.close();

                secondStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
