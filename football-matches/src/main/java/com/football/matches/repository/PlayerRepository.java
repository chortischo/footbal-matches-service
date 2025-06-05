package com.football.matches.repository;

import com.football.matches.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    
    @Query("SELECT p FROM Player p WHERE p.status = 'ACTIVE'")
    List<Player> findAllActivePlayers();
    
    @Query("SELECT p FROM Player p WHERE p.healthStatus = 'FIT'")
    List<Player> findAllFitPlayers();
} 