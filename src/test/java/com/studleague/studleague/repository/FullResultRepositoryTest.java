package com.studleague.studleague.repository;

import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
public class FullResultRepositoryTest {

    @Autowired
    private ResultRepository resultRepository;

    private Team team;
    private Tournament tournament;
    private FullResult fullResult;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @BeforeEach
    public void setUp() {
        team = new Team();
        tournament = new Tournament();
        teamRepository.save(team);
        tournamentRepository.save(tournament);
        fullResult = FullResult.builder()
                .team(team)
                .tournament(tournament)
                .mask_results("some results")
                .build();

        resultRepository.save(fullResult);
    }

    @AfterEach
    void cleanUp() {
        resultRepository.deleteAll();
        tournamentRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Test
    public void testFindAllByTeamId() {
        List<FullResult> results = resultRepository.findAllByTeamId(team.getId());
        assertThat(results).isNotEmpty();
        assertThat(results).contains(fullResult);
    }

    @Test
    public void testExistsByTeamIdAndTournamentId() {
        boolean exists = resultRepository.existsByTeamIdAndTournamentId(team.getId(), tournament.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void testFindByTeamIdAndTournamentId() {
        Optional<FullResult> resultOpt = resultRepository.findByTeamIdAndTournamentId(team.getId(), tournament.getId());
        assertThat(resultOpt).isPresent();
        assertEquals(fullResult, resultOpt.get());
    }

    @Test
    public void testFindByNonExistingTeamIdAndTournamentId() {
        Optional<FullResult> resultOpt = resultRepository.findByTeamIdAndTournamentId(-1L, -1L);
        assertThat(resultOpt).isNotPresent();
    }
}
