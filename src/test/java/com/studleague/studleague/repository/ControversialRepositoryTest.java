package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ControversialRepositoryTest {

    @Autowired
    private ControversialRepository controversialRepository;

    @Autowired
    private ResultRepository fullResultRepository;

    private Controversial controversial;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @AfterEach
    void cleanUp() {
        controversialRepository.deleteAll();
        fullResultRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Transactional
    @BeforeEach
    void setUp() {
        controversial = Controversial
                .builder()
                .id(1L)
                .questionNumber(1)
                .answer("Sample Answer")
                .issuedAt(LocalDateTime.now())
                .status("Pending")
                .comment("Sample comment")
                .resolvedAt(null)
                .appealJuryComment(null)
                .build();
        FullResult fullResult = new FullResult();
        Team team = Team.builder().idSite(12L).build();
        Tournament tournament = Tournament.builder().idSite(12L).build();
        tournamentRepository.save(tournament);
        fullResult.setTournament(tournament);
        fullResult.setTeam(team);
        teamRepository.save(team);
        fullResultRepository.save(fullResult);
        controversial.setFullResult(fullResult);
        controversialRepository.save(controversial);
    }

    @Test
    public void testFindAllByTeamId() {
        Team team = entityRetrievalUtils.getTeamOrThrow(12L);
        List<Controversial> results = controversialRepository.findAllByTeamId(team.getId());
        assertThat(results).isNotEmpty();
    }

    @Test
    public void testFindAllByTournamentId() {
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(12L);
        List<Controversial> results = controversialRepository.findAllByTournamentId(tournament.getId());
        assertThat(results).isNotEmpty();
    }

    @Test
    public void testFindAllByNonExistingTeamId() {
        List<Controversial> results = controversialRepository.findAllByTeamId(999L);
        assertThat(results).isEmpty();
    }

    @Test
    public void testFindAllByNonExistingTournamentId() {
        List<Controversial> results = controversialRepository.findAllByTournamentId(999L);
        assertThat(results).isEmpty();
    }

    @Test
    public void testSaveControversial() {
        Controversial savedControversial = controversialRepository.save(controversial);
        assertThat(savedControversial).isNotNull();
        assertThat(savedControversial.getId()).isEqualTo(1L);
    }

    @Test
    public void testDeleteControversial() {
        controversialRepository.delete(controversial);
        assertThat(controversialRepository.findById(controversial.getId())).isNotPresent();
    }
}

