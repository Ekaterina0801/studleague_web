package com.studleague.studleague.services;

import com.studleague.studleague.dto.FullResultDTO;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.factory.FullResultFactory;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.ResultService;
import com.studleague.studleague.services.interfaces.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ResultServiceTest {

    @MockBean
    private ResultRepository resultRepository;

    @MockBean
    private ControversialRepository controversialRepository;

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private TournamentRepository tournamentRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private TournamentService tournamentService;

    @MockBean
    private EntityRetrievalUtils entityRetrievalUtils;

    @MockBean
    private LeagueService leagueService;

    @MockBean
    private FullResultFactory fullResultFactory;

    @Autowired
    private ResultService resultService;

    private FullResult fullResult;

    @BeforeEach
    public void setUp() {
        fullResult = new FullResult();
        fullResult.setId(1L);
        fullResult.setTotalScore(100);
        fullResult.setMask_results("1101");
        fullResult.setTournament(new Tournament());
        fullResult.setTeam(new Team());
    }

    @Test
    public void testGetAllFullResults() {
        // Arrange
        List<FullResult> mockResults = Arrays.asList(fullResult);
        when(resultRepository.findAll()).thenReturn(mockResults);

        // Act
        List<FullResult> result = resultService.getAllFullResults();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(fullResult, result.get(0));
    }

    @Test
    public void testGetFullResultById() {
        // Arrange
        when(entityRetrievalUtils.getResultOrThrow(anyLong())).thenReturn(fullResult);

        // Act
        FullResult result = resultService.getFullResultById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(fullResult, result);
    }

    @Test
    public void testSaveFullResult_WhenNew() {
        // Arrange
        when(resultRepository.existsById(anyLong())).thenReturn(false);
        when(resultRepository.save(any(FullResult.class))).thenReturn(fullResult);

        // Act
        resultService.saveFullResult(fullResult);

        // Assert
        verify(resultRepository, times(1)).save(fullResult);
    }

    @Test
    public void testSaveFullResult_WhenExisting() {
        // Arrange
        when(resultRepository.existsById(anyLong())).thenReturn(true);
        when(resultRepository.findById(anyLong())).thenReturn(Optional.of(fullResult));
        when(resultRepository.save(any(FullResult.class))).thenReturn(fullResult);

        // Act
        resultService.saveFullResult(fullResult);

        // Assert
        verify(resultRepository, times(1)).save(fullResult); // save should be called for update
    }

    @Test
    public void testAddControversialToResult() {
        // Arrange
        Controversial controversial = new Controversial();
        controversial.setId(1L);
        when(entityRetrievalUtils.getControversialOrThrow(anyLong())).thenReturn(controversial);
        when(entityRetrievalUtils.getResultOrThrow(anyLong())).thenReturn(fullResult);
        when(resultRepository.save(any(FullResult.class))).thenReturn(fullResult);

        // Act
        FullResult result = resultService.addControversialToResult(1L, 1L);

        // Assert
        verify(resultRepository, times(1)).save(fullResult);
        assertTrue(fullResult.getControversials().contains(controversial));
    }

    @Test
    public void testDeleteControversialFromResult() {
        // Arrange
        Controversial controversial = new Controversial();
        controversial.setId(1L);
        fullResult.addControversialToFullResult(controversial); // Add controversial to the result
        when(entityRetrievalUtils.getControversialOrThrow(anyLong())).thenReturn(controversial);
        when(entityRetrievalUtils.getResultOrThrow(anyLong())).thenReturn(fullResult);
        when(resultRepository.save(any(FullResult.class))).thenReturn(fullResult);

        // Act
        resultService.deleteControversialFromResult(1L, 1L);

        // Assert
        verify(resultRepository, times(1)).save(fullResult);
        assertFalse(fullResult.getControversials().contains(controversial)); // controversial should be removed
    }

    @Test
    public void testDeleteFullResult() {
        // Arrange
        doNothing().when(resultRepository).deleteById(anyLong());

        // Act
        resultService.deleteFullResult(1L);

        // Assert
        verify(resultRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAllResults() {
        // Arrange
        doNothing().when(resultRepository).deleteAll();

        // Act
        resultService.deleteAllResults();

        // Assert
        verify(resultRepository, times(1)).deleteAll();
    }
/*
    @Test
    public void testCalculateResultsBySystem() {
        // Arrange
        // Mocking League
        League league = new League();
        league.setId(1L);

        // Mocking Team
        Team team = new Team();
        team.setId(1L);
        team.setLeague(league); // Associating team with league

        // Mocking Tournament
        Tournament tournament = new Tournament();
        tournament.setId(1L);
        tournament.setDateOfStart(LocalDateTime.now()); // Adjust the date if necessary

        // Set up fullResult for the team and tournament
        fullResult.setTeam(team);
        fullResult.setTournament(tournament);

        // Mocking repositories
        List<FullResult> fullResults = Arrays.asList(fullResult);
        when(entityRetrievalUtils.getLeagueOrThrow(anyLong())).thenReturn(league);
        when(resultRepository.findAllByTeamId(anyLong())).thenReturn(fullResults);
        when(leagueService.isManager(anyLong(), anyLong())).thenReturn(true);

        // Act
        List<LeagueResult> results = resultService.calculateResultsBySystem(1L, "standard", 0);

        // Assert
        assertNotNull(results); // Check if results are returned
        assertEquals(1, results.size()); // There should be one result in the list

        // Optional: Check details of the first result if needed
        LeagueResult firstResult = results.get(0);
        System.out.println("First result: " + firstResult);
    }*/



    /*@Test
    public void testIsManager() {
        // Arrange
        when(entityRetrievalUtils.getResultOrThrow(anyLong())).thenReturn(fullResult);
        when(leagueService.isManager(anyLong(), anyLong())).thenReturn(true);
        // Act
        boolean result = resultService.isManager(1L, 1L);

        // Assert
        assertTrue(result);
    }*/

    @Test
    public void testIsManager_WithFullResultDTO() {
        // Arrange
        FullResultDTO resultDTO = new FullResultDTO();
        resultDTO.setId(1L);
        resultDTO.setTotalScore(100);
        resultDTO.setTeamId(1L);
        resultDTO.setTournamentId(1L);

        FullResult fullResultEntity = new FullResult();
        fullResultEntity.setId(1L);
        fullResultEntity.setTeam(new Team());
        fullResultEntity.getTeam().setLeague(new League());
        when(fullResultFactory.mapToEntity(any(FullResultDTO.class))).thenReturn(fullResultEntity);
        when(leagueService.isManager(anyLong(), anyLong())).thenReturn(true);

        // Act
        boolean result = resultService.isManager(1L, resultDTO);

        // Assert
        assertFalse(result);
    }
}
