package com.football.matches.mapper;

import com.football.matches.dto.PlayerDTO;
import com.football.matches.model.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {
    
    public PlayerDTO toDTO(Player player) {
        if (player == null) {
            return null;
        }
        
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setFirstName(player.getFirstName());
        dto.setLastName(player.getLastName());
        dto.setDateOfBirth(player.getDateOfBirth());
        dto.setStatus(player.getStatus());
        dto.setHealthStatus(player.getHealthStatus());
        dto.setSalary(player.getSalary());
        return dto;
    }
    
    public Player toEntity(PlayerDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Player player = new Player();
        player.setId(dto.getId());
        player.setFirstName(dto.getFirstName());
        player.setLastName(dto.getLastName());
        player.setDateOfBirth(dto.getDateOfBirth());
        player.setStatus(dto.getStatus());
        player.setHealthStatus(dto.getHealthStatus());
        player.setSalary(dto.getSalary());
        return player;
    }
} 