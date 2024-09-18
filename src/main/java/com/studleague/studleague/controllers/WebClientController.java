package com.studleague.studleague.controllers;

import com.studleague.studleague.dto.ResultTableRow;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.services.interfaces.FullResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/")
public class WebClientController {

    //@Autowired
    private RestTemplate restTemplate = new RestTemplate();
    private String URL = "http://localhost:8080/api";

    private final FullResultService fullResultService;

    public WebClientController(FullResultService fullResultService) {
        this.fullResultService = fullResultService;
    }


    @RequestMapping("/leagues/{league_id}/results")
    public String leagueResultsView(@PathVariable int league_id, Model model)
    {

        return "leagueResults";
    }

    @RequestMapping("/leagues/{league_id}/tournaments/{tournament_id}/results")
    public String tournamentResultsView(@PathVariable int league_id, @PathVariable int tournament_id, Model model)
    {

        ResponseEntity<List<FullResult>> responseEntityResults = restTemplate.exchange(URL + "/tournaments/"+tournament_id+"/results", HttpMethod.GET, null, new ParameterizedTypeReference<List<FullResult>>() {
        });
        List<FullResult> results = responseEntityResults.getBody();
        List<ResultTableRow> tableResult = fullResultService.fullResultsToTable(tournament_id, results);
        ResponseEntity<List<League>> responseEntityLeagues = restTemplate.exchange(URL + "/leagues", HttpMethod.GET, null, new ParameterizedTypeReference<List<League>>() {
        });
        ResponseEntity<Tournament> responseEntityTournament = restTemplate.exchange(URL + "/tournaments/"+tournament_id, HttpMethod.GET, null, new ParameterizedTypeReference<Tournament>() {
        });
        Tournament tournament = responseEntityTournament.getBody();
        List<League> leagues = responseEntityLeagues.getBody();
        model.addAttribute("tournament",tournament);
        model.addAttribute("leagues",leagues);
        model.addAttribute("leagueId",league_id);
        model.addAttribute("tableResult", tableResult);
        return "results-tournament";
    }

    @RequestMapping("/leagues/{league_id}/teams/{team_id}/results")
    public String teamResultsView(@PathVariable int league_id, @PathVariable int team_id, Model model)
    {


        return "leagueResults";
    }



    @RequestMapping("/leagues/{league_id}/teams")
    public String teamsView(@PathVariable int league_id, Model model)
    {
        ResponseEntity<List<Team>> responseEntityTeams = restTemplate.exchange(URL + "/leagues/"+league_id+"/teams", HttpMethod.GET, null, new ParameterizedTypeReference<List<Team>>() {
        });
        List<Team> teams = responseEntityTeams.getBody();
        model.addAttribute("teams", teams);
        ResponseEntity<List<League>> responseEntityLeagues = restTemplate.exchange(URL + "/leagues", HttpMethod.GET, null, new ParameterizedTypeReference<List<League>>() {
        });
        List<League> leagues = responseEntityLeagues.getBody();
        model.addAttribute("leagues",leagues);
        model.addAttribute("leagueId",league_id);
        return "teams";
    }

    @RequestMapping("/leagues/{league_id}/tournaments")
    public String tournamentsView(@PathVariable int league_id,Model model)
    {
        ResponseEntity<List<Tournament>> responseEntityTournaments = restTemplate.exchange(URL + "/leagues/"+league_id+"/tournaments", HttpMethod.GET, null, new ParameterizedTypeReference<List<Tournament>>() {
        });
        List<Tournament> tournaments = responseEntityTournaments.getBody();
        model.addAttribute("tournaments", tournaments);
        ResponseEntity<List<League>> responseEntityLeagues = restTemplate.exchange(URL + "/leagues", HttpMethod.GET, null, new ParameterizedTypeReference<List<League>>() {
        });
        List<League> leagues = responseEntityLeagues.getBody();
        model.addAttribute("leagues",leagues);
        model.addAttribute("leagueId",league_id);
        return "tournaments";
    }

    @RequestMapping("/leagues/{league_id}/teams/{team_id}")
    public String teamProfileView(@PathVariable int league_id, @PathVariable int team_id, Model model)
    {
        ResponseEntity<List<League>> responseEntityLeagues = restTemplate.exchange(URL + "/leagues", HttpMethod.GET, null, new ParameterizedTypeReference<List<League>>() {
        });
        List<League> leagues = responseEntityLeagues.getBody();
        model.addAttribute("leagues",leagues);
        //ResponseEntity<List<Player>> responseEntityPlayers = restTemplate.exchange(URL + "/teams/"+team_id+"/players", HttpMethod.GET, null, new ParameterizedTypeReference<List<Player>>() {
        //});
        //List<Player> players = responseEntityPlayers.getBody();
        //ResponseEntity<List<Flag>> responseEntityFlags = restTemplate.exchange(URL + "/teams/"+team_id+"/flags", HttpMethod.GET, null, new ParameterizedTypeReference<List<Flag>>() {
        //});
        //List<Flag> flags = responseEntityFlags.getBody();
        ResponseEntity<List<Transfer>> responseEntityTransfers = restTemplate.exchange(URL + "/teams/"+team_id+"/transfers", HttpMethod.GET, null, new ParameterizedTypeReference<List<Transfer>>() {
        });
        List<Transfer> transfers  = responseEntityTransfers.getBody();
        ResponseEntity<Team> responseEntityTeam = restTemplate.exchange(URL + "/teams/"+team_id, HttpMethod.GET, null, new ParameterizedTypeReference<Team>() {
        });
        Team team = responseEntityTeam.getBody();
        model.addAttribute("leagues",leagues);
        model.addAttribute("leagueId", league_id);
        model.addAttribute("players", team.getPlayers());
        model.addAttribute("flags", team.getFlags());
        model.addAttribute("tournaments", team.getTournaments());
        model.addAttribute("transfers", transfers);
        model.addAttribute("teamName",team.getTeamName());
        model.addAttribute("teamId",team.getId());
        return "team-profile";
    }

    @RequestMapping("/leagues/{league_id}/teams/{team_id}/players/{player_id}")
    public String playerProfileView(@PathVariable int league_id, @PathVariable int team_id, @PathVariable int player_id, Model model)
    {
        ResponseEntity<List<League>> responseEntityLeagues = restTemplate.exchange(URL + "/leagues", HttpMethod.GET, null, new ParameterizedTypeReference<List<League>>() {
        });
        List<League> leagues = responseEntityLeagues.getBody();
        model.addAttribute("leagues",leagues);
        //ResponseEntity<List<Player>> responseEntityPlayers = restTemplate.exchange(URL + "/teams/"+team_id+"/players", HttpMethod.GET, null, new ParameterizedTypeReference<List<Player>>() {
        //});
        //List<Player> players = responseEntityPlayers.getBody();
        //ResponseEntity<List<Flag>> responseEntityFlags = restTemplate.exchange(URL + "/teams/"+team_id+"/flags", HttpMethod.GET, null, new ParameterizedTypeReference<List<Flag>>() {
        //});
        //List<Flag> flags = responseEntityFlags.getBody();
        ResponseEntity<Player> responseEntityPlayer = restTemplate.exchange(URL + "/players/"+player_id, HttpMethod.GET, null, new ParameterizedTypeReference<Player>() {
        });
        Player player  = responseEntityPlayer.getBody();
        model.addAttribute("leagues",leagues);
        model.addAttribute("leagueId", league_id);
        model.addAttribute("player", player);

        return "player";
    }
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
    }*/

    private List<League> fetchLeagues() {
        try {
            ResponseEntity<List<League>> responseEntity = restTemplate.exchange(
                    URL + "/leagues",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<League>>() {}
            );
            return responseEntity.getBody();
        } catch (Exception e) {
            // Log the exception
            return Collections.emptyList(); // Return an empty list or handle appropriately
        }
    }

    private Team fetchTeam(int team_id) {
        try {
            ResponseEntity<Team> responseEntity = restTemplate.exchange(
                    URL + "/teams/" + team_id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Team>() {}
            );
            return responseEntity.getBody();
        } catch (Exception e) {
            // Log the exception
            return null; // Return null or handle appropriately
        }
    }

    private List<Transfer> fetchTransfers(int team_id) {
        try {
            ResponseEntity<List<Transfer>> responseEntity = restTemplate.exchange(
                    URL + "/teams/" + team_id + "/transfers",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Transfer>>() {}
            );
            return responseEntity.getBody();
        } catch (Exception e) {
            // Log the exception
            return Collections.emptyList(); // Return an empty list or handle appropriately
        }
    }

    @RequestMapping("/players")
    public String playersView()
    {
        return "players-view";
    }






}
