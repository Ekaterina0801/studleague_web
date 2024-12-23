package com.studleague.studleague.entities;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


public class ControversialTest {

    private Controversial controversial;

    @BeforeEach
    public void setUp() {
        controversial = Controversial.builder()
                .id(1L)
                .questionNumber(5)
                .answer("Yes")
                .issuedAt(LocalDateTime.parse("2024-10-28T17:28:53"))
                .status("Pending")
                .comment("Test comment")
                .resolvedAt("2024-10-28T17:28:53")
                .appealJuryComment("No appeal")
                .fullResult(new FullResult())
                .build();
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(1, controversial.getId());
        assertEquals(5, controversial.getQuestionNumber());
        assertEquals("Yes", controversial.getAnswer());
        assertNotNull(controversial.getIssuedAt());
        assertEquals("Pending", controversial.getStatus());
        assertEquals("Test comment", controversial.getComment());
        assertEquals("2024-10-28T17:28:53", controversial.getResolvedAt());
        assertEquals("No appeal", controversial.getAppealJuryComment());
        assertNotNull(controversial.getFullResult());
    }

    @Test
    public void testToString() {
        String expected = "Controversial(id=1, questionNumber=5, answer=Yes, issuedAt=..., status=Pending, comment=Test comment, resolvedAt=2024-10-28T17:28:53, appealJuryComment=No appeal, fullResult=null)"; // Adjust expected based on your actual toString output format
        String actual = controversial.toString();
        assertTrue(actual.contains("id=1"));
        assertTrue(actual.contains("questionNumber=5"));
    }

    @Test
    public void testAllArgsConstructor() {
        Controversial controversial2 = new Controversial(2L, 10, "No", LocalDateTime.parse("2024-10-28T17:28:53"), "Resolved", "Comment", "2024-10-28T17:28:53", "Jury comments", null);
        assertEquals(2, controversial2.getId());
        assertEquals(10, controversial2.getQuestionNumber());
    }
}
