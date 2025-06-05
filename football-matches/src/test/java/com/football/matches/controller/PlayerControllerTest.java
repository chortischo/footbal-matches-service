package com.football.matches.controller;

import com.football.matches.dto.PlayerDTO;
import com.football.matches.mapper.PlayerMapper;
import com.football.matches.model.Player;
import com.football.matches.model.PlayerStatus;
import com.football.matches.model.HealthStatus;
import com.football.matches.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private PlayerController playerController;

    private Player testPlayer;
    private PlayerDTO testPlayerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setFirstName("John");
        testPlayer.setLastName("Doe");
        testPlayer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testPlayer.setStatus(PlayerStatus.ACTIVE);
        testPlayer.setHealthStatus(HealthStatus.FIT);
        testPlayer.setSalary(new BigDecimal("10000"));

        testPlayerDTO = new PlayerDTO();
        testPlayerDTO.setId(1L);
        testPlayerDTO.setFirstName("John");
        testPlayerDTO.setLastName("Doe");
        testPlayerDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testPlayerDTO.setStatus(PlayerStatus.ACTIVE);
        testPlayerDTO.setHealthStatus(HealthStatus.FIT);
        testPlayerDTO.setSalary(new BigDecimal("10000"));
    }

    @Test
    void addPlayer_ShouldReturnCreatedPlayer() {
        when(playerMapper.toEntity(testPlayerDTO)).thenReturn(testPlayer);
        when(playerService.addPlayer(testPlayer)).thenReturn(testPlayer);
        when(playerMapper.toDTO(testPlayer)).thenReturn(testPlayerDTO);

        ResponseEntity<PlayerDTO> response = playerController.addPlayer(testPlayerDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testPlayerDTO, response.getBody());
        verify(playerService, times(1)).addPlayer(testPlayer);
    }

    @Test
    void deletePlayer_ShouldReturnSuccess() {
        doNothing().when(playerService).deletePlayer(1L);

        ResponseEntity<Boolean> response = playerController.deletePlayer(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
        verify(playerService, times(1)).deletePlayer(1L);
    }

    @Test
    void updatePlayer_ShouldReturnUpdatedPlayer() {
        when(playerMapper.toEntity(testPlayerDTO)).thenReturn(testPlayer);
        when(playerService.updatePlayer(1L, testPlayer)).thenReturn(testPlayer);
        when(playerMapper.toDTO(testPlayer)).thenReturn(testPlayerDTO);

        ResponseEntity<PlayerDTO> response = playerController.updatePlayer(1L, testPlayerDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testPlayerDTO, response.getBody());
        verify(playerService, times(1)).updatePlayer(1L, testPlayer);
    }

    @Test
    void getPlayer_ShouldReturnPlayer() {
        when(playerService.getPlayer(1L)).thenReturn(testPlayer);
        when(playerMapper.toDTO(testPlayer)).thenReturn(testPlayerDTO);

        ResponseEntity<PlayerDTO> response = playerController.getPlayer(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testPlayerDTO, response.getBody());
        verify(playerService, times(1)).getPlayer(1L);
    }

    @Test
    void getAllPlayers_ShouldReturnListOfPlayers() {
        List<Player> players = Arrays.asList(testPlayer);
        List<PlayerDTO> playerDTOs = Arrays.asList(testPlayerDTO);
        when(playerService.getAllPlayers()).thenReturn(players);
        when(playerMapper.toDTO(testPlayer)).thenReturn(testPlayerDTO);

        ResponseEntity<List<PlayerDTO>> response = playerController.getAllPlayers();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(playerDTOs, response.getBody());
        verify(playerService, times(1)).getAllPlayers();
    }

    @Test
    void searchPlayers_ShouldReturnMatchingPlayers() {
        String searchTerm = "John";
        List<Player> players = Arrays.asList(testPlayer);
        List<PlayerDTO> playerDTOs = Arrays.asList(testPlayerDTO);
        when(playerService.searchPlayers(searchTerm)).thenReturn(players);
        when(playerMapper.toDTO(testPlayer)).thenReturn(testPlayerDTO);

        ResponseEntity<List<PlayerDTO>> response = playerController.searchPlayers(searchTerm);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(playerDTOs, response.getBody());
        verify(playerService, times(1)).searchPlayers(searchTerm);
    }

    @Test
    void getActivePlayers_ShouldReturnActivePlayers() {
        List<Player> players = Arrays.asList(testPlayer);
        List<PlayerDTO> playerDTOs = Arrays.asList(testPlayerDTO);
        when(playerService.getActivePlayers()).thenReturn(players);
        when(playerMapper.toDTO(testPlayer)).thenReturn(testPlayerDTO);

        ResponseEntity<List<PlayerDTO>> response = playerController.getActivePlayers();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(playerDTOs, response.getBody());
        verify(playerService, times(1)).getActivePlayers();
    }

    @Test
    void getFitPlayers_ShouldReturnFitPlayers() {
        List<Player> players = Arrays.asList(testPlayer);
        List<PlayerDTO> playerDTOs = Arrays.asList(testPlayerDTO);
        when(playerService.getFitPlayers()).thenReturn(players);
        when(playerMapper.toDTO(testPlayer)).thenReturn(testPlayerDTO);

        ResponseEntity<List<PlayerDTO>> response = playerController.getFitPlayers();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(playerDTOs, response.getBody());
        verify(playerService, times(1)).getFitPlayers();
    }
} 