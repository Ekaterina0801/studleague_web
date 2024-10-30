package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.*;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.factory.*;
import com.studleague.studleague.repository.TeamCompositionRepository;
import com.studleague.studleague.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/")
public class WebClientController {

    private RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL = "http://localhost:8080/api";

    private final ResultService resultService;
    private final ControversialService controversialService;
    private final TeamService teamService;

    @Autowired
    private TournamentFactory tournamentFactory;

    @Autowired
    private TeamFactory teamFactory;

    @Autowired
    private LeagueFactory leagueFactory;

    @Autowired
    private ControversialFactory controversialFactory;

    @Autowired
    private FullResultFactory fullResultFactory;

    @Autowired
    private PlayerFactory playerFactory;

    @Autowired
    private FlagFactory flagFactory;

    @Autowired
    private TransferFactory transferFactory;

    @Autowired
    private TeamCompositionRepository teamCompositionRepository;

    @Autowired
    private TeamCompositionService teamCompositionService;

    @Autowired
    public WebClientController(ResultService resultService, TournamentService tournamentService,
                               ControversialService controversialService, PlayerService playerService,
                               TeamService teamService) {
        this.resultService = resultService;
        this.controversialService = controversialService;
        this.teamService = teamService;
    }

    public static List<Integer> generateNumbers(int n) {
        return IntStream.rangeClosed(1, n).boxed().collect(Collectors.toList());
    }

    private List<LeagueDTO> fetchLeagues() {
        return fetchFromApi(BASE_URL + "/leagues", new ParameterizedTypeReference<List<LeagueDTO>>() {});
    }

    private <T> T fetchFromApi(String url, ParameterizedTypeReference<T> responseType) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            return responseEntity.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping("/leagues/{league_id}/results")
    public String leagueResultsView(@PathVariable long league_id, Model model) {
        List<LeagueDTO> leagueDTOs = fetchLeagues();
        model.addAttribute("leagues", leagueDTOs != null ? leagueDTOs : null);
        model.addAttribute("leagueId", league_id);
        return "leagueResults";
    }

    @RequestMapping("/leagues/{league_id}/tournaments/{tournament_id}/controversials")
    public String controversialsTournamentView(@PathVariable long league_id, @PathVariable long tournament_id, Model model) {
        TournamentDTO tournamentDTO = fetchFromApi(BASE_URL + "/tournaments/" + tournament_id, new ParameterizedTypeReference<TournamentDTO>() {});
        Tournament tournament = tournamentDTO != null ? tournamentFactory.toEntity(tournamentDTO) : null;

        List<LeagueDTO> leagueDTOs = fetchLeagues();
        List<League> leagues = leagueDTOs != null
                ? leagueDTOs.stream().map(leagueFactory::toEntity).toList()
                : null;

        List<Controversial> controversials = controversialService.getControversialsByTournamentId(tournament_id);


        model.addAttribute("leagueId", league_id);
        model.addAttribute("leagues", leagues);
        model.addAttribute("controversials", controversials);
        model.addAttribute("numbers", generateNumbers(controversials != null ? controversials.size() : 0));
        model.addAttribute("tournament", tournament);
        return "controversials";
    }

    @RequestMapping("/leagues/{league_id}/tournaments/{tournament_id}/results")
    public String tournamentResultsView(@PathVariable long league_id, @PathVariable long tournament_id, Model model) {
        List<FullResultDTO> resultsDTO = fetchFromApi(BASE_URL + "/tournaments/" + tournament_id + "/results", new ParameterizedTypeReference<List<FullResultDTO>>() {});
        List<FullResult> results = resultsDTO != null
                ? resultsDTO.stream().map(fullResultFactory::toEntity).toList()
                : null;

        TournamentDTO tournamentDTO = fetchFromApi(BASE_URL + "/tournaments/" + tournament_id, new ParameterizedTypeReference<TournamentDTO>() {});
        Tournament tournament = tournamentDTO != null ? tournamentFactory.toEntity(tournamentDTO) : null;

        List<LeagueDTO> leagueDTOs = fetchLeagues();
        List<League> leagues = leagueDTOs != null
                ? leagueDTOs.stream().map(leagueFactory::toEntity).toList()
                : null;

        List<TeamComposition> teamCompositions = teamCompositionService.findByTournamentId(tournament_id);
        model.addAttribute("tournament", tournament);
        model.addAttribute("leagues", leagues);
        model.addAttribute("leagueId", league_id);
        model.addAttribute("tableResult", resultService.fullResultsToTable(results));
        model.addAttribute("teamCompositions", teamCompositions);
        return "results-tournament";
    }

    @RequestMapping("/leagues/{league_id}/teams/{team_id}/results")
    public String teamResultsView(@PathVariable long league_id, @PathVariable long team_id, Model model) {
        model.addAttribute("leagues", fetchLeagues());
        model.addAttribute("leagueId", league_id);
        return "leagueResults";
    }

    @RequestMapping("/leagues/{league_id}/teams")
    public String teamsView(@PathVariable long league_id, Model model) {
        List<TeamDTO> teamDTOs = fetchFromApi(BASE_URL + "/leagues/" + league_id + "/teams", new ParameterizedTypeReference<List<TeamDTO>>() {});
        model.addAttribute("teams", teamDTOs != null ? teamDTOs.stream().map(teamFactory::toEntity).toList() : null);
        model.addAttribute("leagues", fetchLeagues());
        model.addAttribute("leagueId", league_id);
        return "teams";
    }

    @RequestMapping("/leagues/{league_id}/tournaments")
    public String tournamentsView(@PathVariable long league_id, Model model) {
        List<TournamentDTO> tournamentDTOs =  fetchFromApi(BASE_URL + "/leagues/" + league_id + "/tournaments", new ParameterizedTypeReference<>() {
        });
        model.addAttribute("tournaments", tournamentDTOs != null ? tournamentDTOs.stream().map(tournamentFactory::toEntity).toList() : null);
        model.addAttribute("leagues", fetchLeagues());
        model.addAttribute("leagueId", league_id);
        return "tournaments";
    }

    @RequestMapping("/leagues/{league_id}/teams/{team_id}")
    public String teamProfileView(@PathVariable long league_id, @PathVariable long team_id, Model model) {
        model.addAttribute("leagues", fetchLeagues());

        TeamDTO teamDTO = fetchFromApi(BASE_URL + "/teams/" + team_id, new ParameterizedTypeReference<TeamDTO>() {});
        Team team = teamDTO != null ? teamFactory.toEntity(teamDTO) : null;

        List<Flag> allFlags = fetchFromApi(BASE_URL + "/flags", new ParameterizedTypeReference<List<FlagDTO>>() {}).stream().map(x-> flagFactory.toEntity(x)).toList();
        List<Transfer> transfers = fetchFromApi(BASE_URL + "/teams/" + team_id + "/transfers", new ParameterizedTypeReference<List<TransferDTO>>() {}).stream().map(x-> transferFactory.toEntity(x)).toList();
        List<InfoTeamResults> resultsTable = teamService.getInfoTeamResultsByTeam(team_id);
        List<Integer> numbers = generateNumbers(team != null ? team.getTournaments().size() : 0);
        List<TeamComposition> teamCompositions = teamCompositionService.findByParentTeamId(team_id);
        //HashMap<Tournament, List<Player>> tournamentsPlayers = team != null ? teamService.getTournamentsPlayersByTeam(team.getId()) : new HashMap<>();

        model.addAttribute("leagueId", league_id);
        model.addAttribute("transfers", transfers);
        model.addAttribute("results", resultsTable);
        model.addAttribute("numbers", numbers);
        model.addAttribute("allFlags", allFlags);
        model.addAttribute("team", team);
        model.addAttribute("teamCompositions", teamCompositions);
        return "team-profile";
    }

    @RequestMapping("/leagues/{league_id}/teams/{team_id}/players/{player_id}")
    public String playerProfileView(@PathVariable long league_id, @PathVariable long team_id, @PathVariable long player_id, Model model) {
        model.addAttribute("leagues", fetchLeagues());

        PlayerDTO playerDTO = fetchFromApi(BASE_URL + "/players/" + player_id, new ParameterizedTypeReference<PlayerDTO>() {});
        Player player = playerDTO != null ? playerFactory.toEntity(playerDTO) : null;

        List<TeamDTO> allTeamsDTO = fetchFromApi(BASE_URL + "/teams", new ParameterizedTypeReference<List<TeamDTO>>() {});
        List<Team> allTeams = new ArrayList<>(allTeamsDTO != null ? allTeamsDTO.stream().map(teamFactory::toEntity).toList() : null);

        List<Team> currentTeams = player != null ? player.getTeams() : null;
        if (currentTeams != null) {
            allTeams.removeAll(currentTeams);
        }

        model.addAttribute("oldTeams", currentTeams);
        model.addAttribute("newTeams", allTeams);
        model.addAttribute("leagueId", league_id);
        model.addAttribute("player", player);

        return "player";
    }

    @RequestMapping("/players")
    public String playersView() {
        return "players-view";
    }
}

