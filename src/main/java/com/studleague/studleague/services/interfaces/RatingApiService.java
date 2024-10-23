package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.TeamDetailsDTO;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface RatingApiService {

    List<TeamDetailsDTO> fetchTeams(String tournamentId);
    HttpHeaders createHeaders();
}
