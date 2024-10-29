package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Tournament;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TournamentRepositoryTest {

    @Autowired
    private TournamentRepository tournamentRepository;

    private Tournament testTournament;

    @BeforeEach
    void setUp() {
        testTournament = Tournament.builder()
                .name("Test Tournament")
                .idSite(1L)
                .build();

        tournamentRepository.save(testTournament);
    }

    @Test
    void testFindByIdSite() {
        Optional<Tournament> foundTournament = tournamentRepository.findByIdSite(testTournament.getIdSite());

        assertTrue(foundTournament.isPresent());
        assertThat(foundTournament.get().getIdSite()).isEqualTo(testTournament.getIdSite());
    }

    @Test
    void testExistsByIdSite() {
        boolean exists = tournamentRepository.existsByIdSite(testTournament.getIdSite());
        assertTrue(exists);
    }

    @Test
    void testFindAllByTeamId() {
        List<Tournament> tournaments = tournamentRepository.findAllByTeamId(1L);
        assertNotNull(tournaments);
    }

    @Test
    @Rollback
    void testSaveTournament() {
        Tournament newTournament = Tournament.builder()
                .name("New Tournament")
                .idSite(2L)
                .build();

        Tournament savedTournament = tournamentRepository.save(newTournament);
        assertNotNull(savedTournament.getId());
        assertThat(savedTournament.getName()).isEqualTo(newTournament.getName());
    }

    @Test
    void testDeleteTournament() {
        tournamentRepository.delete(testTournament);
        Optional<Tournament> deletedTournament = tournamentRepository.findById(testTournament.getId());
        assertThat(deletedTournament).isEmpty();
    }
}

