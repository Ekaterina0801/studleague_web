package com.studleague.studleague.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studleague.studleague.configs.SecurityConfig;
import com.studleague.studleague.dto.ControversialDTO;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.factory.ControversialFactory;
import com.studleague.studleague.services.implementations.security.AuthenticationService;
import com.studleague.studleague.services.implementations.security.JwtService;
import com.studleague.studleague.services.implementations.security.UserService;
import com.studleague.studleague.services.interfaces.ControversialService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Import(SecurityConfig.class)
public class ControversialControllerTest {


    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ControversialService controversialService;

    @MockBean
    private ControversialFactory controversialFactory;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    String accessToken;
    String refreshToken;

    @BeforeEach
    public void setup() throws NotFoundException {
        objectMapper.findAndRegisterModules();
        User user = userService.getByUsername("admin008");
        accessToken = jwtService.generateAccessToken(user);
        refreshToken = jwtService.generateRefreshToken(user);
    }

    @Test
    @WithMockUser(username = "admin008", roles = {"ADMIN"})
    void shouldCreateControversial() throws Exception {
        ControversialDTO controversial = ControversialDTO.builder()
                .questionNumber(2)
                .appealJuryComment("ok")
                .comment("ok")
                .fullResultId(2L)
                .answer("test")
                .resolvedAt("")
                .issuedAt(LocalDateTime.of(2023, 11, 25, 0, 0))
                .status("ok")
                .build();

        mockMvc.perform(post("/api/controversials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(controversial))
                        .header("Authorization", "Bearer " + accessToken).header("Refresh-Token", refreshToken))
                .andExpect(status().isCreated())
                .andDo(print());

    }

    @Test
    @WithMockUser(username = "admin008", roles = {"ADMIN"})
    void shouldGetAllControversials() throws Exception {
        // Create Controversial entities
        Controversial controversial1 = Controversial.builder()
                .questionNumber(2)
                .appealJuryComment("ok")
                .comment("ok")
                .answer("test")
                .resolvedAt("")
                .issuedAt(LocalDateTime.of(2023, 11, 25, 0, 0))
                .status("ok")
                .build();

        Controversial controversial2 = Controversial.builder()
                .questionNumber(5)
                .appealJuryComment("ok")
                .comment("ok")
                .answer("test")
                .resolvedAt("")
                .issuedAt(LocalDateTime.of(2023, 11, 25, 0, 0))
                .status("ok")
                .build();

        List<Controversial> controversials = List.of(controversial1, controversial2);

        // Mock the service to return the list of Controversial entities
        when(controversialService.getAllControversials()).thenReturn(controversials);


        mockMvc.perform(get("/api/controversials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))  // Ensure the response is a list with 2 items
                .andExpect(jsonPath("$[0].questionNumber").value(2)) // Additional assertions
                .andExpect(jsonPath("$[1].questionNumber").value(5))
                .andDo(print());
    }


    @Test
    @WithMockUser(username = "admin008", roles = {"ADMIN"})
    void shouldGetControversialById() throws Exception {
        long id = 1L;
        ControversialDTO controversialDTO = ControversialDTO.builder()
                .id(id)
                .questionNumber(2)
                .appealJuryComment("ok")
                .comment("ok")
                .fullResultId(2L)
                .answer("test")
                .resolvedAt("")
                .issuedAt(LocalDateTime.of(2023, 11, 25, 0, 0))
                .status("ok")
                .build();
        when(controversialService.getControversialById(id)).thenReturn(controversialFactory.mapToEntity(controversialDTO));
        when(controversialFactory.mapToDto(any())).thenReturn(controversialDTO);

        mockMvc.perform(get("/api/controversials/{id}", id))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(id))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin008", roles = {"ADMIN"})
    void shouldDeleteControversial() throws Exception {
        long id = 1L;
        doNothing().when(controversialService).deleteControversial(id);

        mockMvc.perform(delete("/api/controversials/{id}", id).header("Authorization", "Bearer " + accessToken).header("Refresh-Token", refreshToken))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().string("Controversial with ID = " + id + " was deleted"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin008", roles = {"ADMIN"})
    void shouldDeleteAllControversials() throws Exception {
        doNothing().when(controversialService).deleteAllControversials();

        mockMvc.perform(delete("/api/controversials").header("Authorization", "Bearer " + accessToken).header("Refresh-Token", refreshToken))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().string("Controversials were deleted"))
                .andDo(print());
    }
}

