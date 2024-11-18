package com.studleague.studleague.services.implementations;


import com.studleague.studleague.dto.*;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.factory.*;
import com.studleague.studleague.services.EntityRetrievalUtils;
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
    private final RestTemplate restTemplate = new RestTemplate();
    private final String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE3MjgwNzAyMjYsImV4cCI6MTcyODA3MzgyNiwicm9sZXMiOltdLCJ1c2VybmFtZSI6ImRzLmthdHJpbkBtYWlsLnJ1In0.J9sp_b_nlSCQLaHkaN79MFrhMM8HSGTDj_fqhm0-DUNEWA9CakwJinqqCmYJnY0GNKLocd6-sxaKUVz_zpzVcZKAKBDR3purJVdXQ8bVuPTWktOOremEMrJqJGxHGPU6ZyYddsT2oxEbeDyPomiPMZg_GQiR4bdTXmYCxw0c0cYkiS3hHnHsELwlrkJzNU6DL4nZlm4yto8jir9mb51BYIk8rS9jQXgG-8reB8sYf4i_K6GE5STpve3hFfr4C64S9aIu_uQSxcnD8BxLkmLVFHarf0v5BKkjcmgYMNh2ymR-1KHMDyqkoZ58DaXPQEsV1wE6YZZR9PTn94MtqNZpog";

    private final TeamFactory teamFactory;

    private final TournamentFactory tournamentFactory;

    private final TournamentService tournamentService;

    private final PlayerFactory playerFactory;

    private final TeamService teamService;

    private final ResultService resultService;

    private final LeagueService leagueService;

    private final PlayerService playerService;

    private final ControversialFactory controversialFactory;

    private final EntityRetrievalUtils entityRetrievalUtils;

    private final LeagueFactory leagueFactory;


    public List<TeamDetailsDTO> addTeams(Long tournamentId, Long leagueId) {
        HttpHeaders headers = createHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        List<TeamDetailsDTO> teams = fetchTeamsFromApi(tournamentId, entity);
        Tournament tournament = fetchOrCreateTournament(tournamentId, leagueId, entity);

        if (teams != null) {
            for (TeamDetailsDTO team : teams) {
                processAndSaveTeam(tournament, team, leagueId);
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

    private List<TeamDetailsDTO> fetchTeamsFromApi(Long tournamentId, HttpEntity<Void> entity) {
        ResponseEntity<List<TeamDetailsDTO>> responseEntityTeams = restTemplate.exchange(
                URL + "tournaments/" + tournamentId + "/results?includeTeamMembers=1&includeMasksAndControversials=1&includeTeamFlags=0&includeRatingB=0&town=277",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<TeamDetailsDTO>>() {
                }
        );
        return responseEntityTeams.getBody();
    }

    private Tournament fetchOrCreateTournament(Long tournamentId, Long leagueId, HttpEntity<Void> entity) {
        Tournament tournament;
        League league = leagueService.getLeagueById(leagueId);
        if (!tournamentService.existsByIdSite(tournamentId)) {
            TournamentDTO tournamentDto = fetchTournamentFromApi(tournamentId, entity);
            tournamentDto.setIdSite(tournamentDto.getId());
            tournamentDto.setId(null);
            tournamentDto.setLeaguesIds(Collections.singletonList(leagueId));
            tournament = tournamentFactory.mapToEntity(tournamentDto);
            league.addTournamentToLeague(tournament);

        } else {

            tournament = tournamentService.getTournamentBySiteId(tournamentId);
            league.addTournamentToLeague(tournament);
        }
        tournamentService.saveTournament(tournament);
        return tournament;
    }

    private TournamentDTO fetchTournamentFromApi(Long tournamentId, HttpEntity<Void> entity) {
        ResponseEntity<TournamentDTO> responseEntityTournament = restTemplate.exchange(
                URL + "tournaments/" + tournamentId,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<TournamentDTO>() {
                }
        );
        return responseEntityTournament.getBody();
    }

    private void processAndSaveTeam(Tournament tournament, TeamDetailsDTO teamDetails, Long leagueId) {
        teamDetails.getTeam().setLeague(leagueFactory.mapToDto(entityRetrievalUtils.getLeagueOrThrow(leagueId)));
        teamDetails.setLeagueId(leagueId);
        teamDetails.getTeam().setIdSite(teamDetails.getTeam().getId());


        Team teamEntity = mapAndSaveTeam(teamDetails);
        List<Player> playersEntity = mapAndSavePlayers(teamDetails.getTeamMembers(), teamEntity, tournament);

        if (teamDetails.getMask() != null) {
            FullResult fullResult = FullResult.builder()
                    .team(teamEntity)
                    .totalScore(teamDetails.getQuestionsTotal())
                    .tournament(tournament)
                    .mask_results(teamDetails.getMask())
                    .build();
            fullResult.setTeam(teamEntity);
            List<Controversial> controversials = new ArrayList<>();
            for (ControversialDTO controversial : teamDetails.getControversials()) {
                    controversial.setSiteId(null);
                    Controversial controversialEntity = controversialFactory.mapToEntity(controversial);
                    controversialEntity.setFullResult(fullResult);
                    controversials.add(controversialEntity);
                }
            fullResult.setControversials(controversials);
            resultService.saveFullResult(fullResult);
        }
        tournament.addTeam(teamEntity);
    }


    private Team mapAndSaveTeam(TeamDetailsDTO teamDetails) {
        TeamDTO teamDto = teamDetails.getTeam();
        Team teamEntity;
        Long idSite = teamDto.getId();
        if (!teamService.existsByIdSite(idSite)) {
            teamDto.setId(null);
            teamDto.setIdSite(idSite);
            teamEntity = teamFactory.mapToEntity(teamDto);
            teamService.saveTeam(teamEntity);
        } else {
            teamEntity = teamService.getTeamByIdSite(idSite);
            teamEntity.setLeague(entityRetrievalUtils.getLeagueOrThrow(teamDetails.getLeagueId()));
        }

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
        TeamComposition teamComposition =
                TeamComposition
                        .builder()
                        .players(playersEntity)
                        .parentTeam(teamEntity)
                        .tournament(tournament)
                        .build();
        //teamCompositionService.save(teamComposition);
        tournament.addTeamComposition(teamComposition);
        return playersEntity;
    }

    private Player findOrCreatePlayer(PlayerDTO playerDto) {
        if (!playerService.existsByIdSite(playerDto.getId())) {
            PlayerDTO newPlayer = new PlayerDTO(
                    null,
                    playerDto.getName(),
                    playerDto.getPatronymic(),
                    playerDto.getSurname(),
                    null, null, playerDto.getId(),
                    Collections.emptyList(), new ArrayList<>()
            );
            Player playerEntity = playerFactory.mapToEntity(newPlayer);
            playerEntity.setIdSite(playerDto.getId());
            playerService.savePlayer(playerEntity);
            return playerEntity;
        } else {
            return playerService.getPlayerByIdSite(playerDto.getId());
        }
    }
}
