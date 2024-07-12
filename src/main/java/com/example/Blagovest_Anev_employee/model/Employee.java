package com.example.Blagovest_Anev_employee.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class Employee {

    private long employeeId;
    private long projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

}

