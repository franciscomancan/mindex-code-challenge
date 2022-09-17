package com.mindex.challenge.data;

import java.util.Set;

/** Structure to maintain references sufficient for reporting hierarchy.
 *  Operations placed in the Employee service itself (pojo only) */
public class ReportingStructure {

    private Employee employee;
    private int numberOfReports;
    private Set<Employee> employeeReports;

    public ReportingStructure(Employee employee, Set<Employee> reports) {
        this.employee = employee;
        this.employeeReports = reports;
        this.numberOfReports = (reports != null && reports.size() > 0)? reports.size()-1: 0;        // don't count the root employee
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getNumberOfReports() { return numberOfReports; }

    public Set<Employee> getEmployeeReports() {
        return employeeReports;
    }

    public void setEmployeeReports(Set<Employee> employeeReports) {
        this.employeeReports = employeeReports;
    }

}
