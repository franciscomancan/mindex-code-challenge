package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

import java.util.List;

public interface EmployeeService {
    Employee create(Employee employee);
    Employee read(String id);
    Employee update(Employee employee);
    List<Employee> findAll();
    ReportingStructure getReportingStructure(Employee employee);
    Compensation createCompensation(Compensation employee);
    Compensation readCompensation(Employee employee);
    List<Compensation> findAllCompensation();
}
