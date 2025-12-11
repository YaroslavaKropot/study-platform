package com.studyplatform.client.controller;

import com.studyplatform.client.model.Group;
import com.studyplatform.client.model.Task;
import com.studyplatform.client.model.TaskStatus;
import com.studyplatform.client.service.ApiService;
import com.studyplatform.client.service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class TaskController {
    @FXML private TableView<Task> tasksTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> statusColumn;
    @FXML private TableColumn<Task, String> deadlineColumn;

    @FXML private Button createTaskButton;
    @FXML private Button deleteTaskButton;
    @FXML private Button refreshButton;
    @FXML private Button backButton;

    @FXML private Label groupLabel;
    @FXML private Label statusLabel;

    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    private ApiService apiService = ApiService.getInstance();
    private Long currentGroupId;
    private String currentGroupName;

    public void setGroup(Group group) {
        this.currentGroupId = group.getId();
        this.currentGroupName = group.getName();
        groupLabel.setText("Group tasks: " + currentGroupName);
        loadTasks();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        setupButtonActions();
    }

    private void setupTableColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusDisplay"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadlineDisplay"));

        tasksTable.setItems(tasks);
    }

    private void setupButtonActions() {
        createTaskButton.setOnAction(e -> createNewTask());
        deleteTaskButton.setOnAction(e -> deleteSelectedTask());
        refreshButton.setOnAction(e -> loadTasks());
        backButton.setOnAction(e -> goBackToGroups());
    }

    private void loadTasks() {
        if (currentGroupId == null) return;

        try {
            List<Task> tasksList = apiService.getGroupTasks(currentGroupId);
            tasks.setAll(tasksList);
            statusLabel.setText("Tasks: " + tasksList.size());
            statusLabel.setStyle("-fx-text-fill: green;");

        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void createNewTask() {
        //dialog na vytvorenie ulohy
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Task");
        dialog.setHeaderText("Group: " + currentGroupName);
        dialog.setContentText("Enter task name:");

        dialog.showAndWait().ifPresent(taskTitle -> {
            if (!taskTitle.trim().isEmpty()) {
                try {
                    Task createdTask = apiService.createTask(
                            currentGroupId,
                            taskTitle.trim(),
                            "Task description"
                    );
                    statusLabel.setText("Task created: " + createdTask.getTitle());
                    loadTasks();

                } catch (Exception e) {
                    statusLabel.setText("Error: " + e.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red;");
                }
            }
        });
    }

    private void deleteSelectedTask() {
        Task selectedTask = tasksTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert("Select task to delete", Alert.AlertType.WARNING);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Task deletion");
        alert.setContentText("Delete task \"" + selectedTask.getTitle() + "\"?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                //deleteTask v ApiService
                //apiService.deleteTask(selectedTask.getId());

                statusLabel.setText("Task deleted: " + selectedTask.getTitle());
                loadTasks();

            } catch (Exception e) {
                statusLabel.setText("Delete error: " + e.getMessage());
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }

    private void goBackToGroups() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/studyplatform/client/views/group-view.fxml")
            );
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));

        } catch (Exception e) {
            showAlert("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }
}