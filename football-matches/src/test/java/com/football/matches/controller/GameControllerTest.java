package com.football.matches.controller;

import com.football.matches.dto.GameDTO;
import com.football.matches.mapper.GameMapper;
import com.football.matches.model.*;
import com.football.matches.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private GameController gameController;

    private Game testGame;
    private GameDTO testGameDTO;
    private Player testPlayer;
    private Stadium testStadium;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testStadium = new Stadium();
        testStadium.setId(1L);
        testStadium.setName("Test Stadium");
        testStadium.setCapacity(50000);
        testStadium.setPricePerSeat(new BigDecimal("50.00"));

        testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setFirstName("John");
        testPlayer.setLastName("Doe");
        testPlayer.setStatus(PlayerStatus.ACTIVE);
        testPlayer.setHealthStatus(HealthStatus.FIT);

        testGame = new Game();
        testGame.setId(1L);
        testGame.setDateTime(LocalDateTime.now());
        testGame.setOpponentTeam("Opponent FC");
        testGame.setStadium(testStadium);
        List<Player> players = new ArrayList<>();
        players.add(testPlayer);
        testGame.setPlayers(players);
        testGame.setAttendance(30000);
        testGame.setResult(GameResult.NOT_PLAYED);
        testGame.setStatus(GameStatus.SCHEDULED);

        testGameDTO = new GameDTO();
        testGameDTO.setId(1L);
        testGameDTO.setDateTime(testGame.getDateTime());
        testGameDTO.setOpponentTeam(testGame.getOpponentTeam());
        testGameDTO.setAttendance(testGame.getAttendance());
        testGameDTO.setResult(testGame.getResult());
        testGameDTO.setStatus(testGame.getStatus());
    }

    @Test
    void addGame_ShouldReturnCreatedGame() {
        when(gameMapper.toEntity(testGameDTO)).thenReturn(testGame);
        when(gameService.addGame(testGame)).thenReturn(testGame);
        when(gameMapper.toDTO(testGame)).thenReturn(testGameDTO);

        ResponseEntity<GameDTO> response = gameController.addGame(testGameDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testGameDTO, response.getBody());
        verify(gameService, times(1)).addGame(testGame);
    }

    @Test
    void deleteGame_ShouldReturnSuccess() {
        doNothing().when(gameService).deleteGame(1L);

        ResponseEntity<Boolean> response = gameController.deleteGame(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
        verify(gameService, times(1)).deleteGame(1L);
    }

    @Test
    void updateGame_ShouldReturnUpdatedGame() {
        when(gameMapper.toEntity(testGameDTO)).thenReturn(testGame);
        when(gameService.updateGame(1L, testGame)).thenReturn(testGame);
        when(gameMapper.toDTO(testGame)).thenReturn(testGameDTO);

        ResponseEntity<GameDTO> response = gameController.updateGame(1L, testGameDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testGameDTO, response.getBody());
        verify(gameService, times(1)).updateGame(1L, testGame);
    }

    @Test
    void getGame_ShouldReturnGame() {
        when(gameService.getGame(1L)).thenReturn(testGame);
        when(gameMapper.toDTO(testGame)).thenReturn(testGameDTO);

        ResponseEntity<GameDTO> response = gameController.getGame(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testGameDTO, response.getBody());
        verify(gameService, times(1)).getGame(1L);
    }

    @Test
    void getAllGames_ShouldReturnListOfGames() {
        List<Game> games = Collections.singletonList(testGame);
        List<GameDTO> gameDTOs = Collections.singletonList(testGameDTO);
        when(gameService.getAllGames()).thenReturn(games);
        when(gameMapper.toDTO(testGame)).thenReturn(testGameDTO);

        ResponseEntity<List<GameDTO>> response = gameController.getAllGames();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(gameDTOs, response.getBody());
        verify(gameService, times(1)).getAllGames();
    }

    @Test
    void getGamesByDateRange_ShouldReturnGamesInRange() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<Game> games = Collections.singletonList(testGame);
        List<GameDTO> gameDTOs = Collections.singletonList(testGameDTO);
        when(gameService.getGamesByDateRange(start, end)).thenReturn(games);
        when(gameMapper.toDTO(testGame)).thenReturn(testGameDTO);

        ResponseEntity<List<GameDTO>> response = gameController.getGamesByDateRange(start, end);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(gameDTOs, response.getBody());
        verify(gameService, times(1)).getGamesByDateRange(start, end);
    }

    @Test
    void searchGamesByOpponent_ShouldReturnMatchingGames() {
        String opponentTeam = "Opponent";
        LocalDate date = LocalDate.now();
        List<Game> games = Collections.singletonList(testGame);
        List<GameDTO> gameDTOs = Collections.singletonList(testGameDTO);
        when(gameService.searchGamesByOpponent(opponentTeam, date, true)).thenReturn(games);
        when(gameMapper.toDTO(testGame)).thenReturn(testGameDTO);

        ResponseEntity<List<GameDTO>> response = gameController.searchGamesByOpponent(opponentTeam, date, true);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(gameDTOs, response.getBody());
        verify(gameService, times(1)).searchGamesByOpponent(opponentTeam, date, true);
    }

    @Test
    void getGamesByResult_ShouldReturnGamesWithResult() {
        List<Game> games = Collections.singletonList(testGame);
        List<GameDTO> gameDTOs = Collections.singletonList(testGameDTO);
        when(gameService.getGamesByResult(GameResult.NOT_PLAYED)).thenReturn(games);
        when(gameMapper.toDTO(testGame)).thenReturn(testGameDTO);

        ResponseEntity<List<GameDTO>> response = gameController.getGamesByResult(GameResult.NOT_PLAYED);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(gameDTOs, response.getBody());
        verify(gameService, times(1)).getGamesByResult(GameResult.NOT_PLAYED);
    }

    @Test
    void getScheduledGames_ShouldReturnScheduledGames() {
        List<Game> games = Collections.singletonList(testGame);
        List<GameDTO> gameDTOs = Collections.singletonList(testGameDTO);
        when(gameService.getScheduledGames()).thenReturn(games);
        when(gameMapper.toDTO(testGame)).thenReturn(testGameDTO);

        ResponseEntity<List<GameDTO>> response = gameController.getScheduledGames();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(gameDTOs, response.getBody());
        verify(gameService, times(1)).getScheduledGames();
    }

    @Test
    void getGamesByStadium_ShouldReturnGamesForStadium() {
        List<Game> games = Collections.singletonList(testGame);
        List<GameDTO> gameDTOs = Collections.singletonList(testGameDTO);
        when(gameService.getGamesByStadium(1L)).thenReturn(games);
        when(gameMapper.toDTO(testGame)).thenReturn(testGameDTO);

        ResponseEntity<List<GameDTO>> response = gameController.getGamesByStadium(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(gameDTOs, response.getBody());
        verify(gameService, times(1)).getGamesByStadium(1L);
    }

    @Test
    void getGamesByPlayer_ShouldReturnGamesForPlayer() {
        List<Game> games = Collections.singletonList(testGame);
        List<GameDTO> gameDTOs = Collections.singletonList(testGameDTO);
        when(gameService.getGamesByPlayer(1L)).thenReturn(games);
        when(gameMapper.toDTO(testGame)).thenReturn(testGameDTO);

        ResponseEntity<List<GameDTO>> response = gameController.getGamesByPlayer(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(gameDTOs, response.getBody());
        verify(gameService, times(1)).getGamesByPlayer(1L);
    }

    @Test
    void addPlayerToGame_ShouldAddPlayerToGame() {
        when(gameService.addPlayerToGame(1L, 1L)).thenReturn(testGame);
        when(gameMapper.toDTO(testGame)).thenReturn(testGameDTO);

        ResponseEntity<GameDTO> response = gameController.addPlayerToGame(1L, 1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testGameDTO, response.getBody());
        verify(gameService, times(1)).addPlayerToGame(1L, 1L);
    }

    @Test
    void removePlayerFromGame_ShouldRemovePlayerFromGame() {
        when(gameService.removePlayerFromGame(1L, 1L)).thenReturn(testGame);
        when(gameMapper.toDTO(testGame)).thenReturn(testGameDTO);

        ResponseEntity<GameDTO> response = gameController.removePlayerFromGame(1L, 1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testGameDTO, response.getBody());
        verify(gameService, times(1)).removePlayerFromGame(1L, 1L);
    }

    @Test
    void getGamesSortedByDate_ShouldReturnSortedGames() {
        List<Game> games = Collections.singletonList(testGame);
        List<GameDTO> gameDTOs = Collections.singletonList(testGameDTO);
        when(gameService.getGamesSortedByDate(true)).thenReturn(games);
        when(gameMapper.toDTO(testGame)).thenReturn(testGameDTO);

        ResponseEntity<List<GameDTO>> response = gameController.getGamesSortedByDate(true);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(gameDTOs, response.getBody());
        verify(gameService, times(1)).getGamesSortedByDate(true);
    }

    @Test
    void getGamesByResultCategory_ShouldReturnCategorizedGames() {
        Map<String, List<Game>> categorizedGames = new HashMap<>();
        categorizedGames.put("NOT_PLAYED", Collections.singletonList(testGame));
        Map<String, List<GameDTO>> categorizedGameDTOs = new HashMap<>();
        categorizedGameDTOs.put("NOT_PLAYED", Collections.singletonList(testGameDTO));
        when(gameService.getGamesByResultCategory()).thenReturn(categorizedGames);
        when(gameMapper.toDTO(testGame)).thenReturn(testGameDTO);

        ResponseEntity<Map<String, List<GameDTO>>> response = gameController.getGamesByResultCategory();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(categorizedGameDTOs, response.getBody());
        verify(gameService, times(1)).getGamesByResultCategory();
    }
} 