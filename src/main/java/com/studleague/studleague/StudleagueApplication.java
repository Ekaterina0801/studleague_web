package com.studleague.studleague;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.studleague.studleague.dao.interfaces.PlayerDao;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.services.interfaces.PlayerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class StudleagueApplication {


	public static void main(String[] args) {
		SpringApplication.run(StudleagueApplication.class, args);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());


	}

}
