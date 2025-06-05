package com.football.matches.service;

import com.football.matches.model.*;
import com.football.matches.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final StadiumService stadiumService;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerService playerService, StadiumService stadiumService) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.stadiumService = stadiumService;
    }

    public Game addGame(Game game) {
        return gameRepository.save(game);
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    public Game updateGame(Long id, Game updatedGame) {
        Optional<Game> existingGame = gameRepository.findById(id);
        if (existingGame.isPresent()) {
            Game game = existingGame.get();
            game.setDateTime(updatedGame.getDateTime());
            game.setOpponentTeam(updatedGame.getOpponentTeam());
            game.setStadium(updatedGame.getStadium());
            game.setPlayers(updatedGame.getPlayers());
            game.setAttendance(updatedGame.getAttendance());
            game.setResult(updatedGame.getResult());
            game.setStatus(updatedGame.getStatus());
            return gameRepository.save(game);
        }
        throw new RuntimeException("Game not found with id: " + id);
    }

    public Game getGame(Long id) {
        return gameRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Game not found with id: " + id));
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public List<Game> getGamesByDateRange(LocalDateTime start, LocalDateTime end) {
        return gameRepository.findByDateTimeBetween(start, end);
    }

    public List<Game> searchGamesByOpponent(String opponentTeam, LocalDate localDate, boolean ascending) {
        List<Game> games;
        var start = LocalDate.ofYearDay(0, 1).atStartOfDay();
        var end = start.plusYears(999999);

        if (localDate != null) {
            start = localDate.atStartOfDay();
            end = start.plusDays(1);
        }

        if (opponentTeam != null) {
            games = gameRepository.findByOpponentTeamContainingIgnoreCaseAndDateTimeBetween(opponentTeam, start, end);
        } else {
            games = gameRepository.findByDateTimeBetween(start, end);
        }

        if (ascending) {
            return games.stream()
                    .sorted(Comparator.comparing(Game::getDateTime))
                    .collect(Collectors.toList());
        } else {
            return games.stream()
                    .sorted((g1, g2) -> g2.getDateTime().compareTo(g1.getDateTime()))
                    .collect(Collectors.toList());
        }
    }

    public List<Game> getGamesByResult(GameResult result) {
        return gameRepository.findByResult(result);
    }

    public List<Game> getScheduledGames() {
        return gameRepository.findAllScheduledGames();
    }

    public List<Game> getGamesByStadium(Long stadiumId) {
        return gameRepository.findByStadiumId(stadiumId);
    }

    public List<Game> getGamesByPlayer(Long playerId) {
        return gameRepository.findByPlayerId(playerId);
    }

    public Game addPlayerToGame(Long gameId, Long playerId) {
        Game game = getGame(gameId);
        Player player = playerService.getPlayer(playerId);
        game.getPlayers().add(player);
        return gameRepository.save(game);
    }

    public Game removePlayerFromGame(Long gameId, Long playerId) {
        Game game = getGame(gameId);
        Player player = playerService.getPlayer(playerId);
        game.getPlayers().remove(player);
        return gameRepository.save(game);
    }

    public List<Game> getGamesSortedByDate(boolean ascending) {
        List<Game> games = gameRepository.findAll();
        if (ascending) {
            return games.stream()
                .sorted((g1, g2) -> g1.getDateTime().compareTo(g2.getDateTime()))
                .collect(Collectors.toList());
        } else {
            return games.stream()
                .sorted((g1, g2) -> g2.getDateTime().compareTo(g1.getDateTime()))
                .collect(Collectors.toList());
        }
    }

    public Map<String, List<Game>> getGamesByResultCategory() {
        List<Game> allGames = gameRepository.findAll();
        Map<String, List<Game>> categorizedGames = new HashMap<>();
        
        categorizedGames.put("WON", allGames.stream()
            .filter(game -> game.getResult() == GameResult.WIN)
            .collect(Collectors.toList()));
            
        categorizedGames.put("LOST", allGames.stream()
            .filter(game -> game.getResult() == GameResult.LOSS)
            .collect(Collectors.toList()));
            
        categorizedGames.put("DRAW", allGames.stream()
            .filter(game -> game.getResult() == GameResult.DRAW)
            .collect(Collectors.toList()));
            
        categorizedGames.put("NOT_PLAYED", allGames.stream()
            .filter(game -> game.getResult() == GameResult.NOT_PLAYED)
            .collect(Collectors.toList()));
            
        return categorizedGames;
    }

} 