package com.football.matches.controller;

import com.football.matches.dto.StadiumDTO;
import com.football.matches.mapper.StadiumMapper;
import com.football.matches.model.Stadium;
import com.football.matches.service.StadiumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class StadiumControllerTest {

    @Mock
    private StadiumService stadiumService;

    @Mock
    private StadiumMapper stadiumMapper;

    @InjectMocks
    private StadiumController stadiumController;

    private Stadium testStadium;
    private StadiumDTO testStadiumDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testStadium = new Stadium();
        testStadium.setId(1L);
        testStadium.setName("Test Stadium");
        testStadium.setCapacity(50000);
        testStadium.setPricePerSeat(new BigDecimal("50.00"));

        testStadiumDTO = new StadiumDTO();
        testStadiumDTO.setId(1L);
        testStadiumDTO.setName("Test Stadium");
        testStadiumDTO.setCapacity(50000);
        testStadiumDTO.setPricePerSeat(new BigDecimal("50.00"));
    }

    @Test
    void addStadium_ShouldReturnCreatedStadium() {
        when(stadiumMapper.toEntity(testStadiumDTO)).thenReturn(testStadium);
        when(stadiumService.addStadium(testStadium)).thenReturn(testStadium);
        when(stadiumMapper.toDTO(testStadium)).thenReturn(testStadiumDTO);

        ResponseEntity<StadiumDTO> response = stadiumController.addStadium(testStadiumDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testStadiumDTO, response.getBody());
        verify(stadiumService, times(1)).addStadium(testStadium);
    }

    @Test
    void deleteStadium_ShouldReturnSuccess() {
        doNothing().when(stadiumService).deleteStadium(1L);

        ResponseEntity<Boolean> response = stadiumController.deleteStadium(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
        verify(stadiumService, times(1)).deleteStadium(1L);
    }

    @Test
    void updateStadium_ShouldReturnUpdatedStadium() {
        when(stadiumMapper.toEntity(testStadiumDTO)).thenReturn(testStadium);
        when(stadiumService.updateStadium(1L, testStadium)).thenReturn(testStadium);
        when(stadiumMapper.toDTO(testStadium)).thenReturn(testStadiumDTO);

        ResponseEntity<StadiumDTO> response = stadiumController.updateStadium(1L, testStadiumDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testStadiumDTO, response.getBody());
        verify(stadiumService, times(1)).updateStadium(1L, testStadium);
    }

    @Test
    void getStadium_ShouldReturnStadium() {
        when(stadiumService.getStadium(1L)).thenReturn(testStadium);
        when(stadiumMapper.toDTO(testStadium)).thenReturn(testStadiumDTO);

        ResponseEntity<StadiumDTO> response = stadiumController.getStadium(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testStadiumDTO, response.getBody());
        verify(stadiumService, times(1)).getStadium(1L);
    }

    @Test
    void getAllStadiums_ShouldReturnListOfStadiums() {
        List<Stadium> stadiums = Arrays.asList(testStadium);
        List<StadiumDTO> stadiumDTOs = Arrays.asList(testStadiumDTO);
        when(stadiumService.getAllStadiums()).thenReturn(stadiums);
        when(stadiumMapper.toDTO(testStadium)).thenReturn(testStadiumDTO);

        ResponseEntity<List<StadiumDTO>> response = stadiumController.getAllStadiums();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(stadiumDTOs, response.getBody());
        verify(stadiumService, times(1)).getAllStadiums();
    }

    @Test
    void searchStadiums_ShouldReturnMatchingStadiums() {
        String searchTerm = "Test";
        List<Stadium> stadiums = Arrays.asList(testStadium);
        List<StadiumDTO> stadiumDTOs = Arrays.asList(testStadiumDTO);
        when(stadiumService.searchStadiums(searchTerm)).thenReturn(stadiums);
        when(stadiumMapper.toDTO(testStadium)).thenReturn(testStadiumDTO);

        ResponseEntity<List<StadiumDTO>> response = stadiumController.searchStadiums(searchTerm);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(stadiumDTOs, response.getBody());
        verify(stadiumService, times(1)).searchStadiums(searchTerm);
    }

    @Test
    void getStadiumsByMinCapacity_ShouldReturnStadiumsWithMinCapacity() {
        Integer minCapacity = 40000;
        List<Stadium> stadiums = Arrays.asList(testStadium);
        List<StadiumDTO> stadiumDTOs = Arrays.asList(testStadiumDTO);
        when(stadiumService.getStadiumsByMinCapacity(minCapacity)).thenReturn(stadiums);
        when(stadiumMapper.toDTO(testStadium)).thenReturn(testStadiumDTO);

        ResponseEntity<List<StadiumDTO>> response = stadiumController.getStadiumsByMinCapacity(minCapacity);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(stadiumDTOs, response.getBody());
        verify(stadiumService, times(1)).getStadiumsByMinCapacity(minCapacity);
    }

    @Test
    void getStadiumsByMaxPrice_ShouldReturnStadiumsWithMaxPrice() {
        Double maxPrice = 60.0;
        List<Stadium> stadiums = Arrays.asList(testStadium);
        List<StadiumDTO> stadiumDTOs = Arrays.asList(testStadiumDTO);
        when(stadiumService.getStadiumsByMaxPrice(maxPrice)).thenReturn(stadiums);
        when(stadiumMapper.toDTO(testStadium)).thenReturn(testStadiumDTO);

        ResponseEntity<List<StadiumDTO>> response = stadiumController.getStadiumsByMaxPrice(maxPrice);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(stadiumDTOs, response.getBody());
        verify(stadiumService, times(1)).getStadiumsByMaxPrice(maxPrice);
    }
} 