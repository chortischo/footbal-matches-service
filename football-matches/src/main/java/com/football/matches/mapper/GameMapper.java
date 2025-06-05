package com.football.matches.mapper;

import com.football.matches.dto.GameDTO;
import com.football.matches.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class GameMapper {
    
    private final PlayerMapper playerMapper;
    private final StadiumMapper stadiumMapper;
    
    @Autowired
    public GameMapper(PlayerMapper playerMapper, StadiumMapper stadiumMapper) {
        this.playerMapper = playerMapper;
        this.stadiumMapper = stadiumMapper;
    }
    
    public GameDTO toDTO(Game game) {
        if (game == null) {
            return null;
        }
        
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        dto.setDateTime(game.getDateTime());
        dto.setOpponentTeam(game.getOpponentTeam());
        dto.setStadium(stadiumMapper.toDTO(game.getStadium()));
        dto.setPlayers(game.getPlayers().stream()
                .map(playerMapper::toDTO)
                .collect(Collectors.toList()));
        dto.setAttendance(game.getAttendance());
        dto.setResult(game.getResult());
        dto.setStatus(game.getStatus());
        return dto;
    }
    
    public Game toEntity(GameDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Game game = new Game();
        game.setId(dto.getId());
        game.setDateTime(dto.getDateTime());
        game.setOpponentTeam(dto.getOpponentTeam());
        game.setStadium(stadiumMapper.toEntity(dto.getStadium()));
        game.setPlayers(dto.getPlayers() == null ? new ArrayList<>() : dto.getPlayers().stream()
                .map(playerMapper::toEntity)
                .collect(Collectors.toList()));
        game.setAttendance(dto.getAttendance());
        game.setResult(dto.getResult());
        game.setStatus(dto.getStatus());
        return game;
    }
} 