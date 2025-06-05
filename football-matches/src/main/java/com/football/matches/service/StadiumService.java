package com.football.matches.service;

import com.football.matches.model.Stadium;
import com.football.matches.repository.StadiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StadiumService {
    private final StadiumRepository stadiumRepository;

    @Autowired
    public StadiumService(StadiumRepository stadiumRepository) {
        this.stadiumRepository = stadiumRepository;
    }

    public Stadium addStadium(Stadium stadium) {
        return stadiumRepository.save(stadium);
    }

    public void deleteStadium(Long id) {
        stadiumRepository.deleteById(id);
    }

    public Stadium updateStadium(Long id, Stadium updatedStadium) {
        Optional<Stadium> existingStadium = stadiumRepository.findById(id);
        if (existingStadium.isPresent()) {
            Stadium stadium = existingStadium.get();
            stadium.setName(updatedStadium.getName());
            stadium.setCapacity(updatedStadium.getCapacity());
            stadium.setPricePerSeat(updatedStadium.getPricePerSeat());
            return stadiumRepository.save(stadium);
        }
        throw new RuntimeException("Stadium not found with id: " + id);
    }

    public Stadium getStadium(Long id) {
        return stadiumRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Stadium not found with id: " + id));
    }

    public List<Stadium> getAllStadiums() {
        return stadiumRepository.findAll();
    }

    public List<Stadium> searchStadiums(String name) {
        return stadiumRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Stadium> getStadiumsByMinCapacity(Integer minCapacity) {
        return stadiumRepository.findByMinCapacity(minCapacity);
    }

    public List<Stadium> getStadiumsByMaxPrice(Double maxPrice) {
        return stadiumRepository.findByMaxPricePerSeat(maxPrice);
    }
} 