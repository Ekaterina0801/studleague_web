package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    private Player player;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @BeforeEach
    @Transactional
    public void setUp() {
        player = Player.builder()
                .name("John")
                .patronymic("Doe")
                .surname("Smith")
                .university("Test University")
                .dateOfBirth(LocalDate.of(1995, 1, 1))
                .idSite(12345L)
                .build();
        Team team = Team.builder().id(1L).teamName("test").idSite(123L).build();
        Tournament tournament = Tournament.builder().id(1L).idSite(123456L).build();
        tournament.addPlayer(player);
        team.addPlayerToTeam(player);
        teamRepository.save(team);
    }

    @AfterEach
    public void cleanUp() {
        teamRepository.deleteAll();
        tournamentRepository.deleteAll();
        playerRepository.deleteAll();
    }
    @Test
    public void testFindByIdSite() {
        Optional<Player> foundPlayer = playerRepository.findByIdSite(player.getIdSite());
        assertTrue(foundPlayer.isPresent());
        assertThat(foundPlayer.get().getName()).isEqualTo(player.getName());
    }

    @Test
    public void testExistsByIdSite() {
        assertTrue(playerRepository.existsByIdSite(player.getIdSite()));
        assertFalse(playerRepository.existsByIdSite(99999L));
    }

    @Test
    public void testFindAllByTeamIdAndTournamentId() {
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamRepository.findByIdSite(123L), "Team", 123L);
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findByIdSite(123456L), "Tournament", 123456L);
        List<Player> players = playerRepository.findAllByTeamIdAndTournamentId(team.getId(), tournament.getId());
        assertNotNull(players);
        assertThat(players).isNotEmpty();
    }

    @Test
    public void testPlayerNotFound() {
        Optional<Player> player = playerRepository.findById(999L);
        assertFalse(player.isPresent());
    }
}

