package com.example.Blagovest_Anev_employee.initializer;

import com.example.Blagovest_Anev_employee.model.Employee;
import com.example.Blagovest_Anev_employee.reader.FileReader;
import com.example.Blagovest_Anev_employee.service.EmployeeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ApplicationStarter {

    private final FileReader fileReader;
    private final EmployeeService employeeService;

    @PostConstruct
    public void init() {

        Set<Employee> data = fileReader.readEmployeeData();

        employeeService.findLongestWorkingPair(data);

    }

}
