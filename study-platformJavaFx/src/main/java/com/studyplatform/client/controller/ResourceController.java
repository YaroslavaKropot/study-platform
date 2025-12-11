package com.studyplatform.client.controller;

import com.studyplatform.client.model.Group;
import com.studyplatform.client.model.Resource;
import com.studyplatform.client.service.ApiService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class ResourceController {
    @FXML private ListView<String> resourceListView;
    @FXML private Label groupLabel;
    @FXML private Button addButton;
    @FXML private Button openButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;

    private ApiService apiService = ApiService.getInstance();
    private Long currentGroupId;
    private ObservableList<Resource> resources = FXCollections.observableArrayList();

    public void setGroup(Group group) {
        this.currentGroupId = group.getId();
        groupLabel.setText("Materials: " + group.getName());
        loadResources();
    }

    @FXML
    private void initialize() {
        setupListView();
    }

    private void setupListView() {
        resourceListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    openButton.setDisable(newVal == null);
                    deleteButton.setDisable(newVal == null);
                }
        );

        openButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private void loadResources() {
        try {
            List<Resource> loaded = apiService.getGroupResources(currentGroupId);
            resources.setAll(loaded);
            updateListView();
        } catch (Exception e) {
            showAlert("Loading error: " + e.getMessage());
        }
    }

    private void updateListView() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Resource r : resources) {
            String icon = "LINK".equals(r.getType()) ? "🔗 " : "📎 ";
            items.add(icon + r.getTitle());
        }
        resourceListView.setItems(items);
    }

    @FXML
    private void addResource() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Material");
        dialog.setHeaderText("Add link or file description");
        dialog.setContentText("Enter URL or name:");

        dialog.showAndWait().ifPresent(input -> {
            if (!input.trim().isEmpty()) {
                try {
                    //vytvorime resource
                    com.studyplatform.client.dto.ResourceRequest request =
                            new com.studyplatform.client.dto.ResourceRequest(
                                    input,
                                    input.startsWith("http") ? "LINK" : "FILE",
                                    input,
                                    currentGroupId
                            );

                    Resource created = apiService.createResource(request);
                    resources.add(created);
                    updateListView();

                } catch (Exception e) {
                    showAlert("Error: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void openResource() {
        int index = resourceListView.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < resources.size()) {
            Resource resource = resources.get(index);

            if ("LINK".equals(resource.getType())) {
                showAlert("Link: " + resource.getPathOrUrl() + "\n\nOpen in browser.");
            } else {
                showAlert("File: " + resource.getTitle() + "\nPath: " + resource.getPathOrUrl());
            }
        }
    }

    @FXML
    private void deleteResource() {
        int index = resourceListView.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < resources.size()) {
            Resource resource = resources.get(index);

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Delete");
            confirm.setContentText("Delete '" + resource.getTitle() + "'?");

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                try {
                    apiService.deleteResource(currentGroupId, resource.getId());
                    resources.remove(index);
                    updateListView();
                } catch (Exception e) {
                    showAlert("Delete error: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void goBack() {
        try {
            //zatvorime okno
            backButton.getScene().getWindow().hide();
        } catch (Exception e) {
            showAlert("Error: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}