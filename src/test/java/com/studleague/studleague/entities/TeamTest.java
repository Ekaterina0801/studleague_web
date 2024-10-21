package com.studleague.studleague.entities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TeamTest {

    private Team team;
    private Player player;
    private Flag flag;
    private Tournament tournament;

    @BeforeEach
    public void setUp() {
        team = Team.builder()
                .teamName("Test Team")
                .university("Test University")
                .idSite(1L)
                .league(new League())
                .build();

        player = new Player();
        flag = new Flag();
        tournament = new Tournament();
    }

    @Test
    public void testAddPlayerToTeam() {
        team.addPlayerToTeam(player);
        assertTrue(team.getPlayers().contains(player), "Player should be added to the team.");
    }

    @Test
    public void testDeletePlayerFromTeam() {
        team.addPlayerToTeam(player);
        team.deletePlayerFromTeam(player);
        assertFalse(team.getPlayers().contains(player), "Player should be removed from the team.");
    }

    @Test
    public void testAddFlagToTeam() {
        team.addFlagToTeam(flag);
        assertTrue(team.getFlags().contains(flag), "Flag should be added to the team.");
    }

    @Test
    public void testDeleteFlagFromTeam() {
        team.addFlagToTeam(flag);
        team.deleteFlagFromTeam(flag);
        assertFalse(team.getFlags().contains(flag), "Flag should be removed from the team.");
    }

    @Test
    public void testAddTournamentToTeam() {
        team.addTournamentToTeam(tournament);
        assertTrue(team.getTournaments().contains(tournament), "Tournament should be added to the team.");
    }

    @Test
    public void testDeleteTournamentFromTeam() {
        team.addTournamentToTeam(tournament);
        team.deleteTournamentFromTeam(tournament);
        assertFalse(team.getTournaments().contains(tournament), "Tournament should be removed from the team.");
    }

    @Test
    public void testEquality() {
        Team team2 = Team.builder()
                .teamName("Test Team")
                .university("Test University")
                .idSite(1L)
                .league(new League())
                .build();

        assertEquals(team, team2, "Teams with the same properties should be equal.");
    }

    @Test
    public void testHashCode() {
        Team team2 = Team.builder()
                .teamName("Test Team")
                .university("Test University")
                .idSite(1L)
                .league(new League())
                .build();

        assertEquals(team.hashCode(), team2.hashCode(), "Equal teams should have the same hash code.");
    }
}

