package com.studyplatform.client.controller;

import com.studyplatform.client.model.User;
import com.studyplatform.client.service.ApiService;
import com.studyplatform.client.service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label errorLabel;

    private ApiService apiService = ApiService.getInstance();

    @FXML
    private void initialize() {
        loginButton.setOnAction(e -> login());
        registerButton.setOnAction(e -> register());
    }

    @FXML
    private void login() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Fill all fields");
            return;
        }

        try {
            User user = apiService.login(email, password);
            openMainWindow(user);
        } catch (Exception e) {
            showError("Login error: " + e.getMessage());
        }
    }

    @FXML
    private void register() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Fill all fields");
            return;
        }

        //meno z emailu
        String name = email.split("@")[0];

        try {
            User user = apiService.register(name, email, password);
            openMainWindow(user);
        } catch (Exception e) {
            showError("Registration error: " + e.getMessage());
        }
    }

    private void openMainWindow(User user) {
        try {
            //zatvorit prihlasovacie okno
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

            //otvorit hlavne okno
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/studyplatform/client/views/main.fxml")
            );
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.setUser(user);

            Stage stage = new Stage();
            stage.setTitle("Study Platform - " + user.getName());
            stage.setScene(new Scene(root, 800, 600));
            stage.show();

        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}