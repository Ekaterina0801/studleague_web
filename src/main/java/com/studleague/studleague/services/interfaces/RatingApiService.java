package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.TeamDetailsDTO;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface RatingApiService {

    public List<TeamDetailsDTO> fetchTeams(String tournamentId);
    public HttpHeaders createHeaders();
}
