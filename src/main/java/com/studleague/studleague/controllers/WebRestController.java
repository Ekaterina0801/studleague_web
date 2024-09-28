package com.studleague.studleague.controllers;
import com.studleague.studleague.dto.*;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.mappings.*;
import com.studleague.studleague.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    public FullResultService fullResultService;

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
    public List<Flag> getFlags()
    {
        return flagService.getAllFlags();
    }

    @GetMapping("/results")
    public List<FullResult> getResults()
    {
        return fullResultService.getAllFullResults();
    }

    @GetMapping("/leagues")
    public List<League> getLeagues()
    {
        return leagueService.getAllLeagues();
    }

    @GetMapping("/players")
    public List<Player> getPlayers()
    {
        List<Player> players = playerService.getAllPlayers();
        System.out.println(players);
        return players;
    }

    @GetMapping("/controversials")
    public List<Controversial> getControversials()
    {
        List<Controversial> controversials = controversialService.getAllControversials();
        return controversials;
    }

    @GetMapping("/teams")
    public List<Team> getTeams()
    {
        return teamService.getAllTeams();
    }

    @GetMapping("/tournaments")
    public List<Tournament> getTournaments()
    {
        return tournamentService.getAllTournaments();
    }

    @GetMapping("/transfers")
    public List<Transfer> getTransfers()
    {
        return transferService.getAllTransfers();
    }

    @GetMapping("/flags/{id}")
    public Flag getFlag(@PathVariable int id)
    {
        Flag flag = flagService.getFlagById(id);
        return flag;
    }

    @GetMapping("/results/{id}")
    public FullResult getResult(@PathVariable int id)
    {
        FullResult fullResult = fullResultService.getFullResultById(id);
        return fullResult;
    }

    @GetMapping("/leagues/{id}")
    public League getLeague(@PathVariable int id)
    {
        League league = leagueService.getLeagueById(id);
        return league;
    }

    @GetMapping("/players/{id}")
    public Player getPlayer(@PathVariable int id)
    {
        Player player = playerService.getPlayerById(id);
        return player;
    }

    @GetMapping("/teams/{id}")
    public Team getTeam(@PathVariable int id)
    {
        Team team = teamService.getTeamById(id);
        return team;
    }

    @GetMapping("/tournaments/{id}")
    public Tournament getTournament(@PathVariable int id)
    {
        Tournament tournament = tournamentService.getTournamentById(id);
        return tournament;
    }

    @GetMapping("/transfers/{id}")
    public Transfer getTransfer(@PathVariable int id)
    {
        Transfer transfer = transferService.getTransfer(id);
        return transfer;
    }

    @PostMapping("/flags")
    public Flag addNewFlag(@RequestBody Flag flag)
    {
        flagService.saveFlag(flag);
        return flag;
    }

    @PostMapping("/results")
    public FullResultDTO addNewResult(@RequestBody FullResultDTO fullResultDTO)
    {
        FullResult fullResult = fullResultMapper.toEntity(fullResultDTO);
        fullResultService.saveFullResult(fullResult);
        return fullResultDTO;
    }

    @PostMapping("/leagues")
    public League addNewLeague(@RequestBody League league)
    {
        leagueService.saveLeague(league);
        return league;
    }

    @PostMapping("/players")
    public PlayerDTO addNewPlayer(@RequestBody PlayerDTO playerDTO)
    {
        Player player = playerMapper.toEntity(playerDTO);
        playerService.savePlayer(player);
        return playerDTO;
    }


    @PostMapping ("/controversials")
    public Controversial addNewControversial(@RequestBody Controversial controversial)
    {
        controversialService.saveControversial(controversial);
        return controversial;
    }

    @PostMapping(value="/teams",consumes = MediaType.APPLICATION_JSON_VALUE)
    public TeamDTO addNewTeam(@RequestBody TeamDTO teamDTO)
    {
        Team team = teamMapper.toEntity(teamDTO);
        teamService.saveTeam(team);
        return teamDTO;
    }

    @PostMapping(value = "/tournaments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TournamentDto addNewTournament(@RequestBody TournamentDto tournamentDto) {
        Tournament tournament = tournamentMapper.toEntity(tournamentDto);
        tournamentService.saveTournament(tournament);
        return tournamentDto;
    }


    @PostMapping("/transfers")
    public TransferDTO addNewTransfer(@RequestBody TransferDTO transferDTO)
    {
        Transfer transfer = transferMapper.toEntity(transferDTO);
        transferService.saveTransfer(transfer);
        return transferDTO;
    }

    @DeleteMapping("/flags/{id}")
    public String deleteFlag(@PathVariable int id)
    {
        flagService.deleteFlag(id);
        return "Flag with ID = "+ id + "was deleted";
    }

    @DeleteMapping("/controversials/{id}")
    public String deleteControversial(@PathVariable int id)
    {
        controversialService.deleteControversial(id);
        return "Controversial with ID = "+ id + "was deleted";
    }


    @DeleteMapping("/results/{id}")
    public String deleteFullResult(@PathVariable int id)
    {
        fullResultService.deleteFullResult(id);
        return "FullResult with ID = "+ id + "was deleted";
    }

    @DeleteMapping("/leagues/{id}")
    public String deleteLeague(@PathVariable int id)
    {
        leagueService.deleteLeague(id);
        return "League with ID = "+ id + "was deleted";
    }

    @DeleteMapping("/players/{id}")
    public String deletePlayer(@PathVariable int id)
    {
        playerService.deletePlayer(id);
        return "Player with ID = "+ id + "was deleted";
    }

    @DeleteMapping("/teams/{id}")
    public String deleteTeam(@PathVariable int id)
    {
        teamService.deleteTeam(id);
        return "Team with ID = "+ id + "was deleted";
    }

    @DeleteMapping("/tournaments/{id}")
    public String deleteTournament(@PathVariable int id)
    {
        tournamentService.deleteTournament(id);
        return "Tournament with ID = "+ id + "was deleted";
    }

    @DeleteMapping("/transfers/{id}")
    public String deleteTransfer(@PathVariable int id)
    {
        transferService.deleteTransfer(id);
        return "Transfer with ID = "+ id + "was deleted";
    }

    //------------TEAMS------------
    @PutMapping("/teams/{team_id}/players/{player_id}")
    public ResponseEntity<Team> addPlayerToTeam(@PathVariable int team_id, @PathVariable int player_id)
    {
        Team updatedTeam = teamService.addPlayerToTeam(team_id, player_id);
        return ResponseEntity.ok(updatedTeam);
    }

    @DeleteMapping("/teams/{team_id}/players/{player_id}")
    public ResponseEntity<Team> deletePlayerFromTeam(@PathVariable int team_id, @PathVariable int player_id)
    {
        Team updatedTeam = teamService.deletePlayerFromTeam(team_id, player_id);
        return ResponseEntity.ok(updatedTeam);
    }

    @PutMapping("/teams/{team_id}/flags/{flag_id}")
    public ResponseEntity<Team> addFlagToTeam(@PathVariable int team_id, @PathVariable int flag_id)
    {
        Team updatedTeam = teamService.addFlagToTeam(team_id, flag_id);
        return ResponseEntity.ok(updatedTeam);
    }

    @PutMapping("/teams/{team_id}/leagues/{league_id}")
    public ResponseEntity<Team> addLeagueToTeam(@PathVariable int team_id, @PathVariable int league_id)
    {
        Team updatedTeam = teamService.addLeagueToTeam(team_id, league_id);
        return ResponseEntity.ok(updatedTeam);
    }

    @DeleteMapping("/teams/{team_id}/flags/{flag_id}")
    public ResponseEntity<Team> deleteFlagFromTeam(@PathVariable int team_id, @PathVariable int flag_id)
    {
        Team updatedTeam = teamService.deleteFlagFromTeam(team_id, flag_id);
        return ResponseEntity.ok(updatedTeam);
    }

    @GetMapping("/teams/{team_id}/players")
    public List<Player> allPlayersFromTeam(@PathVariable int team_id){
        Team team = teamService.getTeamById(team_id);
        List<Player> players = team.getPlayers();
        return players;
    }

    @GetMapping("/teams/{team_id}/tournaments")
    public List<Tournament> allTournamentsFromTeam(@PathVariable int team_id){
        Team team = teamService.getTeamById(team_id);
        List<Tournament> tournaments = team.getTournaments();
        return tournaments;
    }

    @GetMapping("/teams/{team_id}/flags")
    public List<Flag> allFlagsFromTeam(@PathVariable int team_id){
        Team team = teamService.getTeamById(team_id);
        List<Flag> flags = team.getFlags();
        return flags;
    }

    @GetMapping("/teams/{team_id}/transfers")
    public List<Transfer> allTransfersFromTeam(@PathVariable int team_id){
        List<Transfer> transfers = transferService.getTransfersForTeam(team_id);
        return transfers;
    }

    @GetMapping("/players/{player_id}/transfers")
    public List<Transfer> allTransfersFromPlayer(@PathVariable int player_id){
        List<Transfer> transfers = transferService.getTransfersForPlayer(player_id);
        return transfers;
    }

    @GetMapping("/players/{player_id}/tournaments")
    public List<Tournament> allTournamentsFromPlayer(@PathVariable int player_id){
        Player player = playerService.getPlayerById(player_id);
        List<Tournament> tournaments  = player.getTournaments();
        return tournaments;
    }

    @GetMapping("/teams/{team_id}/results")
    public List<FullResult> allResultsForTeam(@PathVariable int team_id){
        List<FullResult> results = fullResultService.getResultsForTeam(team_id);
        return results;
    }
    //------------LEAGUES------------
    @PutMapping("/leagues/{league_id}/tournaments/{tournament_id}")
    public ResponseEntity<League> addTournamentToLeague(@PathVariable int league_id, @PathVariable int tournament_id)
    {
        League updatedLeague = leagueService.addTournamentToLeague(league_id,tournament_id);
        return ResponseEntity.ok(updatedLeague);
    }

    @DeleteMapping("/leagues/{league_id}/tournaments/{tournament_id}")
    public ResponseEntity<League> deleteTournamentFromLeague(@PathVariable int league_id, @PathVariable int tournament_id)
    {
        League updatedLeague = leagueService.deleteTournamentToLeague(league_id,tournament_id);
        return ResponseEntity.ok(updatedLeague);
    }

    @GetMapping("/leagues/{league_id}/tournaments")
    public List<Tournament> allTournamentsFromLeague(@PathVariable int league_id)
    {
        League league = leagueService.getLeagueById(league_id);
        List<Tournament> tournaments = league.getTournaments();
        return tournaments;
    }

    @GetMapping("/leagues/{league_id}/players/{player_id}/team")
    public Team teamFromLeague(@PathVariable int league_id, @PathVariable int player_id)
    {
        Team team = playerService.getTeamOfPlayerByLeague(league_id,player_id);
        return team;
    }

    @GetMapping("/leagues/{league_id}/teams")
    public List<Team> allTeamsFromLeague(@PathVariable int league_id){
        List<Team> teams = teamService.teamsByLeague(league_id);
        return teams;
    }

    //------------TOURNAMENTS------------
    @PutMapping("/tournaments/{tournament_id}/results/{result_id}")
    public ResponseEntity<Tournament> addResultToTournament(@PathVariable int tournament_id, @PathVariable int result_id)
    {
        Tournament updatedTournament = tournamentService.addResultToTournament(tournament_id,result_id);
        return ResponseEntity.ok(updatedTournament);
    }

    @DeleteMapping("/tournaments/{tournament_id}/results/{result_id}")
    public ResponseEntity<Tournament> deleteResultFromTournament(@PathVariable int tournament_id, @PathVariable int result_id)
    {
        Tournament updatedTournament = tournamentService.deleteResultFromTournament(tournament_id,result_id);
        return ResponseEntity.ok(updatedTournament);
    }

    @GetMapping("/tournaments/{tournament_id}/results")
    public List<FullResult> allResultsFromTournament(@PathVariable int tournament_id)
    {
        Tournament tournament = tournamentService.getTournamentById(tournament_id);
        List<FullResult> results = tournament.getResults();
        return results;
    }

    @PutMapping("/tournaments/{tournament_id}/teams/{team_id}/players/{player_id}")
    public ResponseEntity<Tournament> addPlayersTeamsToTournament(@PathVariable int tournament_id, @PathVariable int team_id, @PathVariable int player_id)
    {
        Tournament updatedTournament = tournamentService.addTeamAndPlayerToTournament(tournament_id,team_id,player_id);
        return ResponseEntity.ok(updatedTournament);
    }

    @GetMapping("/tournaments/{tournament_id}/teams")
    public List<Team> allTeamsFromTournament(@PathVariable int tournament_id)
    {
        Tournament tournament = tournamentService.getTournamentById(tournament_id);
        List<Team> teams = tournament.getTeams();
        return teams;
    }

    @GetMapping("/tournaments/{tournament_id}/players")
    public List<Player> allPlayersFromTournament(@PathVariable int tournament_id)
    {
        Tournament tournament = tournamentService.getTournamentById(tournament_id);
        List<Player> players = tournament.getPlayers();
        return players;
    }

    @GetMapping("/results/{result_id}/controversials")
    public List<Controversial> allControversialsFromResult(@PathVariable int result_id)
    {
        FullResult fullResult = fullResultService.getFullResultById(result_id);
        List<Controversial> controversials = fullResult.getControversials();
        return controversials;
    }

    @PutMapping("/results/{result_id}/controversials/{controversial_id}")
    public ResponseEntity<FullResult> addControversialToResult(@PathVariable int result_id, @PathVariable int controversial_id)
    {
        FullResult fullResult = fullResultService.addControversialToResult(result_id,controversial_id);
        return ResponseEntity.ok(fullResult);
    }




}
