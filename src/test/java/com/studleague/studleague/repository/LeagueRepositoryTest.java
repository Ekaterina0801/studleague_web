package com.studleague.studleague.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.studleague.studleague.entities.League;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Optional;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LeagueRepositoryTest {

    @Autowired
    private LeagueRepository leagueRepository;

    private League league;

    @BeforeEach
    public void setUp() {
        league = new League();
        league.setName("Premier League");
        leagueRepository.save(league);
    }

    @AfterEach
    public void cleanUp() {
        leagueRepository.deleteAll();
    }

    @Test
    public void testExistsByNameIgnoreCase() {
        assertTrue(leagueRepository.existsByNameIgnoreCase("premier league"));
        assertFalse(leagueRepository.existsByNameIgnoreCase("Stud Liga"));
    }

    @Test
    public void testFindByNameIgnoreCase() {
        Optional<League> foundLeague = leagueRepository.findByNameIgnoreCase("premier league");
        assertTrue(foundLeague.isPresent());
        assertThat(foundLeague.get().getName()).isEqualToIgnoringCase("Premier League");

        Optional<League> notFoundLeague = leagueRepository.findByNameIgnoreCase("Studleague");
        assertFalse(notFoundLeague.isPresent());
    }
}

