package com.studyplatform.client.controller;

import com.studyplatform.client.model.Group;
import com.studyplatform.client.model.Membership;
import com.studyplatform.client.service.ApiService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class MembershipController {
    @FXML private ListView<String> membersListView;
    @FXML private Label groupLabel;
    @FXML private Button addMemberButton;
    @FXML private Button removeMemberButton;
    @FXML private Button makeAdminButton;
    @FXML private Button backButton;

    private ApiService apiService = ApiService.getInstance();
    private Long currentGroupId;
    private ObservableList<Membership> memberships = FXCollections.observableArrayList();

    public void setGroup(Group group) {
        this.currentGroupId = group.getId();
        groupLabel.setText("Members: " + group.getName());
        loadMembers();
    }

    @FXML
    private void initialize() {
        setupListView();
    }

    private void setupListView() {
        membersListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    boolean hasSelection = newVal != null;
                    removeMemberButton.setDisable(!hasSelection);
                    makeAdminButton.setDisable(!hasSelection);
                }
        );

        removeMemberButton.setDisable(true);
        makeAdminButton.setDisable(true);
    }

    private void loadMembers() {
        try {
            System.out.println("[DEBUG] Loading members for group ID: " + currentGroupId);
            List<Membership> loaded = apiService.getGroupMembers(currentGroupId);
            System.out.println("[DEBUG] Loaded " + loaded.size() + " members");

            memberships.setAll(loaded);
            updateListView();

        } catch (Exception e) {
            System.err.println("[ERROR] Failed to load members: " + e.getMessage());
            e.printStackTrace();
            showAlert("Loading error: " + e.getMessage());

            // Временно оставь моки для тестирования UI
            //List<Membership> mockMembers = createMockMembers();
            //memberships.setAll(mockMembers);
            //updateListView();
        }
    }

    /*private List<Membership> createMockMembers() {
        Membership m1 = new Membership();
        m1.setUserId(1L);
        m1.setUserName("Ivan Ivanov");
        m1.setRole("ADMIN");

        Membership m2 = new Membership();
        m2.setUserId(2L);
        m2.setUserName("Petr Petrov");
        m2.setRole("MEMBER");

        Membership m3 = new Membership();
        m3.setUserId(3L);
        m3.setUserName("Maria Sidorova");
        m3.setRole("MEMBER");

        return List.of(m1, m2, m3);
    }*/

    private void updateListView() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Membership m : memberships) {
            String roleIcon = "ADMIN".equals(m.getRole()) ? "👑 " : "👤 ";
            items.add(roleIcon + m.getUserName() + " (" + m.getRoleDisplay() + ")");
        }
        membersListView.setItems(items);
    }

    @FXML
    private void addMember() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Member");
        dialog.setHeaderText("Add user to group");
        dialog.setContentText("Enter user email:");

        dialog.showAndWait().ifPresent(email -> {
            if (!email.trim().isEmpty()) {
                try {
                    //addmember
                    showAlert("User added: " + email);
                    loadMembers();
                } catch (Exception e) {
                    showAlert("Error: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void removeMember() {
        int index = membersListView.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < memberships.size()) {
            Membership member = memberships.get(index);

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Removal");
            confirm.setContentText("Remove '" + member.getUserName() + "' from group?");

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                try {
                    //remover member apiservice
                    showAlert("Member removed: " + member.getUserName());
                    loadMembers();
                } catch (Exception e) {
                    showAlert("Removal error: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void makeAdmin() {
        int index = membersListView.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < memberships.size()) {
            Membership member = memberships.get(index);

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Make Admin");
            confirm.setContentText("Make '" + member.getUserName() + "' administrator?");

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                try {
                    //changememberrole
                    member.setRole("ADMIN");
                    updateListView();
                    showAlert("Now administrator: " + member.getUserName());
                } catch (Exception e) {
                    showAlert("Error: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void goBack() {
        try {
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