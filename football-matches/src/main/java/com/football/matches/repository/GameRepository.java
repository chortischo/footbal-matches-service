package com.football.matches.repository;

import com.football.matches.model.Game;
import com.football.matches.model.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    List<Game> findByOpponentTeamContainingIgnoreCase(String opponentTeam);

    List<Game> findByOpponentTeamContainingIgnoreCaseAndDateTimeBetween(String opponentTeam, LocalDateTime start, LocalDateTime end);

    List<Game> findByResult(GameResult result);
    
    @Query("SELECT g FROM Game g WHERE g.status = 'SCHEDULED' ORDER BY g.dateTime")
    List<Game> findAllScheduledGames();
    
    @Query("SELECT g FROM Game g WHERE g.stadium.id = :stadiumId")
    List<Game> findByStadiumId(Long stadiumId);
    
    @Query("SELECT g FROM Game g JOIN g.players p WHERE p.id = :playerId")
    List<Game> findByPlayerId(@Param("playerId") Long playerId);

    List<Game> findByDateTimeIsBetween(LocalDateTime start, LocalDateTime end);
} 