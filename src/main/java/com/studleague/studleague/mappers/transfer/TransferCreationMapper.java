package com.studleague.studleague.mappers.transfer;


import com.studleague.studleague.dto.tournament.TournamentDTO;
import com.studleague.studleague.dto.transfer.TransferCreationDTO;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.entities.Transfer;
import com.studleague.studleague.mappers.MappingUtils;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = MappingUtils.class)
public interface TransferCreationMapper {


    @Mapping(target = "player", source = "playerId", qualifiedByName = "playerIdToPlayer")
    @Mapping(target = "oldTeam", source = "oldTeamId", qualifiedByName = "teamIdToTeam")
    @Mapping(target = "newTeam", source = "newTeamId", qualifiedByName = "teamIdToTeam")
    Transfer mapToEntity(TransferCreationDTO transferDTO);


    @Mapping(target = "playerId", source = "player.id")
    @Mapping(target = "oldTeamId", source = "oldTeam.id")
    @Mapping(target = "newTeamId", source = "newTeam.id")
    TransferCreationDTO mapToDto(Transfer transfer);


    @AfterMapping
    default void resolveEntities(@MappingTarget Transfer transfer, TransferCreationDTO transferDTO, @Context EntityRetrievalUtils entityRetrievalUtils) {
        transfer.setPlayer(entityRetrievalUtils.getPlayerOrThrow(transferDTO.getPlayerId()));
        transfer.setOldTeam(entityRetrievalUtils.getTeamOrThrow(transferDTO.getOldTeamId()));
        transfer.setNewTeam(entityRetrievalUtils.getTeamOrThrow(transferDTO.getNewTeamId()));
    }


    @AfterMapping
    default void resolveDTOFields(@MappingTarget TransferCreationDTO transferDTO, Transfer transfer) {
        if (transfer.getPlayer() != null) {
            transferDTO.setPlayerId(transfer.getPlayer().getId());
        }
        if (transfer.getOldTeam() != null) {
            transferDTO.setOldTeamId(transfer.getOldTeam().getId());
        }
        if (transfer.getNewTeam() != null) {
            transferDTO.setNewTeamId(transfer.getNewTeam().getId());
        }
    }

    @AfterMapping
    default void setLeagues(@MappingTarget Tournament tournament, TournamentDTO tournamentDto, @Context EntityRetrievalUtils entityRetrievalUtils) {
        for (long id : tournamentDto.getLeaguesIds()) {
            League league = entityRetrievalUtils.getLeagueOrThrow(id);
            tournament.addLeague(league);
        }
    }
}
