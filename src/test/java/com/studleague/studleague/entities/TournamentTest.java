package com.studleague.studleague.entities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TournamentTest {

    private Tournament tournament;

    @BeforeEach
    void setUp() {
        tournament = new Tournament();
        tournament.setName("Test Tournament");
        tournament.setIdSite(1234L);
        tournament.setDateOfStart(LocalDateTime.parse("2024-10-28T17:28:53"));
        tournament.setDateOfEnd(LocalDateTime.parse("2024-10-28T17:28:53"));
    }

    @Test
    void testAddResult() {
        FullResult result = new FullResult(); // Assuming a constructor exists
        tournament.addResult(result);
        assertEquals(1, tournament.getResults().size());
        assertTrue(tournament.getResults().contains(result));
    }

    @Test
    void testDeleteResult() {
        FullResult result = new FullResult();
        tournament.addResult(result);
        tournament.deleteResult(result);
        assertEquals(0, tournament.getResults().size());
    }

    @Test
    void testAddPlayer() {
        Player player = new Player(); // Assuming a constructor exists
        tournament.addPlayer(player);
        assertEquals(1, tournament.getPlayers().size());
        assertTrue(tournament.getPlayers().contains(player));
    }

    @Test
    void testDeletePlayer() {
        Player player = new Player();
        tournament.addPlayer(player);
        tournament.deletePlayer(player);
        assertEquals(0, tournament.getPlayers().size());
    }

    @Test
    void testAddTeam() {
        Team team = new Team(); // Assuming a constructor exists
        tournament.addTeam(team);
        assertEquals(1, tournament.getTeams().size());
        assertTrue(tournament.getTeams().contains(team));
    }

    @Test
    void testDeleteTeam() {
        Team team = new Team();
        tournament.addTeam(team);
        tournament.deleteTeam(team);
        assertEquals(0, tournament.getTeams().size());
    }

    @Test
    void testAddLeague() {
        League league = new League(); // Assuming a constructor exists
        tournament.addLeague(league);
        assertEquals(1, tournament.getLeagues().size());
        assertTrue(tournament.getLeagues().contains(league));
    }

    @Test
    void testEqualsAndHashCode() {
        Tournament anotherTournament = new Tournament();
        anotherTournament.setName("Test Tournament");
        anotherTournament.setIdSite(1234L);
        anotherTournament.setDateOfStart(tournament.getDateOfStart());
        anotherTournament.setDateOfEnd(tournament.getDateOfEnd());

        assertEquals(tournament, anotherTournament);
        assertEquals(tournament.hashCode(), anotherTournament.hashCode());
    }
}

