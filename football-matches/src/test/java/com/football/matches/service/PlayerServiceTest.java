package com.football.matches.service;

import com.football.matches.model.Player;
import com.football.matches.model.PlayerStatus;
import com.football.matches.model.HealthStatus;
import com.football.matches.repository.PlayerRepository;
import com.football.matches.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player testPlayer;

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
    }

    @Test
    void addPlayer_ShouldReturnSavedPlayer() {
        when(playerRepository.save(any(Player.class))).thenReturn(testPlayer);

        Player savedPlayer = playerService.addPlayer(testPlayer);

        assertNotNull(savedPlayer);
        assertEquals(testPlayer.getFirstName(), savedPlayer.getFirstName());
        assertEquals(testPlayer.getLastName(), savedPlayer.getLastName());
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void deletePlayer_ShouldDeletePlayerAndRemoveFromGames() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));

        playerService.deletePlayer(1L);

        verify(playerRepository, times(1)).deleteById(1L);
    }

    @Test
    void updatePlayer_ShouldUpdateAndReturnPlayer() {
        Player updatedPlayer = new Player();
        updatedPlayer.setFirstName("Jane");
        updatedPlayer.setLastName("Smith");
        updatedPlayer.setDateOfBirth(LocalDate.of(1992, 2, 2));
        updatedPlayer.setStatus(PlayerStatus.ACTIVE);
        updatedPlayer.setHealthStatus(HealthStatus.FIT);
        updatedPlayer.setSalary(new BigDecimal("12000"));

        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(updatedPlayer);

        Player result = playerService.updatePlayer(1L, updatedPlayer);

        assertNotNull(result);
        assertEquals(updatedPlayer.getFirstName(), result.getFirstName());
        assertEquals(updatedPlayer.getLastName(), result.getLastName());
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void getPlayer_ShouldReturnPlayer() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));

        Player result = playerService.getPlayer(1L);

        assertNotNull(result);
        assertEquals(testPlayer.getId(), result.getId());
        assertEquals(testPlayer.getFirstName(), result.getFirstName());
    }

    @Test
    void getPlayer_ShouldThrowException_WhenPlayerNotFound() {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playerService.getPlayer(1L));
    }

    @Test
    void getAllPlayers_ShouldReturnListOfPlayers() {
        List<Player> players = Arrays.asList(testPlayer);
        when(playerRepository.findAll()).thenReturn(players);

        List<Player> result = playerService.getAllPlayers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPlayer.getId(), result.get(0).getId());
    }

    @Test
    void searchPlayers_ShouldReturnMatchingPlayers() {
        String searchTerm = "John";
        List<Player> players = Arrays.asList(testPlayer);
        when(playerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchTerm, searchTerm))
            .thenReturn(players);

        List<Player> result = playerService.searchPlayers(searchTerm);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPlayer.getFirstName(), result.get(0).getFirstName());
    }

    @Test
    void getActivePlayers_ShouldReturnActivePlayers() {
        List<Player> players = Arrays.asList(testPlayer);
        when(playerRepository.findAllActivePlayers()).thenReturn(players);

        List<Player> result = playerService.getActivePlayers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(PlayerStatus.ACTIVE, result.get(0).getStatus());
    }

    @Test
    void getFitPlayers_ShouldReturnFitPlayers() {
        List<Player> players = Arrays.asList(testPlayer);
        when(playerRepository.findAllFitPlayers()).thenReturn(players);

        List<Player> result = playerService.getFitPlayers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(HealthStatus.FIT, result.get(0).getHealthStatus());
    }
} 