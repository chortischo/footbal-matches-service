package com.football.matches.repository;

import com.football.matches.model.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {
    List<Stadium> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT s FROM Stadium s WHERE s.capacity >= :minCapacity")
    List<Stadium> findByMinCapacity(Integer minCapacity);
    
    @Query("SELECT s FROM Stadium s WHERE s.pricePerSeat <= :maxPrice")
    List<Stadium> findByMaxPricePerSeat(Double maxPrice);
} 