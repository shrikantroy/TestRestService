package com.test.roy.rest.spring.boot;

import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDAO {
	private static EmployeesList list = new EmployeesList();
    
    static
    {
        list.getEmployeeList().add(new Employee(1, "Lokesh", "Gupta", "howtodoinjava@gmail.com"));
        list.getEmployeeList().add(new Employee(2, "Alex", "Kolenchiskey", "abc@gmail.com"));
        list.getEmployeeList().add(new Employee(3, "David", "Kameron", "titanic@gmail.com"));
    }
    
    public EmployeesList getAllEmployees()
    {
        return list;
    }
     
    public void addEmployee(Employee employee) {
        list.getEmployeeList().add(employee);
    }
}
