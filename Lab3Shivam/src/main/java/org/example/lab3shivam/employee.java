package org.example.lab3shivam;

import javafx.scene.control.TableColumn;

public class employee {

    private int EmployeeID;
    private String Name;
    private String Address;
    private int Salary;
    public employee(int EmployeeID, String AdminName, String Address, int Salary) {
        this.EmployeeID = EmployeeID;
        this.Name = Name;
        this.Address = Address;
        this.Salary = Salary;
    }

    public employee(TableColumn<employee, Integer> employeeID, String adminName, String address, int salary) {
    }

    public employee(TableColumn<employee, Integer> employeeID, TableColumn<employee, String> name, String address, int salary) {
    }

    public int getId() {
        return EmployeeID;
    }
    public String getName() {
        return Name;
    }
    public String getDoctor() {
        return Address;
    }
    public int getRoom() {
        return Salary;
    }

}
