package com.studleague.studleague.controllers;

import com.studleague.studleague.services.implementations.TeamResultService;
import com.studleague.studleague.services.interfaces.TeamCompositionService;
import com.studleague.studleague.utils.ExcelParser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Загрузчик файлов")
public class FileUploadController {


    private final TeamResultService teamResultService;

    private final ExcelParser excelParser;

    private final TeamCompositionService teamCompositionService;

    public FileUploadController(
            TeamResultService teamResultService,
            ExcelParser excelParser,
            TeamCompositionService teamCompositionService) {
        this.teamResultService = teamResultService;
        this.excelParser = excelParser;
        this.teamCompositionService = teamCompositionService;
    }

    @PostMapping("/upload-results")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("leagueId") Long leagueId, @RequestParam("tournamentId") Long tournamentId) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload.");
            }

            File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(tempFile);
            teamResultService.processTeamResults(excelParser.parseExcelFile(tempFile), leagueId, tournamentId);

            return ResponseEntity.ok("File processed successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File processing failed.");
        }
    }

    @PostMapping("/upload-compositions")
    public ResponseEntity<?> uploadFileCompositions(@RequestParam("file") MultipartFile file, @RequestParam("leagueId") Long leagueId, @RequestParam("tournamentId") Long tournamentId) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload.");
            }

            File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(tempFile);

            // Используем парсер для получения данных в виде List<Map<String, Object>>
            List<Map<String, Object>> data = excelParser.parseExcelFile(tempFile);
            teamCompositionService.processTeamCompositions(data, leagueId, tournamentId);

            return ResponseEntity.ok("File processed successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File processing failed.");
        }
    }

}
