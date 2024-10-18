package com.studleague.studleague.controllers;


import com.studleague.studleague.dto.TeamDTO;
import com.studleague.studleague.dto.TeamDetailsDTO;
import com.studleague.studleague.dto.TournamentDto;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.mappings.TeamMapper;
import com.studleague.studleague.mappings.TournamentMapper;
import com.studleague.studleague.services.implementations.SiteServiceImpl;
import com.studleague.studleague.services.interfaces.TeamService;
import com.studleague.studleague.services.interfaces.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/site")
@RequiredArgsConstructor
public class RatingSiteController {

    private final String URL = "https://api.rating.chgk.net/";
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE3MjgwNzAyMjYsImV4cCI6MTcyODA3MzgyNiwicm9sZXMiOltdLCJ1c2VybmFtZSI6ImRzLmthdHJpbkBtYWlsLnJ1In0.J9sp_b_nlSCQLaHkaN79MFrhMM8HSGTDj_fqhm0-DUNEWA9CakwJinqqCmYJnY0GNKLocd6-sxaKUVz_zpzVcZKAKBDR3purJVdXQ8bVuPTWktOOremEMrJqJGxHGPU6ZyYddsT2oxEbeDyPomiPMZg_GQiR4bdTXmYCxw0c0cYkiS3hHnHsELwlrkJzNU6DL4nZlm4yto8jir9mb51BYIk8rS9jQXgG-8reB8sYf4i_K6GE5STpve3hFfr4C64S9aIu_uQSxcnD8BxLkmLVFHarf0v5BKkjcmgYMNh2ymR-1KHMDyqkoZ58DaXPQEsV1wE6YZZR9PTn94MtqNZpog";
    private final TournamentMapper tournamentMapper;
    private final TeamMapper teamMapper;
    private final TournamentService tournamentService;
    private final TeamService teamService;
    private final SiteServiceImpl siteService;

    @PostMapping("/tournaments")
    public TournamentDto addTournament(@RequestBody TournamentDto tournamentDto)
    {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<TournamentDto> responseEntityTournament = restTemplate.exchange(URL + "tournaments/"+tournamentDto.getIdSite(), HttpMethod.GET, entity, new ParameterizedTypeReference<TournamentDto>() {
        });
        TournamentDto tournamentDto1 = responseEntityTournament.getBody();
        tournamentDto1.setLeagueIds(tournamentDto.getLeagueIds());
        tournamentDto1.setIdSite(tournamentDto1.getId());
        Tournament tournament = tournamentMapper.toEntity(tournamentDto1);
        tournamentService.saveTournament(tournament);
        return tournamentDto1;
    }

    @PostMapping("/teams")
    public TeamDTO addTeam(@RequestBody TeamDTO teamDto)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<TeamDTO> responseEntityTeam = restTemplate.exchange(URL + "teams/"+teamDto.getIdSite(), HttpMethod.GET, entity, new ParameterizedTypeReference<TeamDTO>() {
        });
        TeamDTO teamDto1 = responseEntityTeam.getBody();
        teamDto1.setLeagueId(teamDto.getLeagueId());
        teamDto1.setIdSite(teamDto1.getId());
        Team team = teamMapper.toEntity(teamDto1);
        teamService.saveTeam(team);
        return teamDto1;
    }

    @PostMapping("/tournaments/{tournament_id}/teams")
    public List<TeamDetailsDTO> addTeams(@PathVariable int tournament_id, @RequestBody TournamentDto tournamentDto)
    {
        List<TeamDetailsDTO> teams = siteService.addTeams(11189,1);
        return teams;

    }

}
