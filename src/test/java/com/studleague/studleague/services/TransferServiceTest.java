package com.studleague.studleague.services;

import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Transfer;
import com.studleague.studleague.mappers.TransferMapper;
import com.studleague.studleague.repository.PlayerRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.repository.TransferRepository;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.TransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransferServiceTest {

    @Autowired
    private TransferService transferService;

    @MockBean
    private TransferRepository transferRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private EntityRetrievalUtils entityRetrievalUtils;

    @MockBean
    private LeagueService leagueService;

    @MockBean
    private TransferMapper transferMapper;

    @Test
    void getAllTransfers_shouldReturnListOfTransfers() {
        List<Transfer> transfers = List.of(new Transfer(), new Transfer());
        when(transferRepository.findAll()).thenReturn(transfers);

        List<Transfer> result = transferService.getAllTransfers();

        assertEquals(transfers, result);
        verify(transferRepository).findAll();
    }

    @Test
    void saveTransfer_shouldSaveNewTransfer_whenConditionsMet() {
        Player player = new Player();
        Team oldTeam = new Team();
        Team newTeam = new Team();
        oldTeam.addPlayerToTeam(player);

        Transfer transfer = new Transfer();
        transfer.setId(null);
        transfer.setPlayer(player);
        transfer.setOldTeam(oldTeam);
        transfer.setNewTeam(newTeam);

        transferService.saveTransfer(transfer);

        verify(transferRepository).save(transfer);
        assertFalse(oldTeam.getPlayers().contains(player));
        assertTrue(newTeam.getPlayers().contains(player));
    }

    @Test
    void saveTransfer_shouldUpdateExistingTransfer_whenIdExists() {
        Long transferId = 1L;

        Transfer existingTransfer = new Transfer();
        existingTransfer.setId(transferId);

        Transfer updatedTransfer = new Transfer();
        updatedTransfer.setId(transferId);
        updatedTransfer.setComments("Updated comment");

        when(transferRepository.existsById(transferId)).thenReturn(true);
        when(entityRetrievalUtils.getTransferOrThrow(transferId)).thenReturn(existingTransfer);

        transferService.saveTransfer(updatedTransfer);

        verify(transferRepository).save(existingTransfer);
        assertEquals("Updated comment", existingTransfer.getComments());
    }

    @Test
    void getTransfer_shouldReturnTransfer_whenExists() {
        Long transferId = 1L;
        Transfer transfer = new Transfer();
        transfer.setId(transferId);

        when(entityRetrievalUtils.getTransferOrThrow(transferId)).thenReturn(transfer);

        Transfer result = transferService.getTransfer(transferId);

        assertEquals(transfer, result);
        verify(entityRetrievalUtils).getTransferOrThrow(transferId);
    }

    @Test
    void deleteTransfer_shouldDeleteTransfer_whenExists() {
        Long transferId = 1L;

        transferService.deleteTransfer(transferId);

        verify(transferRepository).deleteById(transferId);
    }

    @Test
    void getTransfersForPlayer_shouldReturnTransfersForPlayer() {
        Long playerId = 1L;
        List<Transfer> transfers = List.of(new Transfer(), new Transfer());

        when(transferRepository.findAllByPlayerId(playerId)).thenReturn(transfers);

        List<Transfer> result = transferService.getTransfersForPlayer(playerId);

        assertEquals(transfers, result);
        verify(transferRepository).findAllByPlayerId(playerId);
    }

    @Test
    void getTransfersForTeam_shouldReturnTransfersForTeam() {
        Long teamId = 1L;
        List<Transfer> transfers = List.of(new Transfer(), new Transfer());

        when(transferRepository.findAllByTeamId(teamId)).thenReturn(transfers);

        List<Transfer> result = transferService.getTransfersForTeam(teamId);

        assertEquals(transfers, result);
        verify(transferRepository).findAllByTeamId(teamId);
    }

    @Test
    void isManager_shouldReturnTrue_whenUserIsManager() {
        Long userId = 1L;
        Long transferId = 2L;

        Team oldTeam = new Team();
        League league = new League();
        league.setId(3L);
        oldTeam.setLeague(league);

        Transfer transfer = new Transfer();
        transfer.setOldTeam(oldTeam);

        when(entityRetrievalUtils.getTransferOrThrow(transferId)).thenReturn(transfer);
        when(leagueService.isManager(userId, league.getId())).thenReturn(true);

        boolean result = transferService.isManager(userId, transferId);

        assertTrue(result);
    }

    @Test
    void searchTransfers_shouldReturnTransfersBasedOnCriteria() {
        Long playerId = 1L;
        Long oldTeamId = 2L;
        Long newTeamId = 3L;
        Long leagueId = 4L;
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        Sort sort = Sort.by("transferDate");

        List<Transfer> transfers = List.of(new Transfer(), new Transfer());

        when(transferRepository.findAll(any(Specification.class))).thenReturn(transfers);

        List<Transfer> result = transferService.searchTransfers(playerId, oldTeamId, newTeamId, leagueId, startDate, endDate, sort);

        assertEquals(transfers, result);
        verify(transferRepository).findAll(any(Specification.class));
    }
}

