package com.example.Blagovest_Anev_employee.reader;

import com.example.Blagovest_Anev_employee.model.Employee;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileReader {

    @Value("${source-file-path}")
    private String sourceFile;

    private final ResourceLoader resourceLoader;

    public Set<Employee> readEmployeeData() {
        List<String[]> data;
        try (CSVReader reader = createCSVReader()) {
            data = reader.readAll();
        } catch (IOException | CsvException e) {
            log.warn("Error reading CSV data", e); //TODO - this needs to be improved as handling
            data = Collections.emptyList();
        }

        return convert(data);
    }

    private CSVReader createCSVReader() throws IOException {
        return new CSVReaderBuilder(createReader())
                .withSkipLines(1) //ignore header
                .build();
    }

    private Reader createReader() throws IOException {
        Resource resource = resourceLoader.getResource(sourceFile);
        Path filePath = Paths.get(resource.getURI());
        return Files.newBufferedReader(filePath);
    }

    private Set<Employee> convert(List<String[]> employeesData) { //TODO - could be used Mapstruct
        return employeesData.stream()
                .map(row -> new Employee(
                        Long.parseLong(row[0]),
                        Long.parseLong(row[1].trim()),
                        parseDate(row[2].trim()),
                        replaceMissingData(row[3])))
                .collect(Collectors.toSet()); //I don't want equal imports to be assumed as valid
    }

    private LocalDate replaceMissingData(String data) { //TODO - this could be adjusted with library parser, but additional time will be consumed investigating library documentation
        return data.equals(" NULL") ? LocalDate.now() : parseDate(data.trim());
    }

    private LocalDate parseDate(String dateStr) { //TODO - this will be better to be implemented with maybe Chain of responsibilities design pattern
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                DateTimeFormatter.ofPattern("MM-dd-yyyy"),
                DateTimeFormatter.ofPattern("MM dd yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        };
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException ignore) {
                //skipping the error, during the search for appropriate format handler
            }
        }
        throw new IllegalArgumentException("Invalid date format: " + dateStr);
    }

}
