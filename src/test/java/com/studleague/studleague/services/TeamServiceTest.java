package com.studleague.studleague.services;

import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.mappers.TeamMapper;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.TeamCompositionService;
import com.studleague.studleague.services.interfaces.TeamService;
import com.studleague.studleague.specifications.TeamSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TeamServiceTest {

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private FlagRepository flagRepository;

    @MockBean
    private LeagueRepository leagueRepository;

    @MockBean
    private ResultRepository resultRepository;

    @MockBean
    private TournamentRepository tournamentRepository;

    @MockBean
    private TeamCompositionRepository teamCompositionRepository;

    @MockBean
    private TeamCompositionService teamCompositionService;

    @MockBean
    private EntityRetrievalUtils entityRetrievalUtils;

    @MockBean
    private LeagueService leagueService;

    @MockBean
    private TeamMapper teamMapper;

    @Autowired
    private TeamService teamService;

    private Team team;
    private League league;
    private Player player;
    private Flag flag;

    @BeforeEach
    public void setUp() {
        league = new League();
        league.setId(1L);
        league.setName("Test League");

        player = new Player();
        player.setId(1L);
        player.setName("Test Player");

        flag = new Flag();
        flag.setId(1L);
        flag.setName("Test Flag");

        team = new Team();
        team.setId(1L);
        team.setTeamName("Test Team");
        team.setLeague(league);
    }

    @Test
    public void testGetTeamById() {
        // Arrange
        when(entityRetrievalUtils.getTeamOrThrow(1L)).thenReturn(team);

        // Act
        Team result = teamService.getTeamById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Team", result.getTeamName());
    }

    @Test
    public void testGetAllTeams() {
        // Arrange
        List<Team> teams = List.of(team);
        when(teamRepository.findAll()).thenReturn(teams);

        // Act
        List<Team> result = teamService.getAllTeams();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testSaveTeam_NewTeam() {
        // Arrange
        team.setId(null);
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // Act
        teamService.saveTeam(team);

        // Assert
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    public void testSaveTeam_UpdateExisting() {
        // Arrange
        when(teamRepository.existsById(1L)).thenReturn(true);
        when(entityRetrievalUtils.getTeamOrThrow(1L)).thenReturn(team);

        // Act
        teamService.saveTeam(team);

        // Assert
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    public void testDeleteTeam() {
        // Arrange
        when(entityRetrievalUtils.getTeamOrThrow(1L)).thenReturn(team);

        // Act
        teamService.deleteTeam(1L);

        // Assert
        verify(teamRepository, times(1)).delete(team);
    }

    @Test
    public void testTeamsByLeague() {
        // Arrange
        List<Team> teams = List.of(team);
        when(teamRepository.findAllByLeagueId(1L)).thenReturn(teams);

        // Act
        List<Team> result = teamService.teamsByLeague(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testAddPlayerToTeam() {
        // Arrange
        when(entityRetrievalUtils.getPlayerOrThrow(1L)).thenReturn(player);
        when(entityRetrievalUtils.getTeamOrThrow(1L)).thenReturn(team);

        // Act
        Team result = teamService.addPlayerToTeam(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    public void testDeletePlayerFromTeam() {
        // Arrange
        when(entityRetrievalUtils.getPlayerOrThrow(1L)).thenReturn(player);
        when(entityRetrievalUtils.getTeamOrThrow(1L)).thenReturn(team);

        // Act
        Team result = teamService.deletePlayerFromTeam(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    public void testAddFlagToTeam() {
        // Arrange
        when(entityRetrievalUtils.getFlagOrThrow(1L)).thenReturn(flag);
        when(entityRetrievalUtils.getTeamOrThrow(1L)).thenReturn(team);

        // Act
        Team result = teamService.addFlagToTeam(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    public void testDeleteFlagFromTeam() {
        // Arrange
        when(entityRetrievalUtils.getFlagOrThrow(1L)).thenReturn(flag);
        when(entityRetrievalUtils.getTeamOrThrow(1L)).thenReturn(team);

        // Act
        Team result = teamService.deleteFlagFromTeam(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    public void testGetTeamsByPlayerId() {
        // Arrange
        List<Team> teams = List.of(team);
        player.setTeams(teams);
        when(entityRetrievalUtils.getPlayerOrThrow(1L)).thenReturn(player);

        // Act
        List<Team> result = teamService.getTeamsByPlayerId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testIsManager() {
        // Arrange
        when(entityRetrievalUtils.getTeamOrThrow(1L)).thenReturn(team);
        when(leagueService.isManager(1L, 1L)).thenReturn(true);

        // Act
        boolean result = teamService.isManager(1L, 1L);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testSearchTeams() {
        // Arrange
        Sort sort = Sort.by(Sort.Order.asc("id"));
        Specification<Team> spec = TeamSpecification.searchTeams("Test Team", 1L, List.of(1L), sort);
        List<Team> teams = List.of(team);
        when(teamRepository.findAll(spec)).thenReturn(teams);

        // Act
        List<Team> result = teamService.searchTeams("Test Team", 1L, List.of(1L), sort);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetInfoTeamResultsByTeam() {
        // Arrange
        List<FullResult> results = List.of(FullResult.builder().mask_results("1101").build());
        List<TeamComposition> compositions = List.of(new TeamComposition());
        when(entityRetrievalUtils.getTeamOrThrow(1L)).thenReturn(team);
        when(resultRepository.findAllByTeamId(1L)).thenReturn(results);
        when(teamCompositionService.findByParentTeamId(1L)).thenReturn(compositions);

        // Act
        List<InfoTeamResults> result = teamService.getInfoTeamResultsByTeam(1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
