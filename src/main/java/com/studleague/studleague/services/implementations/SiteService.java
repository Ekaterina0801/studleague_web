package com.studleague.studleague.services.implementations;


import com.studleague.studleague.dto.*;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.mappings.PlayerMapper;
import com.studleague.studleague.mappings.TeamMapper;
import com.studleague.studleague.mappings.TournamentMapper;
import com.studleague.studleague.services.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



@Service
@RequiredArgsConstructor
public class SiteService {

    private final String URL = "https://api.rating.chgk.net/";
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE3MjgwNzAyMjYsImV4cCI6MTcyODA3MzgyNiwicm9sZXMiOltdLCJ1c2VybmFtZSI6ImRzLmthdHJpbkBtYWlsLnJ1In0.J9sp_b_nlSCQLaHkaN79MFrhMM8HSGTDj_fqhm0-DUNEWA9CakwJinqqCmYJnY0GNKLocd6-sxaKUVz_zpzVcZKAKBDR3purJVdXQ8bVuPTWktOOremEMrJqJGxHGPU6ZyYddsT2oxEbeDyPomiPMZg_GQiR4bdTXmYCxw0c0cYkiS3hHnHsELwlrkJzNU6DL4nZlm4yto8jir9mb51BYIk8rS9jQXgG-8reB8sYf4i_K6GE5STpve3hFfr4C64S9aIu_uQSxcnD8BxLkmLVFHarf0v5BKkjcmgYMNh2ymR-1KHMDyqkoZ58DaXPQEsV1wE6YZZR9PTn94MtqNZpog";

    private final TeamMapper teamMapper;

    private final TournamentMapper tournamentMapper;

    private final TournamentService tournamentService;

    private final PlayerMapper playerMapper;

    private final TeamService teamService;

    private final ResultService resultService;

    private final LeagueService leagueService;

    private final PlayerService playerService;



    public List<TeamDetailsDTO> addTeams(long tournamentId, long leagueId) {
        HttpHeaders headers = createHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        List<TeamDetailsDTO> teams = fetchTeamsFromApi(tournamentId, entity);
        Tournament tournament = fetchOrCreateTournament(tournamentId, leagueId, entity);

        if (teams != null) {
            for (TeamDetailsDTO team : teams) {
                processTeam(tournament, team, leagueId);
            }
        }

        tournamentService.saveTournament(tournament);
        return teams;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        return headers;
    }

    private List<TeamDetailsDTO> fetchTeamsFromApi(long tournamentId, HttpEntity<Void> entity) {
        ResponseEntity<List<TeamDetailsDTO>> responseEntityTeams = restTemplate.exchange(
                URL + "tournaments/" + tournamentId + "/results?includeTeamMembers=1&includeMasksAndControversials=1&includeTeamFlags=0&includeRatingB=0&town=277",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<TeamDetailsDTO>>() {}
        );
        return responseEntityTeams.getBody();
    }

    private Tournament fetchOrCreateTournament(long tournamentId, long leagueId, HttpEntity<Void> entity) {
        Tournament tournament;

        if (!tournamentService.existsByIdSite(tournamentId)) {
            TournamentDto tournamentDto = fetchTournamentFromApi(tournamentId, entity);
            tournamentDto.setIdSite(tournamentDto.getId());
            tournamentDto.setLeagueIds(Collections.singletonList(leagueId));

            tournament = tournamentMapper.toEntity(tournamentDto);
            tournamentService.saveTournament(tournament);
        } else {
            tournament = tournamentService.getTournamentBySiteId(tournamentId);
        }

        return tournament;
    }

    private TournamentDto fetchTournamentFromApi(long tournamentId, HttpEntity<Void> entity) {
        ResponseEntity<TournamentDto> responseEntityTournament = restTemplate.exchange(
                URL + "tournaments/" + tournamentId,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<TournamentDto>() {}
        );
        return responseEntityTournament.getBody();
    }

    private void processTeam(Tournament tournament, TeamDetailsDTO teamDetails, long leagueId) {
        Team teamEntity = mapAndSaveTeam(teamDetails, leagueId);
        List<Player> playersEntity = mapAndSavePlayers(teamDetails.getTeamMembers(), teamEntity, tournament);

        League league = leagueService.getLeagueById(leagueId);
        teamEntity.setLeague(league);

        if (teamDetails.getMask() != null) {
            FullResult fullResult = new FullResult(0, teamEntity, tournament, teamDetails.getMask(), new ArrayList<>());
            resultService.saveFullResult(fullResult);
        }

        tournament.addTeam(teamEntity);
    }

