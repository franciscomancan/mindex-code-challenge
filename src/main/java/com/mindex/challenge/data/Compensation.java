package com.mindex.challenge.data;

import java.time.LocalDate;

/** Type/pojo that represents salary and can be associated to an employee. */
public class Compensation {

    String compensationId;
    Employee employee;
    int salary;
    LocalDate effectiveDate;

    public String getCompensationId() { return compensationId; }

    public void setCompensationId(String compensationId) { this.compensationId = compensationId; }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public String toString() {
        return "Compensation{" +
                "compensationId='" + compensationId + '\'' +
                ", employee=" + employee +
                ", salary=" + salary +
                ", effectiveDate=" + effectiveDate +
                '}';
    }
}
