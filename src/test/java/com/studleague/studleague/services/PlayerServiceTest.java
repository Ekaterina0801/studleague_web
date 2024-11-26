package com.studleague.studleague.services;

import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.factory.PlayerFactory;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.PlayerService;
import com.studleague.studleague.specifications.PlayerSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlayerServiceTest {

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private LeagueRepository leagueRepository;

    @MockBean
    private EntityRetrievalUtils entityRetrievalUtils;

    @MockBean
    private LeagueService leagueService;

    @MockBean
    private PlayerFactory playerFactory;

    @MockBean
    private TeamCompositionRepository teamCompositionRepository;

    @MockBean
    private TournamentRepository tournamentRepository;

    @Autowired
    private PlayerService playerService;

    private Player player;
    private Team team;
    private League league;

    @BeforeEach
    public void setUp() {
        player = new Player();
        player.setId(1L);
        player.setName("John");
        player.setSurname("Doe");

        team = new Team();
        team.setId(1L);
        team.setTeamName("Test Team");

        league = new League();
        league.setId(1L);
        league.setName("Test League");
    }

    @Test
    public void testGetPlayerById() {
        // Arrange
        when(entityRetrievalUtils.getPlayerOrThrow(1L)).thenReturn(player);

        // Act
        Player result = playerService.getPlayerById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    public void testGetAllPlayers() {
        // Arrange
        List<Player> players = List.of(player);
        when(playerRepository.findAll()).thenReturn(players);

        // Act
        List<Player> result = playerService.getAllPlayers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }

    @Test
    public void testSavePlayer_NewPlayer() {
        // Arrange
        when(playerRepository.existsById(1L)).thenReturn(false);
        when(playerRepository.save(any(Player.class))).thenReturn(player);
        player.setId(null);

        // Act
        playerService.savePlayer(player);

        // Assert
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    public void testSavePlayer_ExistingPlayer() {
        // Arrange
        when(playerRepository.existsById(1L)).thenReturn(true);
        when(entityRetrievalUtils.getPlayerOrThrow(1L)).thenReturn(player);
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        // Act
        playerService.savePlayer(player);

        // Assert
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    public void testDeletePlayer() {
        // Arrange
        when(entityRetrievalUtils.getPlayerOrThrow(1L)).thenReturn(player);
        doNothing().when(playerRepository).deleteById(1L);

        // Act
        playerService.deletePlayer(1L);

        // Assert
        verify(playerRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetPlayerByIdSite() {
        // Arrange
        player.setIdSite(123L);
        when(entityRetrievalUtils.getPlayerByIdSiteOrThrow(123L)).thenReturn(player);

        // Act
        Player result = playerService.getPlayerByIdSite(123L);

        // Assert
        assertNotNull(result);
        assertEquals(123L, result.getIdSite());
    }

    @Test
    public void testExistsByIdSite() {
        // Arrange
        when(playerRepository.existsByIdSite(123L)).thenReturn(true);

        // Act
        boolean result = playerService.existsByIdSite(123L);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testDeleteAllPlayers() {
        // Arrange
        doNothing().when(playerRepository).deleteAll();

        // Act
        playerService.deleteAllPlayers();

        // Assert
        verify(playerRepository, times(1)).deleteAll();
    }

    @Test
    public void testIsManager() {
        // Arrange
        when(entityRetrievalUtils.getPlayerOrThrow(1L)).thenReturn(player);
        team.setLeague(league);
        player.setTeams(List.of(team));
        when(leagueService.isManager(1L, 1L)).thenReturn(true);

        // Act
        boolean result = playerService.isManager(1L, 1L);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testSearchPlayers() {
        // Arrange
        Specification<Player> spec = PlayerSpecification.searchPlayers("John", "Doe", null, null, null);
        Sort sort = Sort.by(Sort.Order.asc("name"));
        List<Player> players = List.of(player);
        when(playerRepository.findAll(spec, sort)).thenReturn(players);

        // Act
        List<Player> result = playerService.searchPlayers("John", "Doe", null, null, null, List.of("name"), List.of("asc"));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }
}

