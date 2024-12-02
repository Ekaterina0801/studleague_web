package com.studleague.studleague.utils;

import com.studleague.studleague.dto.FullResultDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@Service
public class ExcelProcessor {

    private final ExcelParserService excelParserService;

    public ExcelProcessor(ExcelParserService excelParserService) {
        this.excelParserService = excelParserService;
    }

    public void processExcelFile(File file, Long leagueId, Long tournamentId) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            System.out.println("Reading Excel file content:");
            for (Row row : sheet) {
                for (Cell cell : row) {
                    System.out.print(cell.toString() + "\t");
                }
                System.out.println();
            }

            System.out.println("\nParsing Excel data into DTOs:");

            List<FullResultDTO> results = excelParserService.parseExcelFile(file, leagueId, tournamentId);

            for (FullResultDTO result : results) {
                System.out.println(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

