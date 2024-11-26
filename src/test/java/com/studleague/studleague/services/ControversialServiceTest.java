package com.studleague.studleague.services;

import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.factory.ControversialFactory;
import com.studleague.studleague.repository.ControversialRepository;
import com.studleague.studleague.services.interfaces.ControversialService;
import com.studleague.studleague.services.interfaces.LeagueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ControversialServiceTest {

    @MockBean
    private ControversialRepository controversialRepository;

    @MockBean
    private EntityRetrievalUtils entityRetrievalUtils;

    @MockBean
    private LeagueService leagueService;

    @MockBean
    private ControversialFactory controversialFactory;

    @Autowired
    private ControversialService controversialService;

    private Controversial controversial;

    @BeforeEach
    public void setUp() {
        controversial = new Controversial();
        controversial.setId(1L);
        controversial.setQuestionNumber(2);
        controversial.setStatus("Resolved");
        controversial.setIssuedAt(LocalDateTime.now());
        controversial.setResolvedAt(String.valueOf(LocalDateTime.now().plusDays(1)));
    }

    @Test
    public void testGetAllControversials() {
        // Arrange
        List<Controversial> mockControversials = Arrays.asList(controversial);
        when(controversialRepository.findAll()).thenReturn(mockControversials);

        // Act
        List<Controversial> result = controversialService.getAllControversials();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(controversial, result.get(0));
    }

    @Test
    public void testSaveControversial_WhenNew() {
        // Arrange
        when(controversialRepository.existsByFullResultIdAndQuestionNumber(anyLong(), anyInt())).thenReturn(false);
        when(controversialRepository.save(any(Controversial.class))).thenReturn(controversial);

        // Act
        controversialService.saveControversial(controversial);

        // Assert
        verify(controversialRepository, times(1)).save(controversial);
    }

    @Test
    public void testSaveControversial_WhenExisting() {
        // Arrange
        when(controversialRepository.existsById(anyLong())).thenReturn(true);
        when(entityRetrievalUtils.getControversialOrThrow(anyLong())).thenReturn(controversial);

        Controversial updatedControversial = new Controversial();
        updatedControversial.setStatus("Updated");
        when(controversialRepository.save(any(Controversial.class))).thenReturn(updatedControversial);

        // Act
        controversialService.saveControversial(updatedControversial);

        // Assert
        verify(controversialRepository, times(1)).save(updatedControversial);
        assertEquals("Updated", updatedControversial.getStatus());
    }

    @Test
    public void testGetControversialById() {
        // Arrange
        when(entityRetrievalUtils.getControversialOrThrow(anyLong())).thenReturn(controversial);

        // Act
        Controversial result = controversialService.getControversialById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(controversial, result);
    }

    @Test
    public void testDeleteControversial() {
        // Arrange
        doNothing().when(controversialRepository).deleteById(anyLong());

        // Act
        controversialService.deleteControversial(1L);

        // Assert
        verify(controversialRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetControversialsByTeamIdWithQuestionNumber() {
        // Arrange
        List<Controversial> mockControversials = Arrays.asList(controversial);
        when(controversialRepository.findAllByTeamId(anyLong())).thenReturn(mockControversials);

        // Act
        var result = controversialService.getControversialsByTeamIdWithQuestionNumber(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey(controversial.getQuestionNumber()));
    }

    /*@Test
    public void testIsManager() {
        // Arrange
        when(entityRetrievalUtils.getControversialOrThrow(anyLong())).thenReturn(controversial);
        when(leagueService.isManager(anyLong(), anyLong())).thenReturn(true);

        // Act
        boolean result = controversialService.isManager(1L, controversial.getId());

        // Assert
        assertTrue(result);
    }*/

    @Test
    public void testSearchControversials() {
        // Arrange
        when(controversialRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(Arrays.asList(controversial));

        // Act
        List<Controversial> result = controversialService.searchControversials(null, null, null, null, null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(controversial, result.get(0));
    }
}

