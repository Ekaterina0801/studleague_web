package com.studleague.studleague.services;

import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.factory.TournamentFactory;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.TournamentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TournamentServiceTest {

    @Autowired
    private TournamentService tournamentService;

    @MockBean
    private TournamentRepository tournamentRepository;

    @MockBean
    private ResultRepository resultRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private TournamentFactory tournamentFactory;

    @MockBean
    private LeagueRepository leagueRepository;

    @MockBean
    private EntityRetrievalUtils entityRetrievalUtils;

    @MockBean
    private LeagueService leagueService;

    @Test
    void getTournamentById_shouldReturnTournament_whenExists() {
        Long id = 1L;
        Tournament tournament = new Tournament();
        tournament.setId(id);

        when(entityRetrievalUtils.getTournamentOrThrow(id)).thenReturn(tournament);

        Tournament result = tournamentService.getTournamentById(id);

        assertEquals(tournament, result);
        verify(entityRetrievalUtils).getTournamentOrThrow(id);
    }

    @Test
    void getAllTournaments_shouldReturnListOfTournaments() {
        List<Tournament> tournaments = List.of(new Tournament(), new Tournament());
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        List<Tournament> result = tournamentService.getAllTournaments();

        assertEquals(tournaments, result);
        verify(tournamentRepository).findAll();
    }

    @Test
    void saveTournament_shouldSaveNewTournament_whenNotExists() {
        Tournament tournament = new Tournament();
        tournament.setIdSite(1L);
        when(tournamentRepository.existsByIdSite(1L)).thenReturn(false);

        tournamentService.saveTournament(tournament);

        verify(tournamentRepository).save(tournament);
    }

    @Test
    void saveTournament_shouldUpdateExistingTournament_whenExists() {
        Tournament tournament = new Tournament();
        tournament.setId(1L);
        Tournament existingTournament = new Tournament();
        existingTournament.setId(1L);

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(entityRetrievalUtils.getTournamentOrThrow(1L)).thenReturn(existingTournament);

        tournamentService.saveTournament(tournament);

        verify(tournamentRepository).save(existingTournament);
        assertEquals(tournament.getId(), existingTournament.getId());
    }

    @Test
    void deleteTournament_shouldRemoveTournament_whenExists() {
        Long id = 1L;
        Tournament tournament = new Tournament();
        tournament.setId(id);
        when(entityRetrievalUtils.getTournamentOrThrow(id)).thenReturn(tournament);

        tournamentService.deleteTournament(id);

        verify(tournamentRepository).deleteById(id);
    }

    @Test
    void addTeamToTournament_shouldAddTeam_whenTeamNotExists() {
        Long tournamentId = 1L;
        Long teamId = 2L;

        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);

        Team team = new Team();
        team.setId(teamId);

        when(entityRetrievalUtils.getTournamentOrThrow(tournamentId)).thenReturn(tournament);
        when(entityRetrievalUtils.getTeamOrThrow(teamId)).thenReturn(team);

        tournamentService.addTeamToTournament(tournamentId, teamId);

        assertTrue(tournament.getTeams().contains(team));
        verify(tournamentRepository).save(tournament);
    }

    @Test
    void deleteTeamFromTournament_shouldRemoveTeam_whenTeamExists() {
        Long tournamentId = 1L;
        Long teamId = 2L;

        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);

        Team team = new Team();
        team.setId(teamId);
        tournament.addTeam(team);

        when(entityRetrievalUtils.getTournamentOrThrow(tournamentId)).thenReturn(tournament);
        when(entityRetrievalUtils.getTeamOrThrow(teamId)).thenReturn(team);

        tournamentService.deleteTeamFromTournament(tournamentId, teamId);

        assertFalse(tournament.getTeams().contains(team));
        verify(tournamentRepository).save(tournament);
    }

    @Test
    void isManager_shouldReturnTrue_whenUserIsManager() {
        Long userId = 1L;
        Long tournamentId = 2L;
        Long leagueId = 3L;

        League league = new League();
        league.setId(leagueId);

        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        tournament.addLeague(league);

        when(entityRetrievalUtils.getTournamentOrThrow(tournamentId)).thenReturn(tournament);
        when(leagueService.isManager(userId, leagueId)).thenReturn(true);

        boolean result = tournamentService.isManager(userId, tournamentId);

        assertTrue(result);
    }

    @Test
    void getTeamsPlayersByTournamentId_shouldReturnCorrectMapping() {
        Long tournamentId = 1L;

        Team team1 = new Team();
        team1.setId(1L);
        team1.setPlayers(List.of(Player.builder().id(1L).build(), Player.builder().id(2L).build()));

        Team team2 = new Team();
        team2.setId(2L);
        team2.setPlayers(List.of(Player.builder().id(3L).build(), Player.builder().id(4L).build()));

        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        tournament.setTeams(List.of(team1, team2));

        when(entityRetrievalUtils.getTournamentOrThrow(tournamentId)).thenReturn(tournament);

        HashMap<Team, List<Player>> result = tournamentService.getTeamsPlayersByTournamentId(tournamentId);

        assertEquals(2, result.size());
        assertEquals(team1.getPlayers(), result.get(team1));
        assertEquals(team2.getPlayers(), result.get(team2));
    }
}
