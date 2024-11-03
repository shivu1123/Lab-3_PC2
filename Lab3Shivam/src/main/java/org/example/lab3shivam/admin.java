package org.example.lab3shivam;

public class admin {
    private int AdminID;
    private String AdminName;
    private String Address;
    private int Salary;

    public admin(int AdminID, String AdminName, String Address, int Salary) {
        this.AdminID = AdminID;
        this.AdminName = AdminName;
        this.Address = Address;
        this.Salary = Salary;
    }

    public int getAdminID() {
        return AdminID;
    }

    public void setAdminID(int AdminID) {
        this.AdminID = AdminID;
    }

    public String getAdminName() {
        return AdminName;
    }

    public void setAdminName(String AdminName) {
        this.AdminName = AdminName;
    }

    public String getAddress() {
        return Address;
    }

    public void getAddress(String Address) {
        this.Address = Address;
    }

    public int getSalary() {
        return Salary;
    }

    public void setSalary(int Salary) {
        this.Salary = Salary;
    }
}
