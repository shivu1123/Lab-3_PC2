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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Employeecontroller implements Initializable {
    @FXML
    private TableView<employee> tableView;
    @FXML
    private TableColumn<employee, Integer> EmployeeID;
    @FXML
    private TableColumn<employee, String> Name;
    @FXML
    private TableColumn<employee, String> Address;
    @FXML
    private TableColumn<employee, Integer> Salary;

    @FXML
    private TextField employeeIdField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField salaryField;

    ObservableList<employee> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EmployeeID.setCellValueFactory(new PropertyValueFactory<>("EmployeeID"));
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
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
            int employeeId = Integer.parseInt(employeeIdField.getText());
            String name = nameField.getText();
            String address = addressField.getText();
            int monthlySalary = Integer.parseInt(salaryField.getText());
            int yearlySalary = monthlySalary * 12;

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                String query = "INSERT INTO employee (EmployeeID, Name, Address, Salary) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, employeeId);
                statement.setString(2, name);
                statement.setString(3, address);
                statement.setInt(4, yearlySalary);
                statement.executeUpdate();

                tableView.getItems().add(new employee(employeeId, name, address, yearlySalary));
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print SQL errors
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Handle invalid input
        }
    }

    // Delete button handler
    @FXML
    public void handleDelete(ActionEvent event) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try {
            int employeeId = Integer.parseInt(employeeIdField.getText());

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                String query = "DELETE FROM employee WHERE EmployeeID = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, employeeId);
                statement.executeUpdate();

                tableView.getItems().removeIf(emp -> emp.getId() == employeeId);
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print SQL errors
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Handle invalid input
        }
    }

    // Update button handler
    @FXML
    public void handleUpdate(ActionEvent event) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try {
            int employeeId = Integer.parseInt(employeeIdField.getText());
            String name = nameField.getText();
            String address = addressField.getText();
            int monthlySalary = Integer.parseInt(salaryField.getText());
            int yearlySalary = monthlySalary * 12;

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                String query = "UPDATE employee SET Name = ?, Address = ?, Salary = ? WHERE EmployeeID = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, name);
                statement.setString(2, address);
                statement.setInt(3, yearlySalary);
                statement.setInt(4, employeeId);
                statement.executeUpdate();

                populateTable(); // Refresh table
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print SQL errors
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Handle invalid input
        }
    }

    // Load button handler
    @FXML
    public void handleLoad(ActionEvent event) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try {
            int employeeId = Integer.parseInt(employeeIdField.getText());

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                String query = "SELECT * FROM employee WHERE EmployeeID = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, employeeId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    nameField.setText(resultSet.getString("Name"));
                    addressField.setText(resultSet.getString("Address"));
                    int yearlySalary = resultSet.getInt("Salary");
                    salaryField.setText(String.valueOf(yearlySalary / 12));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print SQL errors
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Handle invalid input
        }
    }

    // Fetch button handler
    @FXML
    public void handleFetch(ActionEvent event) {
        populateTable();
    }

    // Populate table with data
    public void populateTable() {
        list.clear();
        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM employee";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int employeeId = resultSet.getInt("EmployeeID");
                String name = resultSet.getString("Name");
                String address = resultSet.getString("Address");
                int yearlySalary = resultSet.getInt("Salary");

                list.add(new employee(employeeId, name, address, yearlySalary));
            }
            tableView.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace(); // Print SQL errors
        }
    }

    // Back button handler
    public void backbutton(ActionEvent actionEvent) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("dashbord.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Dashboard");
            loginStage.setScene(new Scene(loginPage));

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle I/O errors
        }
    }

    // Clear input fields after each operation
    private void clearFields() {
        employeeIdField.clear();
        nameField.clear();
        addressField.clear();
        salaryField.clear();
    }
}
