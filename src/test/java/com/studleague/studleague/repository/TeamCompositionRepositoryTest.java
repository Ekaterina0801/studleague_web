package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.TeamComposition;
import com.studleague.studleague.entities.Tournament;
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
public class TeamCompositionRepositoryTest {

    @Autowired
    private TeamCompositionRepository teamCompositionRepository;


    private Team teamA;
    private Tournament tournamentA;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @BeforeEach
    public void setUp() {

        teamA = new Team();
        teamA.setTeamName("Team A");
        teamRepository.save(teamA);

        tournamentA = new Tournament();
        tournamentA.setName("Tournament A");
        tournamentRepository.save(tournamentA);

        Player playerA = new Player();
        playerA.setName("Player A");
        playerRepository.save(playerA);
        TeamComposition teamComposition = new TeamComposition();
        teamComposition.setTournament(tournamentA);
        teamComposition.setParentTeam(teamA);
        teamComposition.addPlayer(playerA);

        teamCompositionRepository.save(teamComposition);
    }

    @AfterEach
    void cleanUp() {
        teamCompositionRepository.deleteAll();
        playerRepository.deleteAll();
        teamRepository.deleteAll();

    }

    @Test
    public void testFindAllByTournamentId() {
        List<TeamComposition> compositions = teamCompositionRepository.findAllByTournamentId(tournamentA.getId());
        assertThat(compositions).isNotEmpty();
        assertThat(compositions.get(0).getTournament()).isEqualTo(tournamentA);
    }

    @Test
    public void testFindAllByParentTeamId() {
        List<TeamComposition> compositions = teamCompositionRepository.findAllByParentTeamId(teamA.getId());
        assertThat(compositions).isNotEmpty();
        assertThat(compositions.get(0).getParentTeam()).isEqualTo(teamA);
    }

    @Test
    public void testFindByTournamentIdAndParentTeamId() {
        Optional<TeamComposition> composition = teamCompositionRepository.findByTournamentIdAndParentTeamId(tournamentA.getId(), teamA.getId());
        assertThat(composition).isPresent();
        assertThat(composition.get().getParentTeam()).isEqualTo(teamA);
        assertThat(composition.get().getTournament()).isEqualTo(tournamentA);
    }
}


