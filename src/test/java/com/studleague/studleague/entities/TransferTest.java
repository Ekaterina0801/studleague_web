package com.studleague.studleague.entities;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

class TransferTest {

    @Test
    void testConstructorAndGetters() {
        LocalDate transferDate = LocalDate.parse("1990-01-01");
        Player player = new Player();
        Team oldTeam = new Team();
        Team newTeam = new Team();
        String comments = "Test comment";

        Transfer transfer = new Transfer(0L, transferDate, player, oldTeam, newTeam, comments);

        assertEquals(transferDate, transfer.getTransferDate());
        assertEquals(player, transfer.getPlayer());
        assertEquals(oldTeam, transfer.getOldTeam());
        assertEquals(newTeam, transfer.getNewTeam());
        assertEquals(comments, transfer.getComments());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDate transferDate = LocalDate.parse("1990-01-01");
        Player playerA = new Player(); // Assume same for playerB
        Player playerB = new Player();
        Team oldTeam = new Team();
        Team newTeam = new Team();
        String comments = "Test comment";

        Transfer transfer1 = new Transfer(0L, transferDate, playerA, oldTeam, newTeam, comments);
        Transfer transfer2 = new Transfer(0L, transferDate, playerA, oldTeam, newTeam, comments);
        Transfer transfer3 = new Transfer(1L, transferDate, playerB, oldTeam, newTeam, comments);

        assertEquals(transfer1, transfer2); // Should be equal
        assertNotEquals(transfer1, transfer3); // Should not be equal

        assertEquals(transfer1.hashCode(), transfer2.hashCode()); // Should have the same hash code
        assertNotEquals(transfer1.hashCode(), transfer3.hashCode()); // Should have different hash codes
    }

    @Test
    void testToString() {
        LocalDate transferDate = LocalDate.parse("1990-01-01");
        Player player = new Player();
        Team oldTeam = new Team();
        Team newTeam = new Team();
        String comments = "Test comment";

        Transfer transfer = new Transfer(1L, transferDate, player, oldTeam, newTeam, comments);

        String expectedString = "Transfer(id=1, transferDate=" + transferDate + ", player=" + player +
                ", oldTeam=" + oldTeam + ", newTeam=" + newTeam + ", comments=" + comments + ")";

        assertEquals(expectedString, transfer.toString());
    }
}
