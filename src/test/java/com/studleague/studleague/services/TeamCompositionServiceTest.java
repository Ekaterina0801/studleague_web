package com.studleague.studleague.services;

import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.TeamComposition;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.mappers.teamComposition.TeamCompositionMapper;
import com.studleague.studleague.repository.TeamCompositionRepository;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.TeamCompositionService;
import com.studleague.studleague.specifications.TeamCompositionSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TeamCompositionServiceTest {

    @MockBean
    private TeamCompositionRepository teamCompositionRepository;

    @MockBean
    private EntityRetrievalUtils entityRetrievalUtils;

    @MockBean
    private LeagueService leagueService;

    @MockBean
    private TeamCompositionMapper teamCompositionMapper;

    @Autowired
    private TeamCompositionService teamCompositionService;

    private TeamComposition teamComposition;
    private Tournament tournament;
    private Team parentTeam;

    @BeforeEach
    public void setUp() {
        tournament = new Tournament();
        tournament.setId(1L);
        tournament.setName("Test Tournament");

        parentTeam = new Team();
        parentTeam.setId(1L);
        parentTeam.setTeamName("Test Team");

        teamComposition = new TeamComposition();
        teamComposition.setId(1L);
        teamComposition.setParentTeam(parentTeam);
        teamComposition.setTournament(tournament);
    }

    @Test
    public void testFindById() {
        // Arrange
        when(entityRetrievalUtils.getTeamCompositionOrThrow(1L)).thenReturn(teamComposition);

        // Act
        TeamComposition result = teamCompositionService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Team", result.getParentTeam().getTeamName());
    }

    @Test
    public void testSave_NewComposition() {
        // Arrange
        when(teamCompositionRepository.findByTournamentIdAndParentTeamId(1L, 1L)).thenReturn(Optional.empty());
        when(teamCompositionRepository.save(any(TeamComposition.class))).thenReturn(teamComposition);
        teamComposition.setId(null);

        // Act
        teamCompositionService.save(teamComposition);

        // Assert
        verify(teamCompositionRepository, times(1)).save(teamComposition);
    }

    @Test
    public void testSave_ExistingComposition() {
        // Arrange
        when(teamCompositionRepository.findById(1L)).thenReturn(Optional.of(teamComposition));
        when(teamCompositionRepository.saveAndFlush(any(TeamComposition.class))).thenReturn(teamComposition);

        // Act
        teamCompositionService.save(teamComposition);

        // Assert
        verify(teamCompositionRepository, times(1)).saveAndFlush(teamComposition);
    }

    @Test
    public void testFindByTournamentId() {
        // Arrange
        List<TeamComposition> compositions = List.of(teamComposition);
        when(teamCompositionRepository.findAllByTournamentId(1L)).thenReturn(compositions);

        // Act
        List<TeamComposition> result = teamCompositionService.findByTournamentId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Tournament", result.get(0).getTournament().getName());
    }

    @Test
    public void testFindByParentTeamId() {
        // Arrange
        List<TeamComposition> compositions = List.of(teamComposition);
        when(teamCompositionRepository.findAllByParentTeamId(1L)).thenReturn(compositions);

        // Act
        List<TeamComposition> result = teamCompositionService.findByParentTeamId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Team", result.get(0).getParentTeam().getTeamName());
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<TeamComposition> compositions = List.of(teamComposition);
        when(teamCompositionRepository.findAll()).thenReturn(compositions);

        // Act
        List<TeamComposition> result = teamCompositionService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteAll() {
        // Arrange
        doNothing().when(teamCompositionRepository).deleteAll();

        // Act
        teamCompositionService.deleteAll();

        // Assert
        verify(teamCompositionRepository, times(1)).deleteAll();
    }

    @Test
    public void testDeleteById() {
        // Arrange
        doNothing().when(teamCompositionRepository).deleteById(1L);

        // Act
        teamCompositionService.deleteById(1L);

        // Assert
        verify(teamCompositionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testIsManager() {
        // Arrange
        League league = new League();
        league.setId(1L);
        parentTeam.setLeague(league);
        teamComposition.setParentTeam(parentTeam);
        when(entityRetrievalUtils.getTeamCompositionOrThrow(1L)).thenReturn(teamComposition);
        when(leagueService.isManager(1L, 1L)).thenReturn(true);

        // Act
        boolean result = teamCompositionService.isManager(1L, 1L);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testSearchTeamCompositions() {
        // Arrange
        Sort sort = Sort.by(Sort.Order.asc("id"));
        Specification<TeamComposition> spec = TeamCompositionSpecification.searchTeamCompositions(1L, 1L, sort);
        List<TeamComposition> compositions = List.of(teamComposition);
        when(teamCompositionRepository.findAll(spec)).thenReturn(compositions);

        // Act
        List<TeamComposition> result = teamCompositionService.searchTeamCompositions(1L, 1L, sort);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
