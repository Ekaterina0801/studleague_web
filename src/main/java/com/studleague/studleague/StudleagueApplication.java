package com.studleague.studleague;

import com.studleague.studleague.dao.interfaces.PlayerDao;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.services.interfaces.PlayerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class StudleagueApplication {


	public static void main(String[] args) {
		SpringApplication.run(StudleagueApplication.class, args);

	}

}
