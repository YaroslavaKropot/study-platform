package com.studyplatform.client.controller;

import com.studyplatform.client.config.AppConfig;
import com.studyplatform.client.dto.GroupRequest;
import com.studyplatform.client.model.Group;
import com.studyplatform.client.service.ApiService;
import com.studyplatform.client.service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class GroupController {
    @FXML private TableView<Group> groupsTable;
    @FXML private TableColumn<Group, Long> idColumn;
    @FXML private TableColumn<Group, String> nameColumn;
    @FXML private TableColumn<Group, String> descriptionColumn;
    @FXML private TableColumn<Group, String> createdByColumn;
    @FXML private TableColumn<Group, String> createdAtColumn;

    @FXML private Button createGroupButton;
    @FXML private Button viewGroupButton;
    @FXML private Button deleteGroupButton;
    @FXML private Button refreshButton;
    @FXML private Button backButton;

    @FXML private Label statusLabel;
    @FXML private TextField searchField;

    private ObservableList<Group> groups = FXCollections.observableArrayList();
    private ApiService apiService = ApiService.getInstance();
    private SessionManager sessionManager = SessionManager.getInstance();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadGroups();
        setupButtonActions();
        setupSearch();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdByName"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAtDisplay"));

        nameColumn.setMinWidth(150);
        descriptionColumn.setMinWidth(200);
        createdByColumn.setMinWidth(100);
        createdAtColumn.setMinWidth(120);

        groupsTable.setItems(groups);

        groupsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> updateButtonsState(newSelection != null)
        );
    }

    private void setupButtonActions() {
        createGroupButton.setOnAction(e -> openCreateGroupDialog());
        viewGroupButton.setOnAction(e -> openSelectedGroup());
        deleteGroupButton.setOnAction(e -> deleteSelectedGroup());
        refreshButton.setOnAction(e -> loadGroups());
        backButton.setOnAction(e -> goBackToMain());

        updateButtonsState(false);
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterGroups(newValue);
        });
    }

    private void loadGroups() {
        try {
            statusLabel.setText("Loading groups...");
            statusLabel.setStyle("-fx-text-fill: blue;");

            List<Group> groupsList = apiService.getGroups();
            groups.setAll(groupsList);

            statusLabel.setText("Groups loaded: " + groupsList.size());
            statusLabel.setStyle("-fx-text-fill: green;");

        } catch (Exception e) {
            statusLabel.setText("Loading error: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }

    private void filterGroups(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            groupsTable.setItems(groups);
            return;
        }

        String lowerCaseFilter = searchText.toLowerCase();
        ObservableList<Group> filteredList = FXCollections.observableArrayList();

        for (Group group : groups) {
            if (group.getName().toLowerCase().contains(lowerCaseFilter) ||
                    (group.getDescription() != null &&
                            group.getDescription().toLowerCase().contains(lowerCaseFilter))) {
                filteredList.add(group);
            }
        }

        groupsTable.setItems(filteredList);
        statusLabel.setText("Found: " + filteredList.size() + " of " + groups.size());
    }

    private void openCreateGroupDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/studyplatform/client/views/create-group-dialog.fxml")
            );
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create Group");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root, 400, 300));

            dialogStage.showAndWait();

            loadGroups();

        } catch (Exception e) {
            showError("Dialog opening error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openSelectedGroup() {
        Group selectedGroup = groupsTable.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) {
            showError("Select group to view");
            return;
        }

        try {
            //trbe potom pridat -detail-view.fxml
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/studyplatform/client/views/group-detail-view.fxml")
            );
            Parent root = loader.load();
            // GroupDetailController detailController = loader.getController();
            // detailController.setGroup(selectedGroup);

            Stage stage = (Stage) groupsTable.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Group: " + selectedGroup.getName());

        } catch (Exception e) {
            showError("Error. Cannot open the group: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteSelectedGroup() {
        Group selectedGroup = groupsTable.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) {
            showError("Select group to delete");
            return;
        }

        //checete delete?
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Group Deletion");
        alert.setContentText("Are you sure you want to delete group \"" +
                selectedGroup.getName() + "\"?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                //pridat potom apiService.deleteGroup(selectedGroup.getId());

                showSuccess("Group \"" + selectedGroup.getName() + "\" deleted");
                loadGroups();

            } catch (Exception e) {
                showError("Deletion error: " + e.getMessage());
            }
        }
    }

    private void goBackToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/studyplatform/client/views/main.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) groupsTable.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Study Platform - Main");

        } catch (Exception e) {
            showError("Return error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateButtonsState(boolean hasSelection) {
        viewGroupButton.setDisable(!hasSelection);
        deleteGroupButton.setDisable(!hasSelection);
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
    }
}