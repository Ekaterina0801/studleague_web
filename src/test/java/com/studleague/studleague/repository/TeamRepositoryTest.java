package com.studleague.studleague.repository;

import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private LeagueRepository leagueRepository;


    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @BeforeEach
    @Transactional
    public void setUp() {
        League league = League.builder()
                .name("Test League")
                .build();
        leagueRepository.save(league);

        Team team = Team.builder()
                .teamName("Test Team")
                .university("Test University")
                .idSite(100L)
                .league(league)
                .build();
        teamRepository.save(team);

        Player player = Player.builder().idSite(12L).build();
        playerRepository.save(player);
    }

    @AfterEach
    void cleanUp() {
        tournamentRepository.deleteAll();
        playerRepository.deleteAll();
        teamRepository.deleteAll();
        leagueRepository.deleteAll();
    }

    @Test
    public void testFindAllByLeagueId() {
        League league = entityRetrievalUtils.getLeagueByNameOrThrow("Test League");
        List<Team> teams = teamRepository.findAllByLeagueId(league.getId());
        assertThat(teams).isNotEmpty();
        assertThat(teams.get(0).getTeamName()).isEqualTo("Test Team");
    }

    @Test
    public void testFindByIdSite() {
        Optional<Team> optionalTeam = teamRepository.findByIdSite(100L);
        assertThat(optionalTeam).isPresent();
        assertThat(optionalTeam.get().getTeamName()).isEqualTo("Test Team");
    }

    @Test
    public void testFindAllByPlayerIdAndLeagueId() {
        Player player = entityRetrievalUtils.getPlayerOrThrow(12L);
        League league = entityRetrievalUtils.getLeagueByNameOrThrow("Test League");
        List<Team> teams = teamRepository.findAllByPlayerIdAndLeagueId(player.getId(), league.getId());
        assertThat(teams).isEmpty();
    }

    @Test
    public void testExistsByIdSite() {
        boolean exists = teamRepository.existsByIdSite(100L);
        assertThat(exists).isTrue();

        exists = teamRepository.existsByIdSite(200L);
        assertThat(exists).isFalse();
    }
}

