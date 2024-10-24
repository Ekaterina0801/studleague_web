package com.studleague.studleague.entities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
        player.setName("John");
        player.setPatronymic("Michael");
        player.setSurname("Doe");
        player.setIdSite(123);
    }

    @Test
    void testGetFullName() {
        assertEquals("John Michael Doe", player.getFullName());
    }

    @Test
    void testAddTeamToPlayer() {
        Team team = new Team();
        team.setTeamName("Team A");

        player.addTeamToPlayer(team);

        assertTrue(player.getTeams().contains(team), "Player should contain Team A");
    }

    @Test
    void testAddTournamentToPlayer() {
        Tournament tournament = new Tournament();
        tournament.setName("Tournament A");

        player.addTournamentToPlayer(tournament);

        assertTrue(player.getTournaments().contains(tournament), "Player should contain Tournament A");
    }

    @Test
    void testEqualsAndHashCode() {
        Player player1 = new Player(1L, "John", "Michael", "Doe", "University", LocalDate.parse("1990-01-01"), 123, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Player player2 = new Player(1L, "John", "Michael", "Doe", "University", LocalDate.parse("1990-01-01"), 123, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        assertEquals(player1, player2);
        assertEquals(player1.hashCode(), player2.hashCode());
    }

    @Test
    void testNotEqualDifferentIdSite() {
        Player player1 = new Player(1L, "John", "Michael", "Doe", "University", LocalDate.parse("1990-01-01"), 123, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Player player2 = new Player(1L, "John", "Michael", "Doe", "University", LocalDate.parse("1990-01-01"), 456, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        assertNotEquals(player1, player2);
    }

    @Test
    void testAddSameTeamTwice() {
        Team team = new Team();
        team.setTeamName("Team A");

        player.addTeamToPlayer(team);
        player.addTeamToPlayer(team);

        assertEquals(1, player.getTeams().size(), "Player should contain only one instance of Team A");
    }

    @Test
    void testAddSameTournamentTwice() {
        Tournament tournament = new Tournament();
        tournament.setName("Tournament A");

        player.addTournamentToPlayer(tournament);
        player.addTournamentToPlayer(tournament);

        assertEquals(1, player.getTournaments().size(), "Player should contain only one instance of Tournament A");
    }
}
