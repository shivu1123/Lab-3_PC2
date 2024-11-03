package org.example.lab3shivam;

public class employee {
    private int EmployeeID;
    private String Name;
    private String Address;
    private int Salary;

    // Constructor
    public employee(int EmployeeID, String Name, String Address, int Salary) {
        this.EmployeeID = EmployeeID;
        this.Name = Name;
        this.Address = Address;
        this.Salary = Salary;
    }

    // Getters and Setters
    public int getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(int EmployeeID) {
        this.EmployeeID = EmployeeID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public int getSalary() {
        return Salary;
    }

    public void setSalary(int Salary) {
        this.Salary = Salary;
    }
}
