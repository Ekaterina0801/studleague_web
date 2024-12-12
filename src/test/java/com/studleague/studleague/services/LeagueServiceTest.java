package com.studleague.studleague.services;

import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.SystemResult;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.mappers.LeagueMapper;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.repository.security.UserRepository;
import com.studleague.studleague.services.interfaces.LeagueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LeagueServiceTest {

    @MockBean
    private LeagueRepository leagueRepository;

    @MockBean
    private TournamentRepository tournamentRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EntityRetrievalUtils entityRetrievalUtils;

    @MockBean
    private LeagueMapper leagueMapper;

    @MockBean
    private ResultRepository resultRepository;

    @MockBean
    private SystemResultRepository systemResultRepository;

    @MockBean
    private TeamRepository teamRepository;

    @Autowired
    private LeagueService leagueService;

    private League league;
    private Tournament tournament;
    private User user;

    @BeforeEach
    public void setUp() {
        league = new League();
        league.setId(1L);
        league.setName("Test League");

        tournament = new Tournament();
        tournament.setId(1L);
        tournament.setName("Test Tournament");

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
    }

    @Test
    public void testGetLeagueById() {
        // Arrange
        when(entityRetrievalUtils.getLeagueOrThrow(1L)).thenReturn(league);

        // Act
        League result = leagueService.getLeagueById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test League", result.getName());
    }

    @Test
    public void testGetAllLeagues() {
        // Arrange
        List<League> leagues = Arrays.asList(league);
        when(leagueRepository.findAll()).thenReturn(leagues);

        // Act
        List<League> result = leagueService.getAllLeagues();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test League", result.get(0).getName());
    }

    @Test
    public void testSaveLeague_WhenNew() {
        // Arrange
        when(leagueRepository.existsByNameIgnoreCase("Test League")).thenReturn(false);
        when(leagueRepository.save(any(League.class))).thenReturn(league);
        league.setId(null);
        // Act
        leagueService.saveLeague(league);

        // Assert
        verify(leagueRepository, times(1)).save(league);
    }

    @Test
    public void testSaveLeague_WhenExisting() {
        // Arrange
        when(leagueRepository.existsById(1L)).thenReturn(true);
        when(entityRetrievalUtils.getLeagueOrThrow(1L)).thenReturn(league);
        when(leagueRepository.save(any(League.class))).thenReturn(league);

        // Act
        leagueService.saveLeague(league);

        // Assert
        verify(leagueRepository, times(1)).save(league);
    }

    @Test
    public void testDeleteLeague() {
        // Arrange
        when(entityRetrievalUtils.getLeagueOrThrow(1L)).thenReturn(league);
        doNothing().when(leagueRepository).deleteById(1L);

        // Act
        leagueService.deleteLeague(1L);

        // Assert
        verify(leagueRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testAddTournamentToLeague() {
        // Arrange
        when(entityRetrievalUtils.getLeagueOrThrow(1L)).thenReturn(league);
        when(entityRetrievalUtils.getTournamentOrThrow(1L)).thenReturn(tournament);

        // Act
        League result = leagueService.addTournamentToLeague(1L, 1L);

        // Assert
        verify(leagueRepository, times(1)).save(league);
        assertTrue(result.getTournaments().contains(tournament));
    }

    @Test
    public void testDeleteTournamentFromLeague() {
        // Arrange
        league.addTournamentToLeague(tournament); // Add tournament first
        when(entityRetrievalUtils.getLeagueOrThrow(1L)).thenReturn(league);
        when(entityRetrievalUtils.getTournamentOrThrow(1L)).thenReturn(tournament);

        // Act
        League result = leagueService.deleteTournamentFromLeague(1L, 1L);

        // Assert
        verify(leagueRepository, times(1)).save(league);
        assertFalse(result.getTournaments().contains(tournament));
    }

    @Test
    public void testGetLeaguesForCurrentUser() {
        // Arrange
        List<League> leagues = Arrays.asList(league);
        when(entityRetrievalUtils.getUserOrThrow(1L)).thenReturn(user);
        when(leagueRepository.findAllByCreatedById(1L)).thenReturn(leagues);

        // Act
        List<League> result = leagueService.getLeaguesForCurrentUser();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test League", result.get(0).getName());
    }

    @Test
    public void testIsManager() {
        // Arrange
        when(entityRetrievalUtils.getLeagueOrThrow(1L)).thenReturn(league);
        when(entityRetrievalUtils.getUserOrThrow(1L)).thenReturn(user);
        league.addManager(user); // Assign user as manager

        // Act
        boolean result = leagueService.isManager(1L, 1L);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testChangeSystemResultOfLeague() {
        // Arrange
        SystemResult systemResult = new SystemResult();
        systemResult.setId(1L);
        when(systemResultRepository.existsById(1L)).thenReturn(true);
        when(entityRetrievalUtils.getSystemResultOrThrow(1L)).thenReturn(systemResult);
        when(leagueRepository.save(any(League.class))).thenReturn(league);

        // Act
        League result = leagueService.changeSystemResultOfLeague(1L, 1L);

        // Assert
        assertEquals(systemResult, result.getSystemResult());
        verify(leagueRepository, times(1)).save(league);
    }

    @Test
    public void testDeleteAllLeagues() {
        // Arrange
        doNothing().when(leagueRepository).deleteAll();

        // Act
        leagueService.deleteAllLeagues();

        // Assert
        verify(leagueRepository, times(1)).deleteAll();
    }
}

