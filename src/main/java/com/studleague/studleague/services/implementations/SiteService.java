package com.studleague.studleague.services.implementations;


import com.studleague.studleague.dto.controversial.ControversialDTO;
import com.studleague.studleague.dto.player.PlayerDTO;
import com.studleague.studleague.dto.team.TeamDTO;
import com.studleague.studleague.dto.team.TeamDetailsDTO;
import com.studleague.studleague.dto.team.TeamMemberDTO;
import com.studleague.studleague.dto.tournament.TournamentDTO;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.mappers.controversial.ControversialMapper;
import com.studleague.studleague.mappers.league.LeagueMainInfoMapper;
import com.studleague.studleague.mappers.league.LeagueMapper;
import com.studleague.studleague.mappers.player.PlayerMapper;
import com.studleague.studleague.mappers.team.TeamMapper;
import com.studleague.studleague.mappers.tournament.TournamentMapper;
import com.studleague.studleague.repository.TeamCompositionRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private final TeamMapper teamMapper;

    private final TournamentMapper tournamentMapper;

    private final TournamentService tournamentService;

    private final PlayerMapper playerMapper;

    private final TeamService teamService;

    private final ResultService resultService;

    private final LeagueService leagueService;

    private final PlayerService playerService;

    private final ControversialMapper controversialMapper;

    private final EntityRetrievalUtils entityRetrievalUtils;

    private final LeagueMapper leagueMapper;

    private final TeamCompositionService teamCompositionService;

    private final LeagueMainInfoMapper leagueMainInfoMapper;

    private final TeamCompositionRepository teamCompositionRepository;

    private final TeamRepository teamRepository;


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
            //tournamentDto.setLeaguesIds(Collections.singletonList(leagueId));
            tournament = tournamentMapper.mapToEntity(tournamentDto);
        } else {
            tournament = tournamentService.getTournamentBySiteId(tournamentId);

        }
        league.addTournamentToLeague(tournament);
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
        teamDetails.getTeam().setLeague(leagueMainInfoMapper.mapToDto(entityRetrievalUtils.getLeagueOrThrow(leagueId)));
        teamDetails.setLeagueId(leagueId);
        teamDetails.getTeam().setIdSite(teamDetails.getTeam().getId());


        Team teamEntity = mapAndSaveTeam(teamDetails);
        List<Player> playersEntity = mapAndSavePlayers(teamDetails.getTeamMembers(), teamEntity, tournament);

        if (teamDetails.getMask() != null) {
            FullResult fullResult = FullResult.builder()
                    .team(teamEntity)
                    .totalScore(Double.valueOf(teamDetails.getQuestionsTotal()))
                    .tournament(tournament)
                    .mask_results(teamDetails.getMask())
                    .build();
            fullResult.setTeam(teamEntity);
            List<Controversial> controversials = new ArrayList<>();
            for (ControversialDTO controversial : teamDetails.getControversials()) {
                controversial.setId(null);
                Controversial controversialEntity = controversialMapper.mapToEntity(controversial);
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
        Long leagueId = teamDetails.getLeagueId();

        // Проверка на существование команды по idSite
        if (!teamRepository.existsByIdSite(idSite)) {
            teamDto.setId(null);
            teamDto.setIdSite(idSite);
            teamEntity = teamMapper.mapToEntity(teamDto);
            teamService.saveTeam(teamEntity);
        } else {
            // Обработка случая, если команда уже существует
            if (teamRepository.existsByIdSite(idSite) &&
                    leagueContainsTeam(leagueId, idSite)) {
                teamEntity = entityRetrievalUtils.getTeamByIdSiteOrThrow(idSite);
                updateTeam(teamEntity, teamDto);
            } else if (teamRepository.existsByTeamNameIgnoreCaseAndLeagueId(teamDto.getTeamName(), leagueId)) {
                teamEntity = entityRetrievalUtils.getTeamByTeamNameIgnoreCaseAndLeagueIdOrThrow(teamDto.getTeamName(), leagueId);
                updateTeam(teamEntity, teamDto);
            } else if (teamDto.getId() != null && teamRepository.existsById(teamDto.getId())) {
                teamEntity = entityRetrievalUtils.getTeamOrThrow(teamDto.getId());
                updateTeam(teamEntity, teamDto);
            } else {

                teamEntity = teamMapper.mapToEntity(teamDto);
                teamService.saveTeam(teamEntity);
            }
        }


        teamEntity.setLeague(entityRetrievalUtils.getLeagueOrThrow(leagueId));

        return teamEntity;
    }

    private boolean leagueContainsTeam(Long leagueId, Long idSite) {
        return teamRepository.existsByIdSiteAndLeagueId(idSite, leagueId);
    }


    @Transactional
    private void updateTeam(Team existingTeam, TeamDTO team) {
        existingTeam.setIdSite(team.getIdSite());
        existingTeam.setUniversity(team.getUniversity());
        teamRepository.save(existingTeam);
    }


    private List<Player> mapAndSavePlayers(List<TeamMemberDTO> teamMembers, Team teamEntity, Tournament tournament) {
        List<Player> playersEntity = new ArrayList<>();

        for (TeamMemberDTO member : teamMembers) {
            Player playerEntity = findOrCreatePlayer(member.getPlayer());
            teamEntity.addPlayerToTeam(playerEntity);
            //tournament.addPlayer(playerEntity);
            playersEntity.add(playerEntity);
        }
        TeamComposition teamComposition = new TeamComposition();
        if (teamCompositionRepository.existsByTournamentIdAndParentTeamId(tournament.getId(), teamEntity.getId())) {
            teamComposition = teamCompositionRepository.findByTournamentIdAndParentTeamId(tournament.getId(), teamEntity.getId()).orElse(null);
            teamComposition.setPlayers(playersEntity);
        } else {
            teamComposition =
                    TeamComposition
                            .builder()
                            .players(playersEntity)
                            .parentTeam(teamEntity)
                            .tournament(tournament)
                            .build();
        }

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
                    Collections.emptyList(), new ArrayList<>(), new ArrayList<>()
            );
            Player playerEntity = playerMapper.mapToEntity(newPlayer);
            playerEntity.setIdSite(playerDto.getId());
            playerService.savePlayer(playerEntity);
            return playerEntity;
        } else {
            return playerService.getPlayerByIdSite(playerDto.getId());
        }
    }


    public TeamDTO addTeamToLeagueFromSite(long leagueId, long teamId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<TeamDTO> responseEntity = restTemplate.exchange(
                URL + "teams/" + teamId,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<TeamDTO>() {
                }
        );

        TeamDTO teamDto = responseEntity.getBody();
        if (teamDto == null) {
            throw new RuntimeException("Не удалось получить данные команды с внешнего API");
        }

        Team team = teamMapper.mapToEntity(teamDto);

        team.setLeague(entityRetrievalUtils.getLeagueOrThrow(leagueId));
        team.setIdSite(teamId);

        teamService.saveTeam(team);

        return teamDto;
    }
}
