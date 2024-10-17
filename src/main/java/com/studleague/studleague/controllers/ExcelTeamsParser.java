package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.PlayerDTO;
import com.studleague.studleague.dto.TeamDTO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.mappings.PlayerMapper;
import com.studleague.studleague.mappings.TeamMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelTeamsParser {

    /*public List<TeamDTO> teams = new ArrayList<>();
    public List<PlayerDTO> players = new ArrayList<>();
    private TeamMapper teamMapper;
    private PlayerMapper playerMapper;

    public void parseExcel(String filePath) {
        FileInputStream file = null;
        Workbook workbook = null;

        try {
            file = new FileInputStream(new File(filePath));
            workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, TeamDTO> teamMap = new HashMap<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Пропуск заголовков

                // Чтение данных из ячеек
                String idSite = row.getCell(0).getStringCellValue();
                String teamName = row.getCell(1).getStringCellValue();
                //String university = row.getCell(2).getStringCellValue(); // Город

                // Проверяем, существует ли команда
                TeamDTO teamDTO = teamMap.get(idSite);
                if (teamDTO == null) {
                    teamDTO = new TeamDTO(0, teamName, null, 0, idSite); // Укажите leagueId и idSite как нужно
                    teamMap.put(idSite, teamDTO);
                    teams.add(teamDTO);
                }
                //Team team = teamMapper.toEntity(teamDTO);
                // Чтение данных игрока
                String playerIdSite = row.getCell(4).getStringCellValue();
                String surname = row.getCell(5).getStringCellValue();
                String name = row.getCell(6).getStringCellValue();
                String patronymic = row.getCell(7).getStringCellValue();

                // Логика для добавления команды к игроку
                List<Integer> teamIds = new ArrayList<>();
                teamIds.add(Integer.valueOf(idSite));

                PlayerDTO playerDTO = new PlayerDTO(0, name, patronymic, surname, null, playerIdSite,teamIds);
                players.add(playerDTO);
            }
            for (PlayerDTO playerDTO: players)
            {
                String teamIdSite = String.valueOf(playerDTO.getTeamId().get(0));
                Player player = playerMapper.toEntity(playerDTO);
                String teamIdSite = player.getTeams().get(0).;
                player.addTeamToPlayer(teams);
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Файл не найден: " + filePath, ex);
        } catch (IOException ex) {
            throw new RuntimeException("Ошибка при чтении Excel файла", ex);
        } finally {
            // Закрытие ресурсов
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (file != null) {
                    file.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException("Ошибка при закрытии файла", ex);
            }
        }
    }*/
}
