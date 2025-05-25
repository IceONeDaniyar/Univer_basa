package com.example.university.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import com.example.university.model.Student;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelImporter {

    public List<Student> importStudents(InputStream is) throws IOException {
        List<Student> students = new ArrayList<>();

        XSSFWorkbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        int rowCount = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rowCount; i++) { // Пропускаем заголовок
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Student student = new Student();
            student.setName(getCellValueAsString(row.getCell(1)));
            student.setEmail(getCellValueAsString(row.getCell(2)));
            student.setGroupName(getCellValueAsString(row.getCell(3)));
            student.setGrade(getCellValueAsString(row.getCell(4)));

            students.add(student);
        }

        workbook.close();
        return students;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        CellType type = cell.getCellType();

        switch (type) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                double d = cell.getNumericCellValue();
                if (d == (long) d) {
                    return String.valueOf((long) d);
                } else {
                    return String.valueOf(d);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
