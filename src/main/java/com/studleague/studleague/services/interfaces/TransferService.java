package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.transfer.TransferDTO;
import com.studleague.studleague.entities.Transfer;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

public interface TransferService {
    List<Transfer> getAllTransfers();

    void saveTransfer(Transfer transfer);

    Transfer getTransfer(Long id);

    void deleteTransfer(Long id);

    List<Transfer> getTransfersForPlayer(Long player_id);

    List<Transfer> getTransfersForTeam(Long team_id);

    void deleteAllTransfers();

    boolean isManager(Long userId, Long transferId);

    boolean isManager(Long userId, TransferDTO transferDTO);

    List<Transfer> searchTransfers(Long playerId, Long oldTeamId, Long newTeamId, Long leagueId, LocalDate startDate, LocalDate endDate, Sort sort);


}