/*

@Controller
@RequestMapping("/")
public class WebClientController {

    //@Autowired
    private RestTemplate restTemplate = new RestTemplate();
    private String URL = "http://localhost:8080/api";

    private final FullResultService fullResultService;

    private final TournamentService tournamentService;

    private final ControversialService controversialService;

    private final PlayerService playerService;

    private final TeamService teamService;

    public WebClientController(FullResultService fullResultService,
                               TournamentService tournamentService,
                               ControversialService controversialService,
                               PlayerService playerService,
                               TeamService teamService) {
        this.fullResultService = fullResultService;
        this.tournamentService = tournamentService;
        this.controversialService = controversialService;
        this.playerService = playerService;
        this.teamService = teamService;
    }

    public static List<Integer> generateNumbers(int n) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add(i);
        }
        return list;
    }

    @RequestMapping("/leagues/{league_id}/results")
    public String leagueResultsView(@PathVariable long league_id, Model model) {
        ResponseEntity<List<League>> responseEntityLeagues = restTemplate.exchange(URL + "/leagues", HttpMethod.GET, null, new ParameterizedTypeReference<List<League>>() {
        });
        List<League> leagues = responseEntityLeagues.getBody();
        model.addAttribute("leagues", leagues);
        model.addAttribute("leagueId", league_id);
        return "leagueResults";
    }

    @RequestMapping("/leagues/{league_id}/tournaments/{tournament_id}/controversials")
    public String controversialsTournamentView(@PathVariable long league_id, @PathVariable long tournament_id, Model model) {
        ResponseEntity<List<League>> responseEntityLeagues = restTemplate.exchange(URL + "/leagues", HttpMethod.GET, null, new ParameterizedTypeReference<List<League>>() {
        });
        ResponseEntity<Tournament> responseEntityTournament = restTemplate.exchange(URL + "/tournaments/" + tournament_id, HttpMethod.GET, null, new ParameterizedTypeReference<Tournament>() {
        });
        Tournament tournament = responseEntityTournament.getBody();
        List<League> leagues = responseEntityLeagues.getBody();
        List<Controversial> controversials = controversialService.getControversialsByTournamentId(tournament_id);
        model.addAttribute("leagueId", league_id);
        model.addAttribute("leagues", leagues);
        model.addAttribute("controversials", controversials);
        model.addAttribute("numbers", generateNumbers(controversials.size()));
        model.addAttribute("tournament", tournament);
        return "controversials";
    }

    @RequestMapping("/leagues/{league_id}/tournaments/{tournament_id}/results")
    public String tournamentResultsView(@PathVariable long league_id, @PathVariable long tournament_id, Model model) {

        ResponseEntity<List<FullResult>> responseEntityResults = restTemplate.exchange(URL + "/tournaments/" + tournament_id + "/results", HttpMethod.GET, null, new ParameterizedTypeReference<List<FullResult>>() {
        });
        List<FullResult> results = responseEntityResults.getBody();
        List<InfoTeamResults> tableResult = fullResultService.fullResultsToTable(results);
        ResponseEntity<List<League>> responseEntityLeagues = restTemplate.exchange(URL + "/leagues", HttpMethod.GET, null, new ParameterizedTypeReference<List<League>>() {
        });
        ResponseEntity<Tournament> responseEntityTournament = restTemplate.exchange(URL + "/tournaments/" + tournament_id, HttpMethod.GET, null, new ParameterizedTypeReference<Tournament>() {
        });
        Tournament tournament = responseEntityTournament.getBody();
        List<League> leagues = responseEntityLeagues.getBody();
        List<Team> teams = tournament.getTeams();
        List<Player> players = tournament.getPlayers();
        HashMap<Team, List<Player>> teamsPlayers = new HashMap<>();
        for (Player player: players)
        {
            Team team = teamService.getTeamByPlayerIdAndLeagueId(player.getId(), league_id);
            if (!teamsPlayers.containsKey(team))
            {
                teamsPlayers.put(team, new ArrayList<>());
            }
            teamsPlayers.get(team).add(player);
        }
        model.addAttribute("tournament", tournament);
        //model.addAttribute("teamsPlayers", teamsPlayers);
        model.addAttribute("teams", teams);
        model.addAttribute("leagues", leagues);
        model.addAttribute("leagueId", league_id);
        model.addAttribute("tableResult", tableResult);
        return "results-tournament";
    }

    @RequestMapping("/leagues/{league_id}/teams/{team_id}/results")
    public String teamResultsView(@PathVariable long league_id, @PathVariable long team_id, Model model) {

        ResponseEntity<List<League>> responseEntityLeagues = restTemplate.exchange(URL + "/leagues", HttpMethod.GET, null, new ParameterizedTypeReference<List<League>>() {
        });
        List<League> leagues = responseEntityLeagues.getBody();
        model.addAttribute("leagues", leagues);
        model.addAttribute("leagueId", league_id);
        return "leagueResults";
    }


    @RequestMapping("/leagues/{league_id}/teams")
    public String teamsView(@PathVariable long league_id, Model model) {
        ResponseEntity<List<Team>> responseEntityTeams = restTemplate.exchange(URL + "/leagues/" + league_id + "/teams", HttpMethod.GET, null, new ParameterizedTypeReference<List<Team>>() {
        });
        List<Team> teams = responseEntityTeams.getBody();
        model.addAttribute("teams", teams);
        ResponseEntity<List<League>> responseEntityLeagues = restTemplate.exchange(URL + "/leagues", HttpMethod.GET, null, new ParameterizedTypeReference<List<League>>() {
        });
        List<League> leagues = responseEntityLeagues.getBody();
        model.addAttribute("leagues", leagues);
        model.addAttribute("leagueId", league_id);

        return "teams";
    }

    @RequestMapping("/leagues/{league_id}/tournaments")
    public String tournamentsView(@PathVariable long league_id, Model model) {
        ResponseEntity<List<Tournament>> responseEntityTournaments = restTemplate.exchange(URL + "/leagues/" + league_id + "/tournaments", HttpMethod.GET, null, new ParameterizedTypeReference<List<Tournament>>() {
        });
        List<Tournament> tournaments = responseEntityTournaments.getBody();
        model.addAttribute("tournaments", tournaments);
        ResponseEntity<List<League>> responseEntityLeagues = restTemplate.exchange(URL + "/leagues", HttpMethod.GET, null, new ParameterizedTypeReference<List<League>>() {
        });
        List<League> leagues = responseEntityLeagues.getBody();
        model.addAttribute("leagues", leagues);
        model.addAttribute("leagueId", league_id);
        ResponseEntity<List<Tournament>> responseEntityAllTournaments = restTemplate.exchange(URL + "/tournaments", HttpMethod.GET, null, new ParameterizedTypeReference<List<Tournament>>() {
        });
        List<Tournament> allTournaments = responseEntityAllTournaments.getBody();
        model.addAttribute("allTournaments", allTournaments);
        return "tournaments";
    }

    @RequestMapping("/leagues/{league_id}/teams/{team_id}")
    public String teamProfileView(@PathVariable long league_id, @PathVariable long team_id, Model model) {
        ResponseEntity<List<League>> responseEntityLeagues = restTemplate.exchange(URL + "/leagues", HttpMethod.GET, null, new ParameterizedTypeReference<List<League>>() {
        });
        List<League> leagues = responseEntityLeagues.getBody();
        model.addAttribute("leagues", leagues);
        ResponseEntity<List<Flag>> responseEntityAllFlags = restTemplate.exchange(URL + "/flags", HttpMethod.GET, null, new ParameterizedTypeReference<List<Flag>>() {
        });
        List<Flag> allFlags = responseEntityAllFlags.getBody();
        //ResponseEntity<List<Player>> responseEntityPlayers = restTemplate.exchange(URL + "/teams/"+team_id+"/players", HttpMethod.GET, null, new ParameterizedTypeReference<List<Player>>() {
        //});
        //List<Player> players = responseEntityPlayers.getBody();
        //ResponseEntity<List<Flag>> responseEntityFlags = restTemplate.exchange(URL + "/teams/"+team_id+"/flags", HttpMethod.GET, null, new ParameterizedTypeReference<List<Flag>>() {
        //});
        //List<Flag> flags = responseEntityFlags.getBody();
        ResponseEntity<List<Transfer>> responseEntityTransfers = restTemplate.exchange(URL + "/teams/" + team_id + "/transfers", HttpMethod.GET, null, new ParameterizedTypeReference<List<Transfer>>() {
        });
        List<Transfer> transfers = responseEntityTransfers.getBody();
        ResponseEntity<Team> responseEntityTeam = restTemplate.exchange(URL + "/teams/" + team_id, HttpMethod.GET, null, new ParameterizedTypeReference<Team>() {
        });
        Team team = responseEntityTeam.getBody();

        ResponseEntity<List<FullResult>> responseEntityResults = restTemplate.exchange(URL + "/teams/" + team_id+"/results", HttpMethod.GET, null, new ParameterizedTypeReference<List<FullResult>>() {
        });
        List<FullResult> results = responseEntityResults.getBody();
        List<InfoTeamResults> resultsTable = teamService.getInfoTeamResultsByTeam(team_id);
        List<Integer> numbers = generateNumbers(team.getTournaments().size());
        HashMap<Tournament, List<Player>> tournamentsPlayers = teamService.getTournamentsPlayersByTeam(team.getId());
        model.addAttribute("leagues", leagues);
        model.addAttribute("leagueId", league_id);
        model.addAttribute("transfers", transfers);
        model.addAttribute("results", resultsTable);
        model.addAttribute("numbers",numbers);
        model.addAttribute("allFlags", allFlags);
        model.addAttribute("team",team);
        //model.addAttribute("tournamentsPlayers",tournamentsPlayers);
        model.addAttribute("resultsTable",resultsTable);
        return "team-profile";
    }

    @RequestMapping("/leagues/{league_id}/teams/{team_id}/players/{player_id}")
    public String playerProfileView(@PathVariable long league_id, @PathVariable long team_id, @PathVariable long player_id, Model model) {
        ResponseEntity<List<League>> responseEntityLeagues = restTemplate.exchange(URL + "/leagues", HttpMethod.GET, null, new ParameterizedTypeReference<List<League>>() {
        });
        List<League> leagues = responseEntityLeagues.getBody();
        model.addAttribute("leagues", leagues);
        //ResponseEntity<List<Player>> responseEntityPlayers = restTemplate.exchange(URL + "/teams/"+team_id+"/players", HttpMethod.GET, null, new ParameterizedTypeReference<List<Player>>() {
        //});
        //List<Player> players = responseEntityPlayers.getBody();
        //ResponseEntity<List<Flag>> responseEntityFlags = restTemplate.exchange(URL + "/teams/"+team_id+"/flags", HttpMethod.GET, null, new ParameterizedTypeReference<List<Flag>>() {
        //});
        //List<Flag> flags = responseEntityFlags.getBody();
        ResponseEntity<Player> responseEntityPlayer = restTemplate.exchange(URL + "/players/" + player_id, HttpMethod.GET, null, new ParameterizedTypeReference<Player>() {
        });
        Player player = responseEntityPlayer.getBody();
        ResponseEntity<List<Team>> responseEntityAllTeams = restTemplate.exchange(URL + "/teams", HttpMethod.GET, null, new ParameterizedTypeReference<List<Team>>() {
        });

        List<Team> allTeams = responseEntityAllTeams.getBody();
        List<Team> currentTeams = player.getTeams();
        allTeams.removeAll(currentTeams);
        model.addAttribute("oldTeams", currentTeams);
        model.addAttribute("newTeams",allTeams);
        model.addAttribute("leagues", leagues);
        model.addAttribute("leagueId", league_id);
        model.addAttribute("player", player);

        return "player";
    }
    */
