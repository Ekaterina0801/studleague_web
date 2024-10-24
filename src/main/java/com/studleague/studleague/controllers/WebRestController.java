package com.studleague.studleague.controllers;
import com.studleague.studleague.dto.*;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.mappings.*;
import com.studleague.studleague.services.interfaces.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WebRestController {

    @Autowired
    public FlagService flagService;

    @Autowired
    public ResultService resultService;

    @Autowired
    public LeagueService leagueService;

    @Autowired
    public PlayerService playerService;

    @Autowired
    public TeamService teamService;

    @Autowired
    public TournamentService tournamentService;

    @Autowired
    public TransferService transferService;

    @Autowired
    public TransferMapper transferMapper;

    @Autowired
    public TeamMapper teamMapper;

    @Autowired
    public FullResultMapper fullResultMapper;

    @Autowired
    public ControversialMapper controversialMapper;

    @Autowired
    public TournamentMapper tournamentMapper;

    @Autowired
    public ControversialService controversialService;

    @Autowired
    public PlayerMapper playerMapper;

    @Autowired
    private FlagMapper flagMapper;

    @Autowired
    private LeagueMapper leagueMapper;


    /* -------------------------------------------
                      Controversials
    ------------------------------------------- */

    /**
     * Обрабатывает входящий запрос на получение всех Controversial.
     *
     * @return List of Controversial, содержащий все Controversial
     */
    @Operation(
            summary = "Получить все спорные",
            description = "Использовать для получения всех спорных"
    )
    @GetMapping("/controversials")
    public ResponseEntity<List<ControversialDTO>> getControversials()
    {
        return ResponseEntity.ok(controversialService.getAllControversials().stream().map(x->controversialMapper.toDto(x)).toList());
    }

    @PostMapping ("/controversials")
    public ResponseEntity<ControversialDTO> addNewControversial(@RequestBody ControversialDTO controversialDto)
    {
        Controversial controversial = controversialMapper.toEntity(controversialDto);
        controversialService.saveControversial(controversial);
        return ResponseEntity.status(HttpStatus.CREATED).body(controversialDto);
    }

    @GetMapping("/controversials/{id}")
    public ResponseEntity<ControversialDTO> controversialById(@PathVariable long id)
    {
        Controversial controversial = controversialService.getControversialById(id);
        ControversialDTO controversialDTO = controversialMapper.toDto(controversial);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(controversialDTO);
    }

    @DeleteMapping("/controversials/{id}")
    public ResponseEntity<String> deleteControversial(@PathVariable long id)
    {
        controversialService.deleteControversial(id);
        return ResponseEntity.ok("Controversial with ID = "+ id + "was deleted");
    }

    @DeleteMapping("/controversials")
    public ResponseEntity<String> deleteAllControversials()
    {
        controversialService.deleteAllControversials();
        return ResponseEntity.ok("Controversials were deleted");
    }


    /* -------------------------------------------
                      Flags
    ------------------------------------------- */

    @GetMapping("/flags")
    public ResponseEntity<List<FlagDTO>> getFlags()
    {
        return ResponseEntity.ok(flagService.getAllFlags().stream().map(x->flagMapper.toDTO(x)).toList());
    }

    @GetMapping("/flags/{id}")
    public ResponseEntity<FlagDTO> getFlag(@PathVariable long id)
    {
        return ResponseEntity.ok(flagMapper.toDTO(flagService.getFlagById(id)));
    }

    @DeleteMapping("/flags/{id}")
    public ResponseEntity<String> deleteFlag(@PathVariable long id)
    {
        flagService.deleteFlag(id);
        return ResponseEntity.ok("Flag with ID = "+ id + "was deleted");
    }

    @PostMapping("/flags")
    public ResponseEntity<FlagDTO> addNewFlag(@RequestBody FlagDTO flagDto)
    {
        flagService.saveFlag(flagMapper.toEntity(flagDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(flagDto);
    }

    @DeleteMapping("/flags")
    public ResponseEntity<String> deleteAllFlags()
    {
        flagService.deleteAllFlags();
        return ResponseEntity.ok("Flags were deleted");
    }
    /* -------------------------------------------
                      Leagues
    ------------------------------------------- */
    @GetMapping("/leagues")
    public ResponseEntity<List<LeagueDTO>> getLeagues()
    {
        return ResponseEntity.ok(leagueService.getAllLeagues().stream().map(x->leagueMapper.toDTO(x)).toList());
    }

    @GetMapping("/leagues/{id}")
    public ResponseEntity<LeagueDTO> getLeague(@PathVariable long id)
    {
        return ResponseEntity.ok(leagueMapper.toDTO(leagueService.getLeagueById(id)));
    }

    @PostMapping("/leagues")
    public ResponseEntity<LeagueDTO> addNewLeague(@RequestBody LeagueDTO leagueDto)
    {
        leagueService.saveLeague(leagueMapper.toEntity(leagueDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(leagueDto);
    }

    @DeleteMapping("/leagues/{id}")
    public ResponseEntity<String> deleteLeague(@PathVariable long id)
    {
        leagueService.deleteLeague(id);
        return ResponseEntity.ok("League with ID = "+ id + "was deleted");
    }

    @PutMapping("/leagues/{leagueId}/tournaments/{tournamentId}")
    public ResponseEntity<LeagueDTO> addTournamentToLeague(@PathVariable long leagueId, @PathVariable long tournamentId)
    {
        League updatedLeague = leagueService.addTournamentToLeague(leagueId,tournamentId);
        return ResponseEntity.ok(leagueMapper.toDTO(updatedLeague));
    }

    @DeleteMapping("/leagues/{leagueId}/tournaments/{tournamentId}")
    public ResponseEntity<LeagueDTO> deleteTournamentFromLeague(@PathVariable long leagueId, @PathVariable long tournamentId)
    {
        League updatedLeague = leagueService.deleteTournamentToLeague(leagueId,tournamentId);
        return ResponseEntity.ok(leagueMapper.toDTO(updatedLeague));
    }

    @GetMapping("/leagues/{leagueId}/tournaments")
    public ResponseEntity<List<TournamentDTO>> allTournamentsFromLeague(@PathVariable long leagueId)
    {
        League league = leagueService.getLeagueById(leagueId);
        return ResponseEntity.ok(league.getTournaments().stream().map(x->tournamentMapper.toDTO(x)).toList());
    }

    @GetMapping("/leagues/{leagueId}/players/{playerId}/team")
    public ResponseEntity<TeamDTO> teamFromLeague(@PathVariable long leagueId, @PathVariable long playerId)
    {
        return ResponseEntity.ok(teamMapper.toDTO(teamService.getTeamByPlayerIdAndLeagueId(leagueId,playerId)));
    }

    @GetMapping("/leagues/{leagueId}/teams")
    public ResponseEntity<List<TeamDTO>> allTeamsFromLeague(@PathVariable long leagueId){
        return ResponseEntity.ok(teamService.teamsByLeague(leagueId).stream().map(x->teamMapper.toDTO(x)).toList());
    }

    @DeleteMapping("/leagues")
    public ResponseEntity<String> deleteAllLeagues()
    {
        leagueService.deleteAllLeagues();
        return ResponseEntity.ok("Leagues were deleted");
    }
    /* -------------------------------------------
                      Players
    ------------------------------------------- */
    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getPlayers()
    {
        return ResponseEntity.ok(playerService.getAllPlayers().stream().map(x->playerMapper.toDTO(x)).toList());
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable long id)
    {
        return ResponseEntity.ok(playerMapper.toDTO(playerService.getPlayerById(id)));
    }

    @PostMapping("/players")
    public ResponseEntity<PlayerDTO> addNewPlayer(@RequestBody PlayerDTO playerDTO)
    {
        Player player = playerMapper.toEntity(playerDTO);
        playerService.savePlayer(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(playerDTO);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable long id)
    {
        playerService.deletePlayer(id);
        return ResponseEntity.ok("Player with ID = "+ id + "was deleted");
    }

    @GetMapping("/players/{playerId}/transfers")
    public ResponseEntity<List<TransferDTO>> allTransfersFromPlayer(@PathVariable long playerId){
        return ResponseEntity.ok(transferService.getTransfersForPlayer(playerId).stream().map(x->transferMapper.toDTO(x)).toList());
    }

    @GetMapping("/players/{playerId}/tournaments")
    public ResponseEntity<List<TournamentDTO>> allTournamentsFromPlayer(@PathVariable long playerId){
        Player player = playerService.getPlayerById(playerId);
        return ResponseEntity.ok(player.getTournaments().stream().map(x->tournamentMapper.toDTO(x)).toList());
    }

    @DeleteMapping("/players")
    public ResponseEntity<String> deleteAllPlayers()
    {
        playerService.deleteAllPlayers();
        return ResponseEntity.ok("Players were deleted");
    }
    /* -------------------------------------------
                      Results
    ------------------------------------------- */

    @GetMapping("/results")
    public ResponseEntity<List<FullResultDTO>> getResults()
    {
        return ResponseEntity.ok(resultService.getAllFullResults().stream().map(x->fullResultMapper.toDTO(x)).toList());
    }

    @GetMapping("/results/{id}")
    public ResponseEntity<FullResultDTO> getResult(@PathVariable long id)
    {
        return ResponseEntity.ok(fullResultMapper.toDTO(resultService.getFullResultById(id)));
    }

    @PostMapping("/results")
    public ResponseEntity<FullResultDTO> addNewResult(@RequestBody FullResultDTO fullResultDTO)
    {
        FullResult fullResult = fullResultMapper.toEntity(fullResultDTO);
        resultService.saveFullResult(fullResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(fullResultDTO);
    }

    @DeleteMapping("/results/{id}")
    public ResponseEntity<String> deleteFullResult(@PathVariable long id)
    {
        resultService.deleteFullResult(id);
        return ResponseEntity.ok("FullResult with ID = "+ id + "was deleted");
    }

    @GetMapping("/results/{resultId}/controversials")
    public ResponseEntity<List<ControversialDTO>> allControversialsFromResult(@PathVariable long resultId)
    {
        FullResult fullResult = resultService.getFullResultById(resultId);
        return ResponseEntity.ok(fullResult.getControversials().stream().map(x->controversialMapper.toDto(x)).toList());
    }

    @PutMapping("/results/{resultId}/controversials/{controversialId}")
    public ResponseEntity<FullResultDTO> addControversialToResult(@PathVariable long resultId, @PathVariable long controversialId)
    {
        FullResult fullResult = resultService.addControversialToResult(resultId,controversialId);
        return ResponseEntity.ok(fullResultMapper.toDTO(fullResult));
    }

    @DeleteMapping("/results")
    public ResponseEntity<String> deleteAllResults()
    {
        resultService.deleteAllResults();
        return ResponseEntity.ok("results were deleted");
    }
    /* -------------------------------------------
                      Teams
    ------------------------------------------- */

    @GetMapping("/teams")
    public ResponseEntity<List<TeamDTO>> getTeams()
    {
        return ResponseEntity.ok(teamService.getAllTeams().stream().map(x->teamMapper.toDTO(x)).toList());
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<TeamDTO> getTeam(@PathVariable long id)
    {
        return ResponseEntity.ok(teamMapper.toDTO(teamService.getTeamById(id)));
    }

    @PostMapping(value="/teams",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeamDTO> addNewTeam(@RequestBody TeamDTO teamDTO)
    {
        Team team = teamMapper.toEntity(teamDTO);
        teamService.saveTeam(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(teamDTO);
    }

    @DeleteMapping("/teams/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable long id)
    {
        teamService.deleteTeam(id);
        return ResponseEntity.ok("Team with ID = "+ id + "was deleted");
    }

    @PutMapping("/teams/{teamId}/players/{playerId}")
    public ResponseEntity<TeamDTO> addPlayerToTeam(@PathVariable long teamId, @PathVariable long playerId)
    {
        Team updatedTeam = teamService.addPlayerToTeam(teamId, playerId);
        return ResponseEntity.ok(teamMapper.toDTO(updatedTeam));
    }

    @DeleteMapping("/teams/{teamId}/players/{playerId}")
    public ResponseEntity<TeamDTO> deletePlayerFromTeam(@PathVariable long teamId, @PathVariable long playerId)
    {
        Team updatedTeam = teamService.deletePlayerFromTeam(teamId, playerId);
        return ResponseEntity.ok(teamMapper.toDTO(updatedTeam));
    }

    @PutMapping("/teams/{teamId}/flags/{flagId}")
    public ResponseEntity<TeamDTO> addFlagToTeam(@PathVariable long teamId, @PathVariable long flagId)
    {
        Team updatedTeam = teamService.addFlagToTeam(teamId, flagId);
        return ResponseEntity.ok(teamMapper.toDTO(updatedTeam));
    }

    @PutMapping("/teams/{teamId}/leagues/{leagueId}")
    public ResponseEntity<TeamDTO> addLeagueToTeam(@PathVariable long teamId, @PathVariable long leagueId)
    {
        Team updatedTeam = teamService.addLeagueToTeam(teamId, leagueId);
        return ResponseEntity.ok(teamMapper.toDTO(updatedTeam));
    }

    @DeleteMapping("/teams/{teamId}/flags/{flagId}")
    public ResponseEntity<TeamDTO> deleteFlagFromTeam(@PathVariable long teamId, @PathVariable long flagId)
    {
        Team updatedTeam = teamService.deleteFlagFromTeam(teamId, flagId);
        return ResponseEntity.ok(teamMapper.toDTO(updatedTeam));
    }

    @GetMapping("/teams/{teamId}/players")
    public ResponseEntity<List<PlayerDTO>> allPlayersFromTeam(@PathVariable long teamId){
        Team team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(team.getPlayers().stream().map(x->playerMapper.toDTO(x)).toList());
    }

    @GetMapping("/teams/{teamId}/tournaments")
    public ResponseEntity<List<TournamentDTO>> allTournamentsFromTeam(@PathVariable long teamId){
        Team team = teamService.getTeamById(teamId);
        List<TournamentDTO> tournaments = team.getTournaments().stream().map(x->tournamentMapper.toDTO(x)).toList();
        return ResponseEntity.ok(tournaments);
    }

    @GetMapping("/teams/{teamId}/flags")
    public ResponseEntity<List<FlagDTO>> allFlagsFromTeam(@PathVariable long teamId){
        Team team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(team.getFlags().stream().map(x->flagMapper.toDTO(x)).toList());
    }

    @GetMapping("/teams/{teamId}/transfers")
    public ResponseEntity<List<TransferDTO>> allTransfersFromTeam(@PathVariable long teamId){
        return ResponseEntity.ok(transferService.getTransfersForTeam(teamId).stream().map(x->transferMapper.toDTO(x)).toList());
    }

    @GetMapping("/teams/{teamId}/results")
    public ResponseEntity<List<FullResultDTO>> allResultsForTeam(@PathVariable long teamId){
        return ResponseEntity.ok(resultService.getResultsForTeam(teamId).stream().map(x->fullResultMapper.toDTO(x)).toList());
    }

    /* -------------------------------------------
                      Tournaments
    ------------------------------------------- */

    @GetMapping("/tournaments")
    public ResponseEntity<List<TournamentDTO>> getTournaments()
    {
        return ResponseEntity.ok(tournamentService.getAllTournaments().stream().map(x->tournamentMapper.toDTO(x)).toList());
    }

    @GetMapping("/tournaments/{id}")
    public ResponseEntity<TournamentDTO> getTournament(@PathVariable long id)
    {
        return ResponseEntity.ok(tournamentMapper.toDTO(tournamentService.getTournamentById(id)));
    }

    @PostMapping(value = "/tournaments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TournamentDTO> addNewTournament(@RequestBody TournamentDTO tournamentDto) {
        Tournament tournament = tournamentMapper.toEntity(tournamentDto);
        tournamentService.saveTournament(tournament);
        return ResponseEntity.status(HttpStatus.CREATED).body(tournamentDto);
    }

    @DeleteMapping("/tournaments/{id}")
    public ResponseEntity<String> deleteTournament(@PathVariable long id)
    {
        tournamentService.deleteTournament(id);
        return ResponseEntity.ok("Tournament with ID = "+ id + "was deleted");
    }

    @PutMapping("/tournaments/{tournamentId}/results/{resultId}")
    public ResponseEntity<TournamentDTO> addResultToTournament(@PathVariable long tournamentId, @PathVariable long resultId)
    {
        Tournament updatedTournament = tournamentService.addResultToTournament(tournamentId,resultId);
        return ResponseEntity.ok(tournamentMapper.toDTO(updatedTournament));
    }

    @DeleteMapping("/tournaments/{tournamentId}/results/{resultId}")
    public ResponseEntity<TournamentDTO> deleteResultFromTournament(@PathVariable long tournamentId, @PathVariable long resultId)
    {
        Tournament updatedTournament = tournamentService.deleteResultFromTournament(tournamentId,resultId);
        return ResponseEntity.ok(tournamentMapper.toDTO(updatedTournament));
    }

    @GetMapping("/tournaments/{tournamentId}/results")
    public ResponseEntity<List<FullResultDTO>> allResultsFromTournament(@PathVariable long tournamentId)
    {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        return ResponseEntity.ok(tournament.getResults().stream().map(x->fullResultMapper.toDTO(x)).toList());
    }

    @PutMapping("/tournaments/{tournamentId}/teams/{teamId}/players/{playerId}")
    public ResponseEntity<TournamentDTO> addPlayersTeamsToTournament(@PathVariable long tournamentId, @PathVariable long teamId, @PathVariable long playerId)
    {
        Tournament updatedTournament = tournamentService.addTeamAndPlayerToTournament(tournamentId,teamId,playerId);
        return ResponseEntity.ok(tournamentMapper.toDTO(updatedTournament));
    }

    @GetMapping("/tournaments/{tournamentId}/teams")
    public ResponseEntity<List<TeamDTO>> allTeamsFromTournament(@PathVariable long tournamentId)
    {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        return ResponseEntity.ok(tournament.getTeams().stream().map(x->teamMapper.toDTO(x)).toList());
    }

    @GetMapping("/tournaments/{tournamentId}/players")
    public ResponseEntity<List<PlayerDTO>> allPlayersFromTournament(@PathVariable long tournamentId)
    {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        return ResponseEntity.ok(tournament.getPlayers().stream().map(x->playerMapper.toDTO(x)).toList());
    }
    /* -------------------------------------------
                      Transfers
    ------------------------------------------- */


    @GetMapping("/transfers")
    public ResponseEntity<List<TransferDTO>> getTransfers()
    {
        return ResponseEntity.ok(transferService.getAllTransfers().stream().map(x->transferMapper.toDTO(x)).toList());
    }


    @GetMapping("/transfers/{id}")
    public ResponseEntity<TransferDTO> getTransfer(@PathVariable long id)
    {
        return ResponseEntity.ok(transferMapper.toDTO(transferService.getTransfer(id)));
    }

    @PostMapping("/transfers")
    public ResponseEntity<TransferDTO> addNewTransfer(@RequestBody TransferDTO transferDTO)
    {
        Transfer transfer = transferMapper.toEntity(transferDTO);
        transferService.saveTransfer(transfer);
        return ResponseEntity.status(HttpStatus.CREATED).body(transferDTO);
    }

    @DeleteMapping("/transfers/{id}")
    public ResponseEntity<String> deleteTransfer(@PathVariable long id)
    {
        transferService.deleteTransfer(id);
        return ResponseEntity.ok("Transfer with ID = "+ id + "was deleted");
    }

}
