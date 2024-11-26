package com.studleague.studleague.services;

import com.studleague.studleague.dto.FlagDTO;
import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.factory.FlagFactory;
import com.studleague.studleague.repository.FlagRepository;
import com.studleague.studleague.services.interfaces.FlagService;
import com.studleague.studleague.services.interfaces.LeagueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class FlagServiceTest {

    @MockBean
    private FlagRepository flagRepository;

    @MockBean
    private EntityRetrievalUtils entityRetrievalUtils;

    @MockBean
    private LeagueService leagueService;

    @MockBean
    private FlagFactory flagFactory;

    @Autowired
    private FlagService flagService;

    private Flag flag;

    @BeforeEach
    public void setUp() {
        flag = new Flag();
        flag.setId(1L);
        flag.setName("Test Flag");
        flag.setLeague(new League());
        flag.getLeague().setId(10L);
        flag.setTeams(new ArrayList<>());
    }

    @Test
    public void testGetAllFlags() {
        // Arrange
        List<Flag> mockFlags = Arrays.asList(flag);
        when(flagRepository.findAll()).thenReturn(mockFlags);

        // Act
        List<Flag> result = flagService.getAllFlags();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(flag, result.get(0));
    }

    @Test
    public void testSaveFlag_WhenNew() {
        // Arrange
        flag.setId(null); // Simulate a new flag (no ID)
        when(flagRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(flagRepository.save(any(Flag.class))).thenReturn(flag);

        // Act
        flagService.saveFlag(flag);

        // Assert
        verify(flagRepository, times(1)).save(flag); // Verify save method is called
    }


    @Test
    public void testSaveFlag_WhenExisting() {
        // Arrange
        when(flagRepository.existsById(anyLong())).thenReturn(true);
        when(entityRetrievalUtils.getFlagOrThrow(anyLong())).thenReturn(flag);

        Flag updatedFlag = new Flag();
        updatedFlag.setName("Updated Flag");
        when(flagRepository.save(any(Flag.class))).thenReturn(updatedFlag);

        // Act
        flagService.saveFlag(updatedFlag);

        // Assert
        verify(flagRepository, times(1)).save(updatedFlag);
        assertEquals("Updated Flag", updatedFlag.getName());
    }

    @Test
    public void testGetFlagById() {
        // Arrange
        when(entityRetrievalUtils.getFlagOrThrow(anyLong())).thenReturn(flag);

        // Act
        Flag result = flagService.getFlagById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(flag, result);
    }

    @Test
    public void testDeleteFlag() {
        // Arrange
        doNothing().when(flagRepository).deleteById(anyLong());

        // Act
        flagService.deleteFlag(1L);

        // Assert
        verify(flagRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAllFlags() {
        // Arrange
        doNothing().when(flagRepository).deleteAll();

        // Act
        flagService.deleteAllFlags();

        // Assert
        verify(flagRepository, times(1)).deleteAll();
    }

    @Test
    public void testIsManager() {
        // Arrange
        when(entityRetrievalUtils.getFlagOrThrow(anyLong())).thenReturn(flag);
        when(leagueService.isManager(anyLong(), anyLong())).thenReturn(true);

        // Act
        boolean result = flagService.isManager(1L, 1L);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsManager_WithFlagDTO() {
        // Arrange
        FlagDTO flagDTO = new FlagDTO();
        flagDTO.setId(1L);
        flagDTO.setName("Test Flag");
        flagDTO.setLeagueId(1L);

        Flag flagEntity = new Flag();
        flagEntity.setId(1L);
        flagEntity.setLeague(new League());
        flagEntity.getLeague().setId(10L);

        when(flagFactory.mapToEntity(any(FlagDTO.class))).thenReturn(flagEntity);
        when(leagueService.isManager(anyLong(), anyLong())).thenReturn(true);

        // Act
        boolean result = flagService.isManager(1L, flagDTO);

        // Assert
        assertTrue(result);
    }
}