/*
    @RequestMapping("/leagues/{league_id}/teams/{team_id}")
    public String teamProfileView(@PathVariable int league_id,
                                  @PathVariable int team_id,
                                  Model model) {

        List<League> leagues = fetchLeagues();
        model.addAttribute("leagues", leagues);
        model.addAttribute("leagueId", league_id);

        Team team = fetchTeam(team_id);
        if (team != null) {
            model.addAttribute("players", team.getPlayers());
            model.addAttribute("flags", team.getFlags());
            model.addAttribute("tournaments", team.getTournaments());

            List<Transfer> transfers = fetchTransfers(team_id);
            model.addAttribute("transfers", transfers);
            model.addAttribute("teamName", team.getTeamName());

            return "team-profile";
        } else {
            return "error";
        }
    }*//*


    private List<League> fetchLeagues() {
        try {
            ResponseEntity<List<League>> responseEntity = restTemplate.exchange(
                    URL + "/leagues",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<League>>() {
                    }
            );
            return responseEntity.getBody();
        } catch (Exception e) {
            // Log the exception
            return Collections.emptyList(); // Return an empty list or handle appropriately
        }
    }

    private Team fetchTeam(long team_id) {
        try {
            ResponseEntity<Team> responseEntity = restTemplate.exchange(
                    URL + "/teams/" + team_id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Team>() {
                    }
            );
            return responseEntity.getBody();
        } catch (Exception e) {
            // Log the exception
            return null; // Return null or handle appropriately
        }
    }

    private List<Transfer> fetchTransfers(long team_id) {
        try {
            ResponseEntity<List<Transfer>> responseEntity = restTemplate.exchange(
                    URL + "/teams/" + team_id + "/transfers",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Transfer>>() {
                    }
            );
            return responseEntity.getBody();
        } catch (Exception e) {
            // Log the exception
            return Collections.emptyList(); // Return an empty list or handle appropriately
        }
    }

    @RequestMapping("/players")
    public String playersView() {
        return "players-view";
    }


}
*/
