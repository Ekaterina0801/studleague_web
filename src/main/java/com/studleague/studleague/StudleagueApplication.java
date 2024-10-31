package com.studleague.studleague;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableAdminServer
@SpringBootApplication
public class StudleagueApplication {


	public static void main(String[] args) {
		SpringApplication.run(StudleagueApplication.class, args);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());


	}

}
