package com.studleague.studleague.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class LeagueTest {

    private League league;
    private Tournament tournament;

    @BeforeEach
    void setUp() {
        league = new League();
        league.setName("Stud League");

        tournament = new Tournament();
        tournament.setName("Tournament A");
    }

    @Test
    void addTournamentToLeague_ShouldAddTournament() {
        league.addTournamentToLeague(tournament);

        assertTrue(league.getTournaments().contains(tournament));
        assertTrue(tournament.getLeagues().contains(league));
    }

    @Test
    void addTournamentToLeague_ShouldNotAddDuplicateTournament() {
        league.addTournamentToLeague(tournament);
        league.addTournamentToLeague(tournament);

        assertEquals(1, league.getTournaments().size());
    }

    @Test
    void deleteTournamentFromLeague_ShouldRemoveTournament() {
        league.addTournamentToLeague(tournament);
        league.deleteTournamentFromLeague(tournament);

        assertFalse(league.getTournaments().contains(tournament));
        assertFalse(tournament.getLeagues().contains(league));
    }

    @Test
    void deleteTournamentFromLeague_ShouldDoNothingIfTournamentNotPresent() {
        league.deleteTournamentFromLeague(tournament);
        assertTrue(league.getTournaments().isEmpty());
    }

    @Test
    void equals_ShouldReturnTrueForSameName() {
        League anotherLeague = new League();
        anotherLeague.setName("Stud League");

        assertEquals(league, anotherLeague);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentNames() {
        League anotherLeague = new League();
        anotherLeague.setName("Stud Liga");

        assertNotEquals(league, anotherLeague);
    }

    @Test
    void hashCode_ShouldBeEqualForSameName() {
        League anotherLeague = new League();
        anotherLeague.setName("Stud League");

        assertEquals(league.hashCode(), anotherLeague.hashCode());
    }

    @Test
    void hashCode_ShouldNotBeEqualForDifferentNames() {
        League anotherLeague = new League();
        anotherLeague.setName("Stud Liga");

        assertNotEquals(league.hashCode(), anotherLeague.hashCode());
    }

    @Test
    void toString_ShouldContainLeagueName() {
        String expected = "League(id=null, name=Stud League)";
        assertEquals(expected, league.toString());
    }
}
