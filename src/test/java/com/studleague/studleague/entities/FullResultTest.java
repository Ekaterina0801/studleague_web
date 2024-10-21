package com.studleague.studleague.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class FullResultTest {

    private FullResult fullResult;
    private Controversial controversial;

    @BeforeEach
    public void setUp() {
        fullResult = new FullResult();
        controversial = new Controversial();
        controversial.setId(1L);
    }

    @Test
    public void testAddControversialToFullResult() {
        fullResult.addControversialToFullResult(controversial);

        assertEquals(1, fullResult.getControversials().size());
        assertTrue(fullResult.getControversials().contains(controversial));
    }

    @Test
    public void testAddDuplicateControversial() {
        fullResult.addControversialToFullResult(controversial);
        fullResult.addControversialToFullResult(controversial);

        assertEquals(1, fullResult.getControversials().size());
    }

    @Test
    public void testDeleteControversialFromFullResult() {
        fullResult.addControversialToFullResult(controversial);
        fullResult.deleteControversialFromFullResult(controversial);

        assertEquals(0, fullResult.getControversials().size());
        assertNull(controversial.getFullResult());
    }

    @Test
    public void testDeleteNonexistentControversial() {
        fullResult.deleteControversialFromFullResult(controversial);

        assertEquals(0, fullResult.getControversials().size());
    }
}

