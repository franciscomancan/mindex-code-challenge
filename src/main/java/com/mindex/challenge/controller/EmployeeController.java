package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);
        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);
        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    /** Added to ensure the service was working from browser */
    @GetMapping("/employee/list")
    public List<Employee> listEmployees() {
        LOG.debug("Fetching all Employees");
        return employeeService.findAll();
    }

    /** Fetch the list of reports for a given employee, including the employee itself,
     *  in no specific order, but populated with hierarchichal info (employees fully populated). */
    @GetMapping("/employee/reports/{id}")
    public List<Employee> employeeReports(@PathVariable String id) {
        LOG.debug("Fetching all Reports for employee {}", id);
        ReportingStructure rs = employeeService.getReportingStructure(employeeService.read(id));
        return rs.getEmployeeReports().stream().collect(Collectors.toList());
    }

    /** Return the number of reports for a given employee as per guidelines */
    @GetMapping("/employee/reports/count/{id}")
    public int employeeReportsCount(@PathVariable String id) {
        LOG.debug("Counting Employee reports for employee {}", id);
        ReportingStructure rs = employeeService.getReportingStructure(employeeService.read(id));
        return rs.getNumberOfReports();
    }

    /** Create a compensation object and associate to an employee by employeeId, assumed
     *  to be populated in the payload. */
    @PostMapping("/employee/compensation")
    public Compensation create(@RequestBody Compensation compensation) {
        LOG.debug("Received compensation create request [{}]", compensation);
        return employeeService.createCompensation(compensation);
    }

    /** Read the compensation of an employee given the employee's GUID */
    @GetMapping("/employee/compensation/{empId}")
    public Compensation readCompensation(@PathVariable String empId) {
        LOG.debug("Reading compensation of employee {}", empId);
        Employee input = employeeService.read(empId);
        return employeeService.readCompensation(input);
    }

    /** Listing for curious minds.. */
    @GetMapping("/employee/compensation/list")
    public List<Compensation> listCompensation() {
        LOG.debug("Fetching list of Compensation");
        return employeeService.findAllCompensation();
    }
}
