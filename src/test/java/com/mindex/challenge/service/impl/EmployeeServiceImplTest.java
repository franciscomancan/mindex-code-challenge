package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportsUrl;
    private String reportsCountUrl;
    private String salaryListUrl;
    private String salaryCreateUrl;
    private String salaryReadUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportsUrl = employeeUrl + "/reports/{id}";
        reportsCountUrl = employeeUrl + "/reports/count/{id}";
        salaryCreateUrl = employeeUrl + "/compensation";
        salaryReadUrl = salaryCreateUrl + "/{id}";
        salaryListUrl = salaryCreateUrl + "/list";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }


    /** Additional testing added as of application mods, testing new components, retaining existing style */
    @Test
    public void testReportsAndCompensation() {
        Employee existingEmp = new Employee();
        existingEmp.setEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");  // John Lennon
        existingEmp.setFirstName("John");
        existingEmp.setLastName("Lennon");
        existingEmp.setPosition("Development Manager");
        existingEmp.setDepartment("Engineering");
        Employee testEmp = restTemplate.getForEntity(employeeIdUrl, Employee.class, existingEmp.getEmployeeId()).getBody();
        assertNotNull(existingEmp.getEmployeeId());
        assertEmployeeEquivalence(existingEmp, testEmp);

        Employee[] reports = restTemplate.getForEntity(reportsUrl, Employee[].class, testEmp.getEmployeeId()).getBody();
        Arrays.stream(reports).forEach(r -> System.out.println(r));
        // root employee included in structure
        assertEquals(5, reports.length);

        // count endpoint adjusts for reports only count
        int reportsCount = restTemplate.getForEntity(reportsCountUrl, Integer.class, testEmp.getEmployeeId()).getBody();
        assertEquals(4, reportsCount);

        Compensation testSalary = new Compensation();
        testSalary.setSalary(10000000);
        testSalary.setEmployee(testEmp);
        Compensation newSalary = restTemplate.postForEntity(salaryCreateUrl, testSalary, Compensation.class).getBody();
        assertEquals(testSalary.getSalary(), newSalary.getSalary());
        assertEquals(testSalary.getEmployee(), newSalary.getEmployee());

        Compensation[] salaries = restTemplate.getForEntity(salaryListUrl, Compensation[].class).getBody();
        assertNotNull(salaries);

        Compensation readSalary = restTemplate.getForEntity(salaryReadUrl, Compensation.class, testEmp.getEmployeeId()).getBody();
        assertEquals(readSalary.getEmployee(), testEmp);
        assertEquals(readSalary.getSalary(), testSalary.getSalary());
    }
}
