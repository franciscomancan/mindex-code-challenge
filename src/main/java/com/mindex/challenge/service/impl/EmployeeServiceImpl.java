package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null)
            throw new RuntimeException("Invalid employeeId: " + id);

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    /**
     * Generate a reporting structure from a given employee root;
     * provides the entire Employee Set as well as a count member.
     * @param employee
     * @return
     */
    @Override
    public ReportingStructure getReportingStructure(Employee employee) {
        Set<Employee> allReports = new HashSet<>();
        accumulateReports(employee, allReports);
        return new ReportingStructure(employee, allReports);
    }

    /**
     * Generate a distinct structure of reports given a root employee,
     * populating Employee objects during the process.
     * @param emp
     * @param accumulator
     */
    public void accumulateReports(Employee emp, Set<Employee> accumulator) {
        if(emp.getFirstName() == null || emp.getFirstName().isEmpty())
            emp = read(emp.getEmployeeId());

        accumulator.add(emp);
        if(emp.getDirectReports() != null && !emp.getDirectReports().isEmpty())
            emp.getDirectReports()
                    .stream()
                    .forEach(e -> accumulateReports(e, accumulator));
    }

    @Override
    public Compensation createCompensation(Compensation comp) {
        comp.setCompensationId(UUID.randomUUID().toString());
        comp.setEffectiveDate(LocalDate.now());
        return compensationRepository.save(comp);
    }

    @Override
    public Compensation readCompensation(Employee employee) {
        return compensationRepository.findByEmployee(employee);
    }

    @Override
    public List<Compensation> findAllCompensation() {
        return compensationRepository.findAll();
    }
}
