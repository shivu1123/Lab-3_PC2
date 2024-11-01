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

    public int getId() {
        return AdminID;  // Change this to getAdminId if you prefer
    }

    public String getName() {
        return AdminName;  // Change this to getAdminName if you prefer
    }

    public String getAddress() {  // Changed from getDoctor() to getAddress()
        return Address;
    }

    public int getSalary() {  // Changed from getRoom() to getSalary()
        return Salary;
    }
}