    private Team mapAndSaveTeam(TeamDetailsDTO teamDetails, long leagueId) {
        TeamDTO teamDto = teamDetails.getTeam();
        teamDto.setLeagueId(leagueId);
        teamDto.setIdSite(teamDto.getId());

        Team teamEntity = teamMapper.toEntity(teamDto);
        teamService.saveTeam(teamEntity);

        return teamEntity;
    }

    private List<Player> mapAndSavePlayers(List<TeamMemberDTO> teamMembers, Team teamEntity, Tournament tournament) {
        List<Player> playersEntity = new ArrayList<>();

        for (TeamMemberDTO member : teamMembers) {
            Player playerEntity = findOrCreatePlayer(member.getPlayer());
            teamEntity.addPlayerToTeam(playerEntity);
            tournament.addPlayer(playerEntity);
            playersEntity.add(playerEntity);
        }

        return playersEntity;
    }

    private Player findOrCreatePlayer(PlayerDTO playerDto) {
        if (!playerService.existsByIdSite(playerDto.getId())) {
            PlayerDTO newPlayer = new PlayerDTO(
                    0,
                    playerDto.getName(),
                    playerDto.getPatronymic(),
                    playerDto.getSurname(),
                    null, null, playerDto.getId(),
                    Collections.emptyList()
            );
            Player playerEntity = playerMapper.toEntity(newPlayer);
            playerEntity.setIdSite(playerDto.getId());
            playerService.savePlayer(playerEntity);
            return playerEntity;
        } else {
            return playerService.getPlayerByIdSite(playerDto.getId());
        }
    }


/*    public List<TeamDetailsDTO> addTeams(long tournamentId, long leagueId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<TeamDetailsDTO>> responseEntityTeams = restTemplate.exchange(
                URL + "tournaments/" + tournamentId + "/results?includeTeamMembers=1&includeMasksAndControversials=1&includeTeamFlags=0&includeRatingB=0&town=277",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<TeamDetailsDTO>>() {});

        List<TeamDetailsDTO> teams = responseEntityTeams.getBody();
        Tournament tournament;

        if (!tournamentService.existsByIdSite(tournamentId)) {
            ResponseEntity<TournamentDto> responseEntityTournament = restTemplate.exchange(
                    URL + "tournaments/" + tournamentId,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<TournamentDto>() {});

            TournamentDto tournamentDto1 = responseEntityTournament.getBody();
            List<Long> leagues = new ArrayList<>();
            leagues.add(leagueId);
            tournamentDto1.setLeagueIds(leagues);
            tournamentDto1.setIdSite(tournamentDto1.getId());
            tournament = tournamentMapper.toEntity(tournamentDto1);
            tournamentService.saveTournament(tournament);
        } else {
            tournament = tournamentService.getTournamentBySiteId(tournamentId);
        }

        if (teams != null) {
            for (TeamDetailsDTO team : teams) {
                team.getTeam().setLeagueId(leagueId);
                team.getTeam().setIdSite(team.getTeam().getId());
                Team teamEntity = teamMapper.toEntity(team.getTeam());

                List<Player> playersEntity = new ArrayList<>();
                for (TeamMemberDTO member : team.getTeamMembers()) {
                    Player existingPlayer;
                    if (!playerService.existsByIdSite(member.getPlayer().getId())) {
                        PlayerDTO player = new PlayerDTO(
                                0,
                                member.getPlayer().getName(),
                                member.getPlayer().getPatronymic(),
                                member.getPlayer().getSurname(),
                                null,
                                null,
                                member.getPlayer().getId(),
                                List.of(team.getId())
                        );
                        existingPlayer = playerMapper.toEntity(player);
                        existingPlayer.setIdSite(member.getPlayer().getId());

                        playerService.savePlayer(existingPlayer);
                    } else {
                        existingPlayer = playerService.getPlayerByIdSite(member.getPlayer().getId());
                    }
                    tournament.addPlayer(existingPlayer);
                    teamEntity.addPlayerToTeam(existingPlayer);
                    playersEntity.add(existingPlayer);
                }

                League league = leagueService.getLeagueById(leagueId);
                teamEntity.setLeague(league);
                teamEntity.setId(0);
                teamService.saveTeam(teamEntity);
                if (team.getMask() != null) {
                    FullResult fullResult = new FullResult(0, teamEntity, tournament, team.getMask(), new ArrayList<>());
                    resultService.saveFullResult(fullResult);
                }
                tournament.addTeam(teamEntity);

            }
        }

        tournamentService.saveTournament(tournament);

        return teams;
    }*/


}
