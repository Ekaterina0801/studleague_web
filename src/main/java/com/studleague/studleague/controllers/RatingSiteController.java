package com.studleague.studleague.controllers;


import com.studleague.studleague.dto.team.TeamDTO;
import com.studleague.studleague.dto.team.TeamDetailsDTO;
import com.studleague.studleague.dto.tournament.TournamentDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.mappers.team.TeamMapper;
import com.studleague.studleague.mappers.tournament.TournamentMapper;
import com.studleague.studleague.services.implementations.SiteService;
import com.studleague.studleague.services.interfaces.TeamService;
import com.studleague.studleague.services.interfaces.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/site-tournaments")
@RequiredArgsConstructor
public class RatingSiteController {

    private final String URL = "https://api.rating.chgk.net/";
    private final RestTemplate restTemplate = new RestTemplate();
    private final String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE3MjgwNzAyMjYsImV4cCI6MTcyODA3MzgyNiwicm9sZXMiOltdLCJ1c2VybmFtZSI6ImRzLmthdHJpbkBtYWlsLnJ1In0.J9sp_b_nlSCQLaHkaN79MFrhMM8HSGTDj_fqhm0-DUNEWA9CakwJinqqCmYJnY0GNKLocd6-sxaKUVz_zpzVcZKAKBDR3purJVdXQ8bVuPTWktOOremEMrJqJGxHGPU6ZyYddsT2oxEbeDyPomiPMZg_GQiR4bdTXmYCxw0c0cYkiS3hHnHsELwlrkJzNU6DL4nZlm4yto8jir9mb51BYIk8rS9jQXgG-8reB8sYf4i_K6GE5STpve3hFfr4C64S9aIu_uQSxcnD8BxLkmLVFHarf0v5BKkjcmgYMNh2ymR-1KHMDyqkoZ58DaXPQEsV1wE6YZZR9PTn94MtqNZpog";
    @Autowired
    private final TournamentMapper tournamentMapper;
    @Autowired
    private final TeamMapper teamMapper;
    @Autowired
    private final TournamentService tournamentService;
    @Autowired
    private final TeamService teamService;
    @Autowired
    private final SiteService siteService;

    /* -------------------------------------------
                      Tournaments
    ------------------------------------------- */

    /**
     * Обрабатывает POST запрос для добавления турнира.
     *
     * @param tournamentDto TournamentDTO, который нужно создать
     * @return ResponseEntity<TournamentDTO>, созданный TournamentDTO
     */
    @Operation(
            summary = "Создать новый турнир",
            description = "Использовать для создания нового турнира в системе"
    )
    @PostMapping("/tournaments")
    public TournamentDTO addTournament(@RequestBody TournamentDTO tournamentDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<TournamentDTO> responseEntityTournament = restTemplate.exchange(URL + "tournaments/"+tournamentDto.getIdSite(), HttpMethod.GET, entity, new ParameterizedTypeReference<TournamentDTO>() {});

        TournamentDTO tournamentDto1 = responseEntityTournament.getBody();
        tournamentDto1.setLeaguesIds(tournamentDto.getLeaguesIds());
        tournamentDto1.setIdSite(tournamentDto1.getId());
        Tournament tournament = tournamentMapper.mapToEntity(tournamentDto1);
        tournamentService.saveTournament(tournament);
        return tournamentDto1;
    }

    /* -------------------------------------------
                      Teams
    ------------------------------------------- */

    /**
     * Обрабатывает POST запрос для добавления команды.
     *
     * @param teamDto TeamDTO, который нужно создать
     * @return ResponseEntity<TeamDTO>, созданный TeamDTO
     */
    @Operation(
            summary = "Создать новую команду",
            description = "Использовать для создания новой команды в системе"
    )
    @PostMapping("/teams")
    public TeamDTO addTeam(@RequestBody TeamDTO teamDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<TeamDTO> responseEntityTeam = restTemplate.exchange(URL + "teams/"+teamDto.getIdSite(), HttpMethod.GET, entity, new ParameterizedTypeReference<TeamDTO>() {});

        TeamDTO teamDto1 = responseEntityTeam.getBody();
        teamDto1.setLeague(teamDto.getLeague());
        teamDto1.setIdSite(teamDto1.getId());
        Team team = teamMapper.mapToEntity(teamDto1);
        teamService.saveTeam(team);
        return teamDto1;
    }

    /* -------------------------------------------
                      Teams by Tournament
    ------------------------------------------- */

    /**
     * Обрабатывает POST запрос для добавления команд в турнир.
     *
     * @param leagueId идентификатор лиги
     * @param tournamentId идентификатор турнира
     * @return ResponseEntity<List<TeamDetailsDTO>>, список команд турнира
     */
    @Operation(
            summary = "Добавить команды в турнир",
            description = "Использовать для добавления команд в конкретный турнир"
    )
    @PostMapping("leagues/{leagueId}/tournaments/{tournamentId}/teams")
    public List<TeamDetailsDTO> addTeams(@PathVariable long leagueId, @PathVariable long tournamentId) {
        return siteService.addTeams(tournamentId, leagueId);
    }
}