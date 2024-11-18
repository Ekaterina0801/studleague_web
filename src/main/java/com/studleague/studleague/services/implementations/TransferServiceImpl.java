package com.studleague.studleague.services.implementations;
import com.studleague.studleague.dto.TransferDTO;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Transfer;
import com.studleague.studleague.factory.TransferFactory;
import com.studleague.studleague.repository.PlayerRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.repository.TransferRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("transferService")
public class TransferServiceImpl implements TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private TransferFactory transferFactory;

    @Override
    @Transactional
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    @Override
    @Transactional
    public void saveTransfer(Transfer transfer) {
        Long id = transfer.getId();
        if (id!=null)
        {
            if (transferRepository.existsById(id))
            {
                Transfer existingTransfer = entityRetrievalUtils.getTransferOrThrow(id);
                updateTransfer(existingTransfer, transfer);
            }
        }
        Player player = transfer.getPlayer();
        if (transfer.getOldTeam().getPlayers().contains(player) && !transfer.getNewTeam().getPlayers().contains(player)) {
            transferRepository.save(transfer);
            transfer.getOldTeam().deletePlayerFromTeam(player);
            transfer.getNewTeam().addPlayerToTeam(player);
        }


    }

    @Transactional
    public void updateTransfer(Transfer existingTransfer, Transfer transfer)
    {
        existingTransfer.setTransferDate(transfer.getTransferDate());
        existingTransfer.setComments(transfer.getComments());

    }

    @Override
    @Transactional
    public Transfer getTransfer(Long id) {
        return entityRetrievalUtils.getTransferOrThrow(id);
    }

    @Override
    @Transactional
    public void deleteTransfer(Long id) {
        transferRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<Transfer> getTransfersForPlayer(Long playerId) {
        return transferRepository.findAllByPlayerId(playerId);
    }

    @Override
    @Transactional
    public List<Transfer> getTransfersForTeam(Long teamId) {
        return transferRepository.findAllByTeamId(teamId);
    }

    @Override
    @Transactional
    public void deleteAllTransfers()
    {
        transferRepository.deleteAll();
    }

    @Override
    public boolean isManager(Long userId, Long transferId) {
        if (userId==null)
            return false;
        Transfer transfer = entityRetrievalUtils.getTransferOrThrow(transferId);
        Long leagueId = transfer.getOldTeam().getLeague().getId();
        return leagueService.isManager(userId, leagueId);
    }

    @Override
    public boolean isManager(Long userId, TransferDTO transferDTO) {
        if (userId==null)
            return false;
        Transfer transfer = transferFactory.mapToEntity(transferDTO);
        Long leagueId = transfer.getOldTeam().getLeague().getId();
        return leagueService.isManager(userId, leagueId);
    }
}
