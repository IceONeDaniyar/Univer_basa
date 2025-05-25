package com.example.university.service;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.university.model.Student;

import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ExcelExporter {
    private List<Student> studentList;

    public ExcelExporter(List<Student> studentList) {
        this.studentList = studentList;
    }

    public void export(HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Students");

        writeHeaderRow(sheet);
        writeDataRows(sheet);

        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void writeHeaderRow(XSSFSheet sheet) {
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("Имя");
        row.createCell(2).setCellValue("Email");
        row.createCell(3).setCellValue("Группа");
        row.createCell(4).setCellValue("Оценка");
    }

    private void writeDataRows(XSSFSheet sheet) {
        int rowCount = 1;

        for (Student student : studentList) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(student.getId() == null ? 0 : student.getId());
            row.createCell(1).setCellValue(student.getName() == null ? "" : student.getName());
            row.createCell(2).setCellValue(student.getEmail() == null ? "" : student.getEmail());
            row.createCell(3).setCellValue(student.getGroupName() == null ? "" : student.getGroupName());
            row.createCell(4).setCellValue(student.getGrade() == null ? "" : student.getGrade());
        }
    }
}
