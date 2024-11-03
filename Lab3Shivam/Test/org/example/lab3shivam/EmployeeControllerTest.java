package org.example.lab3shivam;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeControllerTest {

    @Test
    void calculateSalary() {

        EmployeeController x = new EmployeeController();

        assertEquals(x.calculateSalary(5000.00),  70000);
    }
}