package org.example.lab3shivam;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Admincontroller implements Initializable {
    @FXML
    private TableView<admin> tableView;
    @FXML
    private TableColumn<admin, Integer> AdminID;
    @FXML
    private TableColumn<admin, String> AdminName;
    @FXML
    private TableColumn<admin, String> Address;
    @FXML
    private TableColumn<admin, Integer> Salary;

    @FXML
    private TextField adminIdField;
    @FXML
    private TextField adminNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField salaryField;

    ObservableList<admin> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AdminID.setCellValueFactory(new PropertyValueFactory<>("AdminID"));  // Corrected property names
        AdminName.setCellValueFactory(new PropertyValueFactory<>("AdminName"));
        Address.setCellValueFactory(new PropertyValueFactory<>("Address"));
        Salary.setCellValueFactory(new PropertyValueFactory<>("Salary"));
        tableView.setItems(list);
    }

    // Insert button handler
    @FXML
    public void handleInsert(ActionEvent event) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try {
            int adminId = Integer.parseInt(adminIdField.getText());
            String adminName = adminNameField.getText();
            String address = addressField.getText();
            int monthlySalary = Integer.parseInt(salaryField.getText());
            int yearlySalary = monthlySalary * 12;

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                String query = "INSERT INTO admin (AdminId, AdminName, Address, Salary) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, adminId);
                statement.setString(2, adminName);
                statement.setString(3, address);
                statement.setInt(4, yearlySalary);
                statement.executeUpdate();

                tableView.getItems().add(new admin(adminId, adminName, address, yearlySalary));
                clearFields();
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Delete button handler
    @FXML
    public void handleDelete(ActionEvent event) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try {
            int adminId = Integer.parseInt(adminIdField.getText());

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                String query = "DELETE FROM admin WHERE AdminId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, adminId);
                statement.executeUpdate();

                tableView.getItems().removeIf(admin -> admin.getId() == adminId); // Use getId()
                clearFields();
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Update button handler
    @FXML
    public void handleUpdate(ActionEvent event) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try {
            int adminId = Integer.parseInt(adminIdField.getText());
            String adminName = adminNameField.getText();
            String address = addressField.getText();
            int monthlySalary = Integer.parseInt(salaryField.getText());
            int yearlySalary = monthlySalary * 12;

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                String query = "UPDATE admin SET AdminName = ?, Address = ?, Salary = ? WHERE AdminId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, adminName);
                statement.setString(2, address);
                statement.setInt(3, yearlySalary);
                statement.setInt(4, adminId);
                statement.executeUpdate();

                populateTable();
                clearFields();
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Load button handler
    @FXML
    public void handleLoad(ActionEvent event) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try {
            int adminId = Integer.parseInt(adminIdField.getText());

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                String query = "SELECT * FROM admin WHERE AdminId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, adminId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    adminNameField.setText(resultSet.getString("AdminName"));
                    addressField.setText(resultSet.getString("Address"));
                    int yearlySalary = resultSet.getInt("Salary");
                    salaryField.setText(String.valueOf(yearlySalary / 12)); // Convert yearly to monthly for input field
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Fetch button handler
    @FXML
    public void handleFetch(ActionEvent event) {
        populateTable();
    }

    public void populateTable() {
        list.clear();
        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM admin";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int adminId = resultSet.getInt("AdminId");
                String adminName = resultSet.getString("AdminName");
                String address = resultSet.getString("Address");
                int yearlySalary = resultSet.getInt("Salary");

                list.add(new admin(adminId, adminName, address, yearlySalary));
            }
            tableView.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Back button handler
    public void Backbtn(ActionEvent actionEvent) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("dashbord.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Dashboard");
            loginStage.setScene(new Scene(loginPage));

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clear input fields after each operation
    private void clearFields() {
        adminIdField.clear();
        adminNameField.clear();
        addressField.clear();
        salaryField.clear();
    }
}
