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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {
    @FXML
    private TextField EnterID;
    @FXML
    private TextField EnterName;
    @FXML
    private TextField EnterAddress;
    @FXML
    private TextField EnterSalary;
    @FXML
    private Label errorLabel;

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

    ObservableList<employee> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EmployeeID.setCellValueFactory(new PropertyValueFactory<>("EmployeeID"));
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Address.setCellValueFactory(new PropertyValueFactory<>("Address"));
        Salary.setCellValueFactory(new PropertyValueFactory<>("Salary"));

        // Populate the table initially
        populateTable();
    }

    public void clearFields() {
        EnterID.clear();
        EnterName.clear();
        EnterAddress.clear();
        EnterSalary.clear();
        errorLabel.setText("");  // Clear the error label
    }

    public boolean validateFields(String name, String address, String salary) {
        if (name.isEmpty() || address.isEmpty()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Name and Address are required.");
            return false;
        }
        if (!salary.isEmpty() && !salary.matches("\\d+(\\.\\d+)?")) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Salary must be a valid number.");
            return false;
        }
        return true;
    }

    public void insertData(ActionEvent actionEvent) {
        String name = EnterName.getText();
        String address = EnterAddress.getText();
        String salaryStr = EnterSalary.getText();

        if (!validateFields(name, address, salaryStr)) {
            return;
        }

        double monthlySalary = Double.parseDouble(salaryStr);
        double yearlySalary = calculateSalary(monthlySalary);

        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "INSERT INTO employee (Name, Address, Salary) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, address);
            statement.setDouble(3, yearlySalary);
            statement.executeUpdate();

            clearFields();
            populateTable(); // Refresh the table immediately after insertion
        } catch (SQLException e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Database error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateData(ActionEvent actionEvent) {
        String employeeID = EnterID.getText();
        String name = EnterName.getText();
        String address = EnterAddress.getText();
        String salaryStr = EnterSalary.getText();

        if (employeeID.isEmpty() || !validateFields(name, address, salaryStr)) {
            return;
        }

        double monthlySalary = Double.parseDouble(salaryStr);
        double yearlySalary = calculateSalary(monthlySalary);

        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "UPDATE employee SET Name = ?, Address = ?, Salary = ? WHERE EmployeeID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, address);
            statement.setDouble(3, yearlySalary);
            statement.setInt(4, Integer.parseInt(employeeID));
            statement.executeUpdate();

            clearFields();
            populateTable(); // Refresh the table immediately after update
        } catch (SQLException e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Database error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteData(ActionEvent actionEvent) {
        String employeeID = EnterID.getText();

        if (employeeID.isEmpty()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("ID is required for deletion.");
            return;
        }

        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "DELETE FROM employee WHERE EmployeeID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(employeeID));
            statement.executeUpdate();

            clearFields();
            populateTable(); // Refresh the table immediately after deletion
        } catch (SQLException e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Database error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadData(ActionEvent actionEvent) {
        String employeeID = EnterID.getText();

        if (employeeID.isEmpty()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("ID is required to load data.");
            return;
        }

        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM employee WHERE EmployeeID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(employeeID));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                EnterName.setText(resultSet.getString("Name"));
                EnterAddress.setText(resultSet.getString("Address"));
                // Calculate the monthly salary for display
                EnterSalary.setText(String.valueOf(resultSet.getDouble("Salary") / 12));
            } else {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("No record found with ID: " + employeeID);
            }
        } catch (SQLException e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Database error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
                int employeeID = resultSet.getInt("EmployeeID");
                String name = resultSet.getString("Name");
                String address = resultSet.getString("Address");
                int salary = resultSet.getInt("Salary");

                list.add(new employee(employeeID, name, address, salary));
            }
            tableView.setItems(list);
        } catch (SQLException e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Error fetching data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goToDashboard(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("dashbord.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("dashbord");
            loginStage.setScene(new Scene(loginPage));

            // Close the current stage using event source
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Double calculateSalary(Double Salary) {
        Double yearly = 12 * Salary;
        return yearly;
    }
}
