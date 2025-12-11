package com.studyplatform.client.controller;

import com.studyplatform.client.model.User;
import com.studyplatform.client.model.Group;
import com.studyplatform.client.model.Task;
import com.studyplatform.client.service.ApiService;
import com.studyplatform.client.service.WebSocketService;
import com.sun.javafx.menu.MenuItemBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class MainController {
    @FXML private Label userLabel;
    @FXML private Button logoutButton;
    @FXML private ListView<String> groupsList;
    @FXML private ListView<String> tasksList;
    @FXML private Button createGroupButton;
    @FXML private Button deleteGroupButton;
    @FXML private Button viewTasksButton;
    @FXML private Button addTaskButton;
    @FXML private Button resourcesButton;
    @FXML private Button membersButton;
    @FXML private Button deleteTaskButton;

    private User currentUser;
    private ApiService apiService = ApiService.getInstance();
    private Long selectedGroupId;

    public void setUser(User user) {
        this.currentUser = user;
        userLabel.setText("Welcome, " + user.getName());
        loadGroups();
        connectWebSocket();
    }

    @FXML
    private void initialize() {
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        logoutButton.setOnAction(e -> logout());
        createGroupButton.setOnAction(e -> createGroup());
        deleteTaskButton.setOnAction(e -> deleteTask());
        viewTasksButton.setOnAction(e -> viewTasks());
        addTaskButton.setOnAction(e -> addTask());
        resourcesButton.setOnAction(e -> viewResources());
        membersButton.setOnAction(e -> viewMembers());
        deleteGroupButton.setOnAction(e -> deleteGroup());
        groupsList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> onGroupSelected(newVal)
        );
    }

    private void connectWebSocket() {
        try {
            WebSocketService ws = WebSocketService.getInstance();
            ws.connectToServer();
            System.out.println("WebSocket connected");
        } catch (Exception e) {
            System.out.println("WebSocket not connected: " + e.getMessage());
        }
    }

    private void loadGroups() {
        try {
            List<Group> groups = apiService.getGroups();
            groupsList.getItems().clear();

            for (Group group : groups) {
                groupsList.getItems().add(group.getName());
            }
        } catch (Exception e) {
            showAlert("Error loading groups: " + e.getMessage());
        }
    }

    private void onGroupSelected(String groupName) {
        if (groupName != null) {
            // Find the ID of the selected group
            try {
                List<Group> groups = apiService.getGroups();
                for (Group group : groups) {
                    if ((group.getName()).equals(groupName)) {
                        selectedGroupId = group.getId();
                        loadTasks(group.getId());
                        break;
                    }
                }
            } catch (Exception e) {
                showAlert("Error: " + e.getMessage());
            }
        }
    }

    private void loadTasks(Long groupId) {
        try {
            System.out.println("DEBUG: Loading tasks for group ID: " + groupId);
            List<Task> tasks = apiService.getGroupTasks(groupId);
            System.out.println("DEBUG: Received " + tasks.size() + " tasks");

            tasksList.getItems().clear();

            for (Task task : tasks) {
                System.out.println("DEBUG: Task - ID: " + task.getId() + ", Title: " + task.getTitle() + ", Status: " + task.getStatus());
                String prefix = "";
                switch (task.getStatus()) {
                    case NEW: prefix = "[NEW] "; break;
                    case IN_PROGRESS: prefix = "[IN PROGRESS] "; break;
                    case COMPLETED: prefix = "[COMPLETED] "; break;
                }
                tasksList.getItems().add(prefix + task.getTitle());
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Error loading tasks: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error loading tasks: " + e.getMessage());
        }
    }

    private void createGroup() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Group");
        dialog.setHeaderText("Create new study group");
        dialog.setContentText("Enter group name:");

        dialog.showAndWait().ifPresent(groupName -> {
            if (!groupName.trim().isEmpty()) {
                try {
                    Group group = apiService.createGroup(groupName.trim(), "Group description");
                    loadGroups();
                    showAlert("Group created: " + group.getName());
                } catch (Exception e) {
                    showAlert("Error: " + e.getMessage());
                }
            }
        });
    }
    private void deleteGroup() {
        String selectedGroupName = groupsList.getSelectionModel().getSelectedItem();

        if (selectedGroupName == null) {
            showAlert("Please select a group to delete.");
            return;
        }

        try {
            List<Group> allGroups = apiService.getGroups();

            Group groupToDelete = null;
            for (Group g : allGroups) {
                if (g.getName().equals(selectedGroupName)) {
                    groupToDelete = g;
                    break;
                }
            }

            if (groupToDelete == null) {
                showAlert("Group not found: " + selectedGroupName);
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Delete Group");
            confirm.setHeaderText("Delete group '" + selectedGroupName + "'?");
            confirm.setContentText("This will delete all tasks and members. Continue?");

            Button yesButton = (Button) confirm.getDialogPane().lookupButton(ButtonType.OK);
            yesButton.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                apiService.deleteGroup(groupToDelete.getId());

                showAlert("Group deleted: " + selectedGroupName);
                loadGroups();
                tasksList.getItems().clear();

            }

        } catch (Exception e) {
            showAlert("Error: " + e.getMessage());
        }
    }

    private void viewTasks() {
        if (selectedGroupId == null) {
            showAlert("Select group");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/studyplatform/client/views/task-view.fxml")
            );
            Parent root = loader.load();

            TaskController controller = loader.getController();
            Group group = new Group();
            group.setId(selectedGroupId);
            group.setName("Group");
            controller.setGroup(group);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Group Tasks");
            stage.show();

        } catch (Exception e) {
            showAlert("Error: " + e.getMessage());
        }
    }

    private void addTask() {
        if (selectedGroupId == null) {
            showAlert("Select group");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Task");
        dialog.setHeaderText("Add task to group");
        dialog.setContentText("Enter task name:");

        dialog.showAndWait().ifPresent(taskTitle -> {
            if (!taskTitle.trim().isEmpty()) {
                try {
                    Task task = apiService.createTask(selectedGroupId, taskTitle.trim(), "Task description");
                    loadTasks(selectedGroupId);
                    showAlert("Task added: " + task.getTitle());
                } catch (Exception e) {
                    showAlert("Error: " + e.getMessage());
                }
            }
        });
    }
    private void deleteTask() {
        System.out.println("=== DELETE TASK DEBUG START ===");

        String selectedTaskString = tasksList.getSelectionModel().getSelectedItem();
        System.out.println("[DEBUG] Selected task string: '" + selectedTaskString + "'");
        System.out.println("[DEBUG] Selected group ID: " + selectedGroupId);

        if (selectedTaskString == null) {
            System.out.println("[DEBUG] No task selected - showing alert");
            showAlert("Please select a task to delete.");
            System.out.println("=== DELETE TASK DEBUG END ===");
            return;
        }

        if (selectedGroupId == null) {
            System.out.println("[DEBUG] No group selected - showing alert");
            showAlert("No group selected.");
            System.out.println("=== DELETE TASK DEBUG END ===");
            return;
        }

        try {
            // Извлечение названия задачи
            String taskTitle;
            if (selectedTaskString.startsWith("[")) {
                taskTitle = selectedTaskString.substring(selectedTaskString.indexOf("]") + 2);
                System.out.println("[DEBUG] Task title after removing status: '" + taskTitle + "'");
            } else {
                taskTitle = selectedTaskString;
                System.out.println("[DEBUG] Task title (no status): '" + taskTitle + "'");
            }

            // Получение всех задач группы
            System.out.println("[DEBUG] Fetching tasks for group ID: " + selectedGroupId);
            List<Task> tasks = apiService.getGroupTasks(selectedGroupId);
            System.out.println("[DEBUG] Found " + tasks.size() + " tasks in group");

            // Отладочный вывод всех задач
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                System.out.println("[DEBUG] Task " + i + ": ID=" + t.getId() +
                        ", Title='" + t.getTitle() +
                        "', Status=" + t.getStatus());
            }

            // Поиск задачи по названию
            Task taskToDelete = null;
            for (Task task : tasks) {
                System.out.println("[DEBUG] Comparing: search='" + taskTitle +
                        "' vs task='" + task.getTitle() + "' -> " +
                        task.getTitle().equals(taskTitle));
                if (task.getTitle().equals(taskTitle)) {
                    taskToDelete = task;
                    System.out.println("[DEBUG] Found matching task! ID: " + task.getId());
                    break;
                }
            }

            if (taskToDelete == null) {
                System.out.println("[DEBUG] Task not found in list: '" + taskTitle + "'");
                showAlert("Task not found: " + taskTitle);
                System.out.println("=== DELETE TASK DEBUG END ===");
                return;
            }

            System.out.println("[DEBUG] Task to delete - ID: " + taskToDelete.getId() +
                    ", Title: '" + taskToDelete.getTitle() + "'");

            // Подтверждение
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Delete Task");
            confirm.setHeaderText("Delete task '" + taskTitle + "'?");
            confirm.setContentText("This action cannot be undone.");

            Button yesButton = (Button) confirm.getDialogPane().lookupButton(ButtonType.OK);
            yesButton.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

            System.out.println("[DEBUG] Showing confirmation dialog...");
            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                System.out.println("[DEBUG] User confirmed deletion");
                System.out.println("[DEBUG] Calling apiService.deleteTask(" +
                        selectedGroupId + ", " + taskToDelete.getId() + ")");

                try {
                    apiService.deleteTask(selectedGroupId, taskToDelete.getId());
                    System.out.println("[DEBUG] API call successful");

                    showAlert("Task deleted: " + taskTitle);
                    System.out.println("[DEBUG] Alert shown, reloading tasks...");

                    loadTasks(selectedGroupId);
                    System.out.println("[DEBUG] Tasks reloaded");

                } catch (Exception apiError) {
                    System.err.println("[ERROR] API call failed: " + apiError.getMessage());
                    apiError.printStackTrace();
                    showAlert("Error deleting task: " + apiError.getMessage());
                }
            } else {
                System.out.println("[DEBUG] User cancelled deletion");
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Exception in deleteTask: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error deleting task: " + e.getMessage());
        }

        System.out.println("=== DELETE TASK DEBUG END ===\n");
    }

    private void viewResources() {
        if (selectedGroupId == null) {
            showAlert("Select group");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/studyplatform/client/views/resource-view.fxml")
            );
            Parent root = loader.load();

            ResourceController controller = loader.getController();
            Group group = new Group();
            group.setId(selectedGroupId);
            group.setName("Group");
            controller.setGroup(group);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Group Materials");
            stage.show();

        } catch (Exception e) {
            showAlert("Error: " + e.getMessage());
        }
    }

    private void viewMembers() {
        if (selectedGroupId == null) {
            showAlert("Select group");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/studyplatform/client/views/membership-view.fxml")
            );
            Parent root = loader.load();

            MembershipController controller = loader.getController();
            Group group = new Group();
            group.setId(selectedGroupId);
            group.setName("Group");
            controller.setGroup(group);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Group Members");
            stage.show();

        } catch (Exception e) {
            showAlert("Error: " + e.getMessage());
        }
    }

    private void logout() {
        try {
            apiService.logout();

            //vratit sa k prihlasovaciemu oknu
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/studyplatform/client/views/login.fxml")
            );
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 350));

        } catch (Exception e) {
            showAlert("Logout error: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    public Button getDeleteGroupButton() {
        return deleteGroupButton;
    }

    public void setDeleteGroupButton(Button deleteGroupButton) {
        this.deleteGroupButton = deleteGroupButton;
    }
}