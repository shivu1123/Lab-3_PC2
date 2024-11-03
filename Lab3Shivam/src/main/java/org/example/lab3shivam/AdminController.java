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

public class AdminController implements Initializable {
    @FXML
    private TextField enterid;
    @FXML
    private TextField entername;
    @FXML
    private TextField enteraddress;
    @FXML
    private TextField entersalary;
    @FXML
    private Label errorLabel;

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

    ObservableList<admin> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AdminID.setCellValueFactory(new PropertyValueFactory<>("AdminID"));
        AdminName.setCellValueFactory(new PropertyValueFactory<>("AdminName"));
        Address.setCellValueFactory(new PropertyValueFactory<>("Address"));
        Salary.setCellValueFactory(new PropertyValueFactory<>("Salary"));
        populateTable();  // Load data into the table on initialization
    }

    public void clearFields() {
        enterid.clear();
        entername.clear();
        enteraddress.clear();
        entersalary.clear();
        errorLabel.setText("");  // Clear the error label
    }

    public boolean validateFields(String AdminID, String AdminName, String Address, String Salary) {
        if (AdminID.isEmpty() || AdminName.isEmpty() || Address.isEmpty()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("All fields are required.");
            return false;
        }
        if (!Salary.isEmpty() && !Salary.matches("\\d+")) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Salary must be a number.");
            return false;
        }
        return true;
    }

    public void insertdata(ActionEvent actionEvent) {
        String AdminName = entername.getText();
        String address = enteraddress.getText();
        String salary = entersalary.getText();

        if (AdminName.isEmpty() && address.isEmpty() && salary.isEmpty()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Name, Address, and Salary are required.");
            return;
        }

        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "INSERT INTO admin (AdminName, address, salary) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, AdminName);
            statement.setString(2, address);
            statement.setInt(3, Integer.parseInt(salary));
            statement.executeUpdate();

            clearFields();
            populateTable();  // Refresh the table after insertion
        } catch (SQLException e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Database error occurred.");
            e.printStackTrace();
        }
    }

    public void updateData(ActionEvent actionEvent) {
        String AdminID = enterid.getText();
        String AdminName = entername.getText();
        String address = enteraddress.getText();
        String salary = entersalary.getText();

        if (!validateFields(AdminID, AdminName, address, salary)) {
            return;
        }

        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "UPDATE admin SET AdminName = ?, address = ?, salary = ? WHERE AdminID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, AdminName);
            statement.setString(2, address);
            statement.setInt(3, Integer.parseInt(salary));
            statement.setInt(4, Integer.parseInt(AdminID));
            statement.executeUpdate();

            clearFields();
            populateTable();  // Refresh the table after update
        } catch (SQLException e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Database error occurred.");
            e.printStackTrace();
        }
    }

    public void deleteData(ActionEvent actionEvent) {
        String AdminID = enterid.getText();

        if (AdminID.isEmpty()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("ID is required for deletion.");
            return;
        }

        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "DELETE FROM admin WHERE AdminID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(AdminID));
            statement.executeUpdate();

            clearFields();
            populateTable();  // Refresh the table after deletion
        } catch (SQLException e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Database error occurred.");
            e.printStackTrace();
        }
    }

    public void loadData(ActionEvent actionEvent) {
        String AdminID = enterid.getText();

        if (AdminID.isEmpty()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("ID is required to load data.");
            return;
        }

        String jdbcUrl = "jdbc:mysql://localhost:3306/db-lab3";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM admin WHERE AdminID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(AdminID));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                entername.setText(resultSet.getString("AdminName"));
                enteraddress.setText(resultSet.getString("address"));
                entersalary.setText(resultSet.getString("salary"));
            } else {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("No record found with ID: " + AdminID);
            }
        } catch (SQLException e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Database error occurred.");
            e.printStackTrace();
        }
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
                int AdminID = resultSet.getInt("AdminID");
                String AdminName = resultSet.getString("AdminName");
                String address = resultSet.getString("address");
                int salary = resultSet.getInt("salary");

                list.add(new admin(AdminID, AdminName, address, salary));
            }
            tableView.setItems(list);
        } catch (SQLException e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Error fetching data.");
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
}
