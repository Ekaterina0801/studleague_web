package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Transfer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TransferRepositoryTest {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;

    private Player player;
    private Team oldTeam;
    private Team newTeam;

    @BeforeEach
    @Transactional
    void setUp() {
        player = new Player();
        player.setName("Test Player");

        oldTeam = Team.builder().teamName("Old Team").idSite(12L).build();
        newTeam = Team.builder().teamName("New Team").idSite(34L).build();

        player = playerRepository.save(player);
        oldTeam = teamRepository.save(oldTeam);
        newTeam = teamRepository.save(newTeam);
    }

    @Test
    void testFindAllByPlayerId() {
        Transfer transfer = new Transfer();
        transfer.setPlayer(player);
        transfer.setOldTeam(oldTeam);
        transfer.setNewTeam(newTeam);
        transfer.setTransferDate(LocalDate.now());
        transfer.setComments("Test Transfer");

        transferRepository.save(transfer);

        List<Transfer> foundTransfers = transferRepository.findAllByPlayerId(player.getId());

        assertThat(foundTransfers).isNotEmpty();
        assertThat(foundTransfers.get(0).getPlayer()).isEqualTo(player);

    }


    @Test
    void testFindAllByTeamId() {
        Transfer transfer = new Transfer();
        transfer.setPlayer(player);
        transfer.setOldTeam(oldTeam);
        transfer.setNewTeam(newTeam);
        transfer.setTransferDate(LocalDate.now());
        transfer.setComments("Test Transfer");

        transferRepository.save(transfer);

        List<Transfer> foundTransfers = transferRepository.findAllByTeamId(oldTeam.getId());

        assertThat(foundTransfers).isNotEmpty();
        assertThat(foundTransfers.get(0).getOldTeam()).isEqualTo(oldTeam);

    }


}

