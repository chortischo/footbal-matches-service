package com.football.matches.controller;

import com.football.matches.dto.GameDTO;
import com.football.matches.mapper.GameMapper;
import com.football.matches.model.Game;
import com.football.matches.model.GameResult;
import com.football.matches.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
public class GameController {
    private final GameService gameService;
    private final GameMapper gameMapper;

    @Autowired
    public GameController(GameService gameService, GameMapper gameMapper) {
        this.gameService = gameService;
        this.gameMapper = gameMapper;
    }

    @PostMapping
    public ResponseEntity<GameDTO> addGame(@RequestBody GameDTO gameDTO) {
        Game game = gameMapper.toEntity(gameDTO);
        Game savedGame = gameService.addGame(game);
        return ResponseEntity.ok(gameMapper.toDTO(savedGame));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.ok().body(true);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameDTO> updateGame(@PathVariable Long id, @RequestBody GameDTO gameDTO) {
        Game game = gameMapper.toEntity(gameDTO);
        Game updatedGame = gameService.updateGame(id, game);
        return ResponseEntity.ok(gameMapper.toDTO(updatedGame));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
        Game game = gameService.getGame(id);
        return ResponseEntity.ok(gameMapper.toDTO(game));
    }

    @GetMapping
    public ResponseEntity<List<GameDTO>> getAllGames() {
        List<Game> games = gameService.getAllGames();
        List<GameDTO> gameDTOs = games.stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(gameDTOs);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<GameDTO>> getGamesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Game> games = gameService.getGamesByDateRange(start, end);
        List<GameDTO> gameDTOs = games.stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(gameDTOs);
    }

    @GetMapping("/search")
    public ResponseEntity<List<GameDTO>> searchGamesByOpponent(@RequestParam(required = false) String opponentTeam,
                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                               @RequestParam(defaultValue = "true") boolean ascending) {
        List<Game> games = gameService.searchGamesByOpponent(opponentTeam, date, ascending);
        List<GameDTO> gameDTOs = games.stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(gameDTOs);
    }

    @GetMapping("/result")
    public ResponseEntity<List<GameDTO>> getGamesByResult(@RequestParam GameResult result) {
        List<Game> games = gameService.getGamesByResult(result);
        List<GameDTO> gameDTOs = games.stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(gameDTOs);
    }

    @GetMapping("/scheduled")
    public ResponseEntity<List<GameDTO>> getScheduledGames() {
        List<Game> games = gameService.getScheduledGames();
        List<GameDTO> gameDTOs = games.stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(gameDTOs);
    }

    @GetMapping("/stadium/{stadiumId}")
    public ResponseEntity<List<GameDTO>> getGamesByStadium(@PathVariable Long stadiumId) {
        List<Game> games = gameService.getGamesByStadium(stadiumId);
        List<GameDTO> gameDTOs = games.stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(gameDTOs);
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<GameDTO>> getGamesByPlayer(@PathVariable Long playerId) {
        List<Game> games = gameService.getGamesByPlayer(playerId);
        List<GameDTO> gameDTOs = games.stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(gameDTOs);
    }

    @PostMapping("/{gameId}/players/{playerId}")
    public ResponseEntity<GameDTO> addPlayerToGame(@PathVariable Long gameId, @PathVariable Long playerId) {
        Game game = gameService.addPlayerToGame(gameId, playerId);
        return ResponseEntity.ok(gameMapper.toDTO(game));
    }

    @DeleteMapping("/{gameId}/players/{playerId}")
    public ResponseEntity<GameDTO> removePlayerFromGame(@PathVariable Long gameId, @PathVariable Long playerId) {
        Game game = gameService.removePlayerFromGame(gameId, playerId);
        return ResponseEntity.ok(gameMapper.toDTO(game));
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<GameDTO>> getGamesSortedByDate(@RequestParam(defaultValue = "true") boolean ascending) {
        List<Game> games = gameService.getGamesSortedByDate(ascending);
        List<GameDTO> gameDTOs = games.stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(gameDTOs);
    }

    @GetMapping("/categories")
    public ResponseEntity<Map<String, List<GameDTO>>> getGamesByResultCategory() {
        Map<String, List<Game>> categorizedGames = gameService.getGamesByResultCategory();
        Map<String, List<GameDTO>> categorizedGameDTOs = new HashMap<>();
        
        categorizedGames.forEach((category, games) -> 
            categorizedGameDTOs.put(category, games.stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList()))
        );
        
        return ResponseEntity.ok(categorizedGameDTOs);
    }
} 