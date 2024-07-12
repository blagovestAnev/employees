package com.example.Blagovest_Anev_employee.service.impl;

import com.example.Blagovest_Anev_employee.model.Employee;
import com.example.Blagovest_Anev_employee.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MONTHS;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public void findLongestWorkingPair(Set<Employee> employees) {

        Map<Long, List<Employee>> employeesByProject = groupByProject(employees);

        Employee longestWorkingPairEmployee1 = null;
        Employee longestWorkingPairEmployee2 = null;
        long maxMonthsWorkedTogether = 0;

        for (List<Employee> employeesList : employeesByProject.values()) {
            for (int i = 0; i < employeesList.size() - 1; i++) {
                for (int j = i + 1; j < employeesList.size(); j++) {

                    Employee empl1 = employeesList.get(i);
                    Employee empl2 = employeesList.get(j);

                    LocalDate commonWorkStartDate = empl1.getDateFrom().isAfter(empl2.getDateFrom()) ? empl1.getDateFrom() : empl2.getDateFrom();
                    LocalDate commonWorkEndDate = empl1.getDateTo().isBefore(empl2.getDateTo()) ? empl1.getDateTo() : empl2.getDateTo();

                    long monthsWorkedTogether = MONTHS.between(commonWorkStartDate, commonWorkEndDate);

                    if (monthsWorkedTogether > maxMonthsWorkedTogether) {
                        longestWorkingPairEmployee1 = empl1;
                        longestWorkingPairEmployee2 = empl2;
                        maxMonthsWorkedTogether = monthsWorkedTogether;
                    }
                }
            }
        }

        System.out.printf("%s, %s, %s\n", longestWorkingPairEmployee1.getEmployeeId(), longestWorkingPairEmployee2.getEmployeeId(), maxMonthsWorkedTogether);
    }

    private Map<Long, List<Employee>> groupByProject(Set<Employee> employees) { //TODO - to be added logic, if an employee has returned to project - to sum the actual assignment months
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getProjectId))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1) //discarded projects with less than 2 employees
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
