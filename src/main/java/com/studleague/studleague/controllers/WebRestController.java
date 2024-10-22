package com.studleague.studleague.controllers;
import com.studleague.studleague.dto.*;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.mappings.*;
import com.studleague.studleague.services.interfaces.*;
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

    @GetMapping("/flags")
    public ResponseEntity<List<Flag>> getFlags()
    {
        return ResponseEntity.ok(flagService.getAllFlags());
    }

    @GetMapping("/results")
    public ResponseEntity<List<FullResult>> getResults()
    {
        return ResponseEntity.ok(resultService.getAllFullResults());
    }

    @GetMapping("/leagues")
    public ResponseEntity<List<League>> getLeagues()
    {
        return ResponseEntity.ok(leagueService.getAllLeagues());
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayers()
    {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @GetMapping("/controversials")
    public ResponseEntity<List<Controversial>> getControversials()
    {
        return ResponseEntity.ok(controversialService.getAllControversials());
    }

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getTeams()
    {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/tournaments")
    public ResponseEntity<List<Tournament>> getTournaments()
    {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    @GetMapping("/transfers")
    public ResponseEntity<List<Transfer>> getTransfers()
    {
        return ResponseEntity.ok(transferService.getAllTransfers());
    }

    @GetMapping("/flags/{id}")
    public ResponseEntity<Flag> getFlag(@PathVariable long id)
    {
        return ResponseEntity.ok(flagService.getFlagById(id));
    }

    @GetMapping("/results/{id}")
    public ResponseEntity<FullResult> getResult(@PathVariable long id)
    {
        return ResponseEntity.ok(resultService.getFullResultById(id));
    }

    @GetMapping("/leagues/{id}")
    public ResponseEntity<League> getLeague(@PathVariable long id)
    {
        return ResponseEntity.ok(leagueService.getLeagueById(id));
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable long id)
    {
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable long id)
    {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @GetMapping("/tournaments/{id}")
    public ResponseEntity<Tournament> getTournament(@PathVariable long id)
    {
        return ResponseEntity.ok(tournamentService.getTournamentById(id));
    }

    @GetMapping("/transfers/{id}")
    public ResponseEntity<Transfer> getTransfer(@PathVariable long id)
    {
        return ResponseEntity.ok(transferService.getTransfer(id));
    }

    @PostMapping("/flags")
    public ResponseEntity<Flag> addNewFlag(@RequestBody Flag flag)
    {
        flagService.saveFlag(flag);
        return ResponseEntity.status(HttpStatus.CREATED).body(flag);
    }

    @PostMapping("/results")
    public ResponseEntity<FullResultDTO> addNewResult(@RequestBody FullResultDTO fullResultDTO)
    {
        FullResult fullResult = fullResultMapper.toEntity(fullResultDTO);
        resultService.saveFullResult(fullResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(fullResultDTO);
    }

    @PostMapping("/leagues")
    public ResponseEntity<League> addNewLeague(@RequestBody League league)
    {
        leagueService.saveLeague(league);
        return ResponseEntity.status(HttpStatus.CREATED).body(league);
    }

    @PostMapping("/players")
    public ResponseEntity<PlayerDTO> addNewPlayer(@RequestBody PlayerDTO playerDTO)
    {
        Player player = playerMapper.toEntity(playerDTO);
        playerService.savePlayer(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(playerDTO);
    }


    @PostMapping ("/controversials")
    public ResponseEntity<Controversial> addNewControversial(@RequestBody Controversial controversial)
    {
        controversialService.saveControversial(controversial);
        return ResponseEntity.status(HttpStatus.CREATED).body(controversial);
    }

    @PostMapping(value="/teams",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeamDTO> addNewTeam(@RequestBody TeamDTO teamDTO)
    {
        Team team = teamMapper.toEntity(teamDTO);
        teamService.saveTeam(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(teamDTO);
    }

    @PostMapping(value = "/tournaments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TournamentDto> addNewTournament(@RequestBody TournamentDto tournamentDto) {
        Tournament tournament = tournamentMapper.toEntity(tournamentDto);
        tournamentService.saveTournament(tournament);
        return ResponseEntity.status(HttpStatus.CREATED).body(tournamentDto);
    }


    @PostMapping("/transfers")
    public ResponseEntity<TransferDTO> addNewTransfer(@RequestBody TransferDTO transferDTO)
    {
        Transfer transfer = transferMapper.toEntity(transferDTO);
        transferService.saveTransfer(transfer);
        return ResponseEntity.status(HttpStatus.CREATED).body(transferDTO);
    }

    @DeleteMapping("/flags/{id}")
    public ResponseEntity<String> deleteFlag(@PathVariable long id)
    {
        flagService.deleteFlag(id);
        return ResponseEntity.ok("Flag with ID = "+ id + "was deleted");
    }

    @DeleteMapping("/controversials/{id}")
    public ResponseEntity<String> deleteControversial(@PathVariable long id)
    {
        controversialService.deleteControversial(id);
        return ResponseEntity.ok("Controversial with ID = "+ id + "was deleted");
    }


    @DeleteMapping("/results/{id}")
    public ResponseEntity<String> deleteFullResult(@PathVariable long id)
    {
        resultService.deleteFullResult(id);
        return ResponseEntity.ok("FullResult with ID = "+ id + "was deleted");
    }

    @DeleteMapping("/leagues/{id}")
    public ResponseEntity<String> deleteLeague(@PathVariable long id)
    {
        leagueService.deleteLeague(id);
        return ResponseEntity.ok("League with ID = "+ id + "was deleted");
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable long id)
    {
        playerService.deletePlayer(id);
        return ResponseEntity.ok("Player with ID = "+ id + "was deleted");
    }

    @DeleteMapping("/teams/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable long id)
    {
        teamService.deleteTeam(id);
        return ResponseEntity.ok("Team with ID = "+ id + "was deleted");
    }

    @DeleteMapping("/tournaments/{id}")
    public ResponseEntity<String> deleteTournament(@PathVariable long id)
    {
        tournamentService.deleteTournament(id);
        return ResponseEntity.ok("Tournament with ID = "+ id + "was deleted");
    }

    @DeleteMapping("/transfers/{id}")
    public ResponseEntity<String> deleteTransfer(@PathVariable long id)
    {
        transferService.deleteTransfer(id);
        return ResponseEntity.ok("Transfer with ID = "+ id + "was deleted");
    }

    //------------TEAMS------------
    @PutMapping("/teams/{teamId}/players/{playerId}")
    public ResponseEntity<Team> addPlayerToTeam(@PathVariable long teamId, @PathVariable long playerId)
    {
        Team updatedTeam = teamService.addPlayerToTeam(teamId, playerId);
        return ResponseEntity.ok(updatedTeam);
    }

    @DeleteMapping("/teams/{teamId}/players/{playerId}")
    public ResponseEntity<Team> deletePlayerFromTeam(@PathVariable long teamId, @PathVariable long playerId)
    {
        Team updatedTeam = teamService.deletePlayerFromTeam(teamId, playerId);
        return ResponseEntity.ok(updatedTeam);
    }

    @PutMapping("/teams/{teamId}/flags/{flagId}")
    public ResponseEntity<Team> addFlagToTeam(@PathVariable long teamId, @PathVariable long flagId)
    {
        Team updatedTeam = teamService.addFlagToTeam(teamId, flagId);
        return ResponseEntity.ok(updatedTeam);
    }

    @PutMapping("/teams/{teamId}/leagues/{leagueId}")
    public ResponseEntity<Team> addLeagueToTeam(@PathVariable long teamId, @PathVariable long leagueId)
    {
        Team updatedTeam = teamService.addLeagueToTeam(teamId, leagueId);
        return ResponseEntity.ok(updatedTeam);
    }

    @DeleteMapping("/teams/{teamId}/flags/{flagId}")
    public ResponseEntity<Team> deleteFlagFromTeam(@PathVariable long teamId, @PathVariable long flagId)
    {
        Team updatedTeam = teamService.deleteFlagFromTeam(teamId, flagId);
        return ResponseEntity.ok(updatedTeam);
    }

    @GetMapping("/teams/{teamId}/players")
    public ResponseEntity<List<Player>> allPlayersFromTeam(@PathVariable long teamId){
        Team team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(team.getPlayers());
    }

    @GetMapping("/teams/{teamId}/tournaments")
    public ResponseEntity<List<Tournament>> allTournamentsFromTeam(@PathVariable long teamId){
        Team team = teamService.getTeamById(teamId);
        List<Tournament> tournaments = team.getTournaments();
        return ResponseEntity.ok(tournaments);
    }

    @GetMapping("/teams/{teamId}/flags")
    public ResponseEntity<List<Flag>> allFlagsFromTeam(@PathVariable long teamId){
        Team team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(team.getFlags());
    }

    @GetMapping("/teams/{teamId}/transfers")
    public ResponseEntity<List<Transfer>> allTransfersFromTeam(@PathVariable long teamId){
        return ResponseEntity.ok(transferService.getTransfersForTeam(teamId));
    }

    @GetMapping("/players/{playerId}/transfers")
    public ResponseEntity<List<Transfer>> allTransfersFromPlayer(@PathVariable long playerId){
        return ResponseEntity.ok(transferService.getTransfersForPlayer(playerId));
    }

    @GetMapping("/players/{playerId}/tournaments")
    public ResponseEntity<List<Tournament>> allTournamentsFromPlayer(@PathVariable long playerId){
        Player player = playerService.getPlayerById(playerId);
        return ResponseEntity.ok(player.getTournaments());
    }

    @GetMapping("/teams/{teamId}/results")
    public ResponseEntity<List<FullResult>> allResultsForTeam(@PathVariable long teamId){
        return ResponseEntity.ok(resultService.getResultsForTeam(teamId));
    }
    //------------LEAGUES------------
    @PutMapping("/leagues/{leagueId}/tournaments/{tournamentId}")
    public ResponseEntity<League> addTournamentToLeague(@PathVariable long leagueId, @PathVariable long tournamentId)
    {
        League updatedLeague = leagueService.addTournamentToLeague(leagueId,tournamentId);
        return ResponseEntity.ok(updatedLeague);
    }

    @DeleteMapping("/leagues/{leagueId}/tournaments/{tournamentId}")
    public ResponseEntity<League> deleteTournamentFromLeague(@PathVariable long leagueId, @PathVariable long tournamentId)
    {
        League updatedLeague = leagueService.deleteTournamentToLeague(leagueId,tournamentId);
        return ResponseEntity.ok(updatedLeague);
    }

    @GetMapping("/leagues/{leagueId}/tournaments")
    public ResponseEntity<List<Tournament>> allTournamentsFromLeague(@PathVariable long leagueId)
    {
        League league = leagueService.getLeagueById(leagueId);
        return ResponseEntity.ok(league.getTournaments());
    }

    @GetMapping("/leagues/{leagueId}/players/{playerId}/team")
    public ResponseEntity<Team> teamFromLeague(@PathVariable long leagueId, @PathVariable long playerId)
    {
        return ResponseEntity.ok(teamService.getTeamByPlayerIdAndLeagueId(leagueId,playerId));
    }

    @GetMapping("/leagues/{leagueId}/teams")
    public ResponseEntity<List<Team>> allTeamsFromLeague(@PathVariable long leagueId){
        return ResponseEntity.ok(teamService.teamsByLeague(leagueId));
    }

    //------------TOURNAMENTS------------
    @PutMapping("/tournaments/{tournamentId}/results/{resultId}")
    public ResponseEntity<Tournament> addResultToTournament(@PathVariable long tournamentId, @PathVariable long resultId)
    {
        Tournament updatedTournament = tournamentService.addResultToTournament(tournamentId,resultId);
        return ResponseEntity.ok(updatedTournament);
    }

    @DeleteMapping("/tournaments/{tournamentId}/results/{resultId}")
    public ResponseEntity<Tournament> deleteResultFromTournament(@PathVariable long tournamentId, @PathVariable long resultId)
    {
        Tournament updatedTournament = tournamentService.deleteResultFromTournament(tournamentId,resultId);
        return ResponseEntity.ok(updatedTournament);
    }

    @GetMapping("/tournaments/{tournamentId}/results")
    public ResponseEntity<List<FullResult>> allResultsFromTournament(@PathVariable long tournamentId)
    {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        return ResponseEntity.ok(tournament.getResults());
    }

    @PutMapping("/tournaments/{tournamentId}/teams/{teamId}/players/{playerId}")
    public ResponseEntity<Tournament> addPlayersTeamsToTournament(@PathVariable long tournamentId, @PathVariable long teamId, @PathVariable long playerId)
    {
        Tournament updatedTournament = tournamentService.addTeamAndPlayerToTournament(tournamentId,teamId,playerId);
        return ResponseEntity.ok(updatedTournament);
    }

    @GetMapping("/tournaments/{tournamentId}/teams")
    public ResponseEntity<List<Team>> allTeamsFromTournament(@PathVariable long tournamentId)
    {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        return ResponseEntity.ok(tournament.getTeams());
    }

    @GetMapping("/tournaments/{tournamentId}/players")
    public ResponseEntity<List<Player>> allPlayersFromTournament(@PathVariable long tournamentId)
    {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        return ResponseEntity.ok(tournament.getPlayers());
    }

    @GetMapping("/results/{resultId}/controversials")
    public ResponseEntity<List<Controversial>> allControversialsFromResult(@PathVariable long resultId)
    {
        FullResult fullResult = resultService.getFullResultById(resultId);
        return ResponseEntity.ok(fullResult.getControversials());
    }

    @PutMapping("/results/{resultId}/controversials/{controversialId}")
    public ResponseEntity<FullResult> addControversialToResult(@PathVariable long resultId, @PathVariable long controversialId)
    {
        FullResult fullResult = resultService.addControversialToResult(resultId,controversialId);
        return ResponseEntity.ok(fullResult);
    }

}
