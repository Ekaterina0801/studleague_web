package com.studleague.studleague.services.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.studleague.studleague.dao.interfaces.TournamentDao;
import com.studleague.studleague.dto.*;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.mappings.PlayerMapper;
import com.studleague.studleague.mappings.TeamMapper;
import com.studleague.studleague.mappings.TournamentMapper;
import com.studleague.studleague.services.interfaces.*;
import groovy.lang.GString;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
public class SiteServiceImpl {

    private final String URL = "https://api.rating.chgk.net/";
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE3MjgwNzAyMjYsImV4cCI6MTcyODA3MzgyNiwicm9sZXMiOltdLCJ1c2VybmFtZSI6ImRzLmthdHJpbkBtYWlsLnJ1In0.J9sp_b_nlSCQLaHkaN79MFrhMM8HSGTDj_fqhm0-DUNEWA9CakwJinqqCmYJnY0GNKLocd6-sxaKUVz_zpzVcZKAKBDR3purJVdXQ8bVuPTWktOOremEMrJqJGxHGPU6ZyYddsT2oxEbeDyPomiPMZg_GQiR4bdTXmYCxw0c0cYkiS3hHnHsELwlrkJzNU6DL4nZlm4yto8jir9mb51BYIk8rS9jQXgG-8reB8sYf4i_K6GE5STpve3hFfr4C64S9aIu_uQSxcnD8BxLkmLVFHarf0v5BKkjcmgYMNh2ymR-1KHMDyqkoZ58DaXPQEsV1wE6YZZR9PTn94MtqNZpog";

    private final TeamMapper teamMapper;

    private final TournamentMapper tournamentMapper;

    private final TournamentService tournamentService;

    private final PlayerMapper playerMapper;

    private final TeamService teamService;

    private final FullResultService fullResultService;

    private final LeagueService leagueService;

    private final PlayerService playerService;



    public List<TeamDetailsDTO> addTeams(long tournamentId, long leagueId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<TeamDetailsDTO>> responseEntityTeams = restTemplate.exchange(
                URL + "tournaments/" + tournamentId + "/results?includeTeamMembers=1&includeMasksAndControversials=1&includeTeamFlags=0&includeRatingB=0",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<TeamDetailsDTO>>() {});

        List<TeamDetailsDTO> teams = responseEntityTeams.getBody();
        Tournament tournament = tournamentService.getTournamentBySiteId(tournamentId);

        if (tournament.getId() == 0) {
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
        }

        if (teams != null) {
            for (TeamDetailsDTO team : teams) {
                team.setLeagueId(leagueId);
                team.setIdSite(team.getTeam().getId());

                List<Player> playersEntity = new ArrayList<>();
                for (TeamMemberDTO member : team.getTeamMembers()) {
                    // Поиск существующего игрока
                    Player existingPlayer = playerService.getPlayerByIdSite(String.valueOf(member.getPlayer().getId()));
                    if (existingPlayer.getId()==0) {
                        // Если игрок не найден, создаем нового
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
                        tournament.addPlayer(existingPlayer);
                    }
                    playersEntity.add(existingPlayer);
                }

                League league = leagueService.getLeagueById(team.getLeagueId());

                Team teamEntity = teamService.getTeamByIdSite(team.getTeam().getId());

                teamEntity.setLeague(league);

                if (team.getMask() != null) {
                    FullResult fullResult = new FullResult(0, teamEntity, tournament, team.getMask(), new ArrayList<>());
                    fullResultService.saveFullResult(fullResult);
                }

                tournament.addTeam(teamEntity);
                if (teamEntity.getTeamName()==null)
                {
                    teamEntity.setPlayers(playersEntity);
                    TeamDTO teamInDetails = team.getTeam();
                    teamEntity.setIdSite(teamInDetails.getId());
                    teamEntity.setTeamName(String.valueOf(teamInDetails.getTeamName()));
                }
                else
                {
                    for (Player player: playersEntity)
                    {
                        teamService.addPlayerToTeam(teamEntity.getId(),player.getId());
                    }
                }
                teamService.saveTeam(teamEntity);
                tournamentService.saveTournament(tournament);
            }
        }

        return teams;
    }

}


/*@Service
@RequiredArgsConstructor
public class SiteServiceImpl {

    private static final String URL = "https://api.rating.chgk.net/";
    private final RestTemplate restTemplate = new RestTemplate();
    private final String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE3MjgwNzAyMjYsImV4cCI6MTcyODA3MzgyNiwicm9sZXMiOltdLCJ1c2VybmFtZSI6ImRzLmthdHJpbkBtYWlsLnJ1In0.J9sp_b_nlSCQLaHkaN79MFrhMM8HSGTDj_fqhm0-DUNEWA9CakwJinqqCmYJnY0GNKLocd6-sxaKUVz_zpzVcZKAKBDR3purJVdXQ8bVuPTWktOOremEMrJqJGxHGPU6ZyYddsT2oxEbeDyPomiPMZg_GQiR4bdTXmYCxw0c0cYkiS3hHnHsELwlrkJzNU6DL4nZlm4yto8jir9mb51BYIk8rS9jQXgG-8reB8sYf4i_K6GE5STpve3hFfr4C64S9aIu_uQSxcnD8BxLkmLVFHarf0v5BKkjcmgYMNh2ymR-1KHMDyqkoZ58DaXPQEsV1wE6YZZR9PTn94MtqNZpog";

    private final TeamMapper teamMapper;
    private final TournamentMapper tournamentMapper;
    private final TournamentService tournamentService;
    private final PlayerMapper playerMapper;
    private final TeamService teamService;
    private final FullResultService fullResultService;
    private final LeagueService leagueService;
    private final PlayerService playerService;
    private final RatingApiService ratingApiService;

    public List<TeamDetailsDTO> addTeams(String tournamentId, String leagueId) {
        List<TeamDetailsDTO> teams = ratingApiService.fetchTeams(tournamentId);
        Tournament tournament = checkAndFetchTournament(tournamentId, leagueId);

        if (teams != null) {
            for (TeamDetailsDTO team : teams) {
                processTeam(team, leagueId, tournament);
            }
        }

        return teams;
    }



    private Tournament checkAndFetchTournament(String tournamentId, String leagueId) {
        Tournament tournament = tournamentService.getTournamentBySiteId(tournamentId);

        if (tournament.getId() == 0) {
            tournament = fetchAndSaveTournament(tournamentId, leagueId);
        }

        return tournament;
    }

    private Tournament fetchAndSaveTournament(String tournamentId, String leagueId) {
        HttpHeaders headers = ratingApiService.createHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<TournamentDto> responseEntityTournament = restTemplate.exchange(
                URL + "tournaments/" + tournamentId,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<TournamentDto>() {}
        );

        TournamentDto tournamentDto = responseEntityTournament.getBody();
        List<Integer> leagues = List.of(Integer.valueOf(leagueId)); // Using List.of for immutability.
        tournamentDto.setLeagueIds(leagues);
        tournamentDto.setIdSite(String.valueOf(tournamentDto.getId()));

        Tournament tournament = tournamentMapper.toEntity(tournamentDto);
        tournamentService.saveTournament(tournament);

        return tournament;
    }

    private void processTeam(TeamDetailsDTO team, String leagueId, Tournament tournament) {
        team.setLeagueId(Integer.valueOf(leagueId));
        team.setIdSite(team.getTeam().getId());

        List<Player> playersEntity = processPlayers(team, tournament);
        League league = leagueService.getLeagueById(team.getLeagueId());
        Team teamEntity = teamService.getTeamByIdSite(String.valueOf(team.getTeam().getId()));

        teamEntity.setLeague(league);

        if (team.getMask() != null) {
            saveFullResult(teamEntity, tournament, team.getMask());
        }

        tournament.addTeam(teamEntity);
        updateTeamEntity(teamEntity, team, playersEntity);

        teamService.saveTeam(teamEntity);
        tournamentService.saveTournament(tournament);
    }

    private List<Player> processPlayers(TeamDetailsDTO team, Tournament tournament) {
        List<Player> playersEntity = new ArrayList<>();

        for (TeamMemberDTO member : team.getTeamMembers()) {
            Player existingPlayer = playerService.getPlayerByIdSite(String.valueOf(member.getPlayer().getId()));
            if (existingPlayer.getId() == 0) {
                existingPlayer = createNewPlayer(member, tournament);
            }
            playersEntity.add(existingPlayer);
        }

        return playersEntity;
    }

    private Player createNewPlayer(TeamMemberDTO member, Tournament tournament) {
        PlayerDTO playerDTO = new PlayerDTO(
                0,
                member.getPlayer().getName(),
                member.getPlayer().getPatronymic(),
                member.getPlayer().getSurname(),
                null, null,
                String.valueOf(member.getPlayer().getId()),
                List.of(tournament.getId()) // Assuming tournament has an ID to link
        );
        Player newPlayer = playerMapper.toEntity(playerDTO);
        newPlayer.setIdSite(String.valueOf(member.getPlayer().getId()));
        tournament.addPlayer(newPlayer);
        return newPlayer;
    }

    private void saveFullResult(Team teamEntity, Tournament tournament, String mask) {
        FullResult fullResult = new FullResult(0, teamEntity, tournament, mask);
        fullResultService.saveFullResult(fullResult);
    }

    private void updateTeamEntity(Team teamEntity, TeamDetailsDTO team, List<Player> playersEntity) {
        if (teamEntity.getTeamName() == null) {
            teamEntity.setPlayers(playersEntity);
            TeamDTO teamInDetails = team.getTeam();
            teamEntity.setIdSite(String.valueOf(teamInDetails.getId()));
            teamEntity.setTeamName(String.valueOf(teamInDetails.getTeamName()));
        } else {
            for (Player player : playersEntity) {
                teamService.addPlayerToTeam(teamEntity.getId(), player.getId());
            }
        }
    }
}*/
