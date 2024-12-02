package com.studleague.studleague.controllers;

import com.studleague.studleague.utils.ExcelProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private final ExcelProcessor excelProcessor;

    public FileUploadController(ExcelProcessor excelProcessor) {
        this.excelProcessor = excelProcessor;
    }

    @PostMapping("/upload-results")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("leagueId") Long leagueId, @RequestParam("tournamentId") Long tournamentId) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload.");
            }

            File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(tempFile);

            System.out.println("Processing file: " + tempFile.getAbsolutePath());
            excelProcessor.processExcelFile(tempFile, leagueId, tournamentId);

            return ResponseEntity.ok("File processed successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File processing failed.");
        }
    }
}
