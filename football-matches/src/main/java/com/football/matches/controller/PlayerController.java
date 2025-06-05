package com.football.matches.controller;

import com.football.matches.dto.PlayerDTO;
import com.football.matches.mapper.PlayerMapper;
import com.football.matches.model.Player;
import com.football.matches.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "*")
public class PlayerController {
    private final PlayerService playerService;
    private final PlayerMapper playerMapper;

    @Autowired
    public PlayerController(PlayerService playerService, PlayerMapper playerMapper) {
        this.playerService = playerService;
        this.playerMapper = playerMapper;
    }

    @PostMapping
    public ResponseEntity<PlayerDTO> addPlayer(@RequestBody PlayerDTO playerDTO) {
        Player player = playerMapper.toEntity(playerDTO);
        Player savedPlayer = playerService.addPlayer(player);
        return ResponseEntity.ok(playerMapper.toDTO(savedPlayer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.ok().body(true);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long id, @RequestBody PlayerDTO playerDTO) {
        Player player = playerMapper.toEntity(playerDTO);
        Player updatedPlayer = playerService.updatePlayer(id, player);
        return ResponseEntity.ok(playerMapper.toDTO(updatedPlayer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable Long id) {
        Player player = playerService.getPlayer(id);
        return ResponseEntity.ok(playerMapper.toDTO(player));
    }

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        List<PlayerDTO> playerDTOs = players.stream()
                .map(playerMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(playerDTOs);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PlayerDTO>> searchPlayers(@RequestParam String searchTerm) {
        List<Player> players = playerService.searchPlayers(searchTerm);
        List<PlayerDTO> playerDTOs = players.stream()
                .map(playerMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(playerDTOs);
    }

    @GetMapping("/active")
    public ResponseEntity<List<PlayerDTO>> getActivePlayers() {
        List<Player> players = playerService.getActivePlayers();
        List<PlayerDTO> playerDTOs = players.stream()
                .map(playerMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(playerDTOs);
    }

    @GetMapping("/fit")
    public ResponseEntity<List<PlayerDTO>> getFitPlayers() {
        List<Player> players = playerService.getFitPlayers();
        List<PlayerDTO> playerDTOs = players.stream()
                .map(playerMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(playerDTOs);
    }
} 