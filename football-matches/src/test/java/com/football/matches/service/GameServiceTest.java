package com.football.matches.service;

import com.football.matches.model.*;
import com.football.matches.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private GameService gameService;

    private Game testGame;
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
    }

    @Test
    void addGame_ShouldReturnSavedGame() {
        when(gameRepository.save(any(Game.class))).thenReturn(testGame);

        Game savedGame = gameService.addGame(testGame);

        assertNotNull(savedGame);
        assertEquals(testGame.getOpponentTeam(), savedGame.getOpponentTeam());
        assertEquals(testGame.getStadium(), savedGame.getStadium());
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void deleteGame_ShouldDeleteGame() {
        doNothing().when(gameRepository).deleteById(1L);

        gameService.deleteGame(1L);

        verify(gameRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateGame_ShouldUpdateAndReturnGame() {
        Game updatedGame = new Game();
        updatedGame.setOpponentTeam("New Opponent FC");
        updatedGame.setDateTime(LocalDateTime.now().plusDays(1));
        updatedGame.setStadium(testStadium);
        updatedGame.setStatus(GameStatus.SCHEDULED);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(testGame));
        when(gameRepository.save(any(Game.class))).thenReturn(updatedGame);

        Game result = gameService.updateGame(1L, updatedGame);

        assertNotNull(result);
        assertEquals(updatedGame.getOpponentTeam(), result.getOpponentTeam());
        assertEquals(updatedGame.getDateTime(), result.getDateTime());
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void updateGame_ShouldThrowException_WhenGameNotFound() {
        Game updatedGame = new Game();
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gameService.updateGame(1L, updatedGame));
    }

    @Test
    void getGame_ShouldReturnGame() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(testGame));

        Game result = gameService.getGame(1L);

        assertNotNull(result);
        assertEquals(testGame.getId(), result.getId());
        assertEquals(testGame.getOpponentTeam(), result.getOpponentTeam());
    }

    @Test
    void getGame_ShouldThrowException_WhenGameNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gameService.getGame(1L));
    }

    @Test
    void getAllGames_ShouldReturnListOfGames() {
        List<Game> games = Collections.singletonList(testGame);
        when(gameRepository.findAll()).thenReturn(games);

        List<Game> result = gameService.getAllGames();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testGame.getId(), result.get(0).getId());
    }

    @Test
    void getGamesByDateRange_ShouldReturnGamesInRange() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<Game> games = Collections.singletonList(testGame);
        when(gameRepository.findByDateTimeBetween(start, end)).thenReturn(games);

        List<Game> result = gameService.getGamesByDateRange(start, end);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getDateTime().isAfter(start) && result.get(0).getDateTime().isBefore(end));
    }

    @Test
    void searchGamesByOpponent_ShouldReturnMatchingGames() {
        String opponentTeam = "Opponent";
        LocalDate date = LocalDate.now();
        List<Game> games = Collections.singletonList(testGame);
        when(gameRepository.findByOpponentTeamContainingIgnoreCaseAndDateTimeBetween(
            eq(opponentTeam), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(games);

        List<Game> result = gameService.searchGamesByOpponent(opponentTeam, date, true);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getOpponentTeam().contains(opponentTeam));
    }

    @Test
    void getGamesByResult_ShouldReturnGamesWithResult() {
        List<Game> games = Collections.singletonList(testGame);
        when(gameRepository.findByResult(GameResult.NOT_PLAYED)).thenReturn(games);

        List<Game> result = gameService.getGamesByResult(GameResult.NOT_PLAYED);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(GameResult.NOT_PLAYED, result.get(0).getResult());
    }

    @Test
    void getScheduledGames_ShouldReturnScheduledGames() {
        List<Game> games = Collections.singletonList(testGame);
        when(gameRepository.findAllScheduledGames()).thenReturn(games);

        List<Game> result = gameService.getScheduledGames();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(GameStatus.SCHEDULED, result.get(0).getStatus());
    }

    @Test
    void getGamesByStadium_ShouldReturnGamesForStadium() {
        List<Game> games = Collections.singletonList(testGame);
        when(gameRepository.findByStadiumId(1L)).thenReturn(games);

        List<Game> result = gameService.getGamesByStadium(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testStadium.getId(), result.get(0).getStadium().getId());
    }

    @Test
    void getGamesByPlayer_ShouldReturnGamesForPlayer() {
        List<Game> games = Collections.singletonList(testGame);
        when(gameRepository.findByPlayerId(1L)).thenReturn(games);

        List<Game> result = gameService.getGamesByPlayer(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getPlayers().contains(testPlayer));
    }

    @Test
    void addPlayerToGame_ShouldAddPlayerToGame() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(testGame));
        when(playerService.getPlayer(1L)).thenReturn(testPlayer);
        when(gameRepository.save(any(Game.class))).thenReturn(testGame);

        Game result = gameService.addPlayerToGame(1L, 1L);

        assertNotNull(result);
        assertTrue(result.getPlayers().contains(testPlayer));
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void removePlayerFromGame_ShouldRemovePlayerFromGame() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(testGame));
        when(playerService.getPlayer(1L)).thenReturn(testPlayer);
        when(gameRepository.save(any(Game.class))).thenReturn(testGame);

        Game result = gameService.removePlayerFromGame(1L, 1L);

        assertNotNull(result);
        assertFalse(result.getPlayers().contains(testPlayer));
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void getGamesSortedByDate_ShouldReturnSortedGames() {
        List<Game> games = Arrays.asList(testGame);
        when(gameRepository.findAll()).thenReturn(games);

        List<Game> result = gameService.getGamesSortedByDate(true);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getGamesByResultCategory_ShouldReturnCategorizedGames() {
        List<Game> games = Collections.singletonList(testGame);
        when(gameRepository.findAll()).thenReturn(games);

        Map<String, List<Game>> result = gameService.getGamesByResultCategory();

        assertNotNull(result);
        assertTrue(result.containsKey("NOT_PLAYED"));
        assertEquals(1, result.get("NOT_PLAYED").size());
    }
} 