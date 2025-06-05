package com.football.matches.service;

import com.football.matches.model.Player;
import com.football.matches.model.Game;
import com.football.matches.repository.PlayerRepository;
import com.football.matches.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, GameRepository gameRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    public Player addPlayer(Player player) {
        return playerRepository.save(player);
    }

    public void deletePlayer(Long id) {
        Player player = getPlayer(id);
        // Remove player from all associated games
        List<Game> games = gameRepository.findByPlayerId(id);
        for (Game game : games) {
            game.getPlayers().remove(player);
            gameRepository.save(game);
        }
        // Now we can safely delete the player
        playerRepository.deleteById(id);
    }

    @Transactional
    public Player updatePlayer(Long id, Player updatedPlayer) {
        Optional<Player> existingPlayer = playerRepository.findById(id);
        if (existingPlayer.isPresent()) {
            Player player = existingPlayer.get();
            player.setFirstName(updatedPlayer.getFirstName());
            player.setLastName(updatedPlayer.getLastName());
            player.setDateOfBirth(updatedPlayer.getDateOfBirth());
            player.setStatus(updatedPlayer.getStatus());
            player.setHealthStatus(updatedPlayer.getHealthStatus());
            player.setSalary(updatedPlayer.getSalary());
            return playerRepository.save(player);
        }
        throw new RuntimeException("Player not found with id: " + id);
    }

    public Player getPlayer(Long id) {
        return playerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Player not found with id: " + id));
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public List<Player> searchPlayers(String searchTerm) {
        return playerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchTerm, searchTerm);
    }

    public List<Player> getActivePlayers() {
        return playerRepository.findAllActivePlayers();
    }

    public List<Player> getFitPlayers() {
        return playerRepository.findAllFitPlayers();
    }
} 