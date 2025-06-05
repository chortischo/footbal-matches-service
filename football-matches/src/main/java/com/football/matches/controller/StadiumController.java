package com.football.matches.controller;

import com.football.matches.dto.StadiumDTO;
import com.football.matches.mapper.StadiumMapper;
import com.football.matches.model.Stadium;
import com.football.matches.service.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stadiums")
@CrossOrigin(origins = "*")
public class StadiumController {
    private final StadiumService stadiumService;
    private final StadiumMapper stadiumMapper;

    @Autowired
    public StadiumController(StadiumService stadiumService, StadiumMapper stadiumMapper) {
        this.stadiumService = stadiumService;
        this.stadiumMapper = stadiumMapper;
    }

    @PostMapping
    public ResponseEntity<StadiumDTO> addStadium(@RequestBody StadiumDTO stadiumDTO) {
        Stadium stadium = stadiumMapper.toEntity(stadiumDTO);
        Stadium savedStadium = stadiumService.addStadium(stadium);
        return ResponseEntity.ok(stadiumMapper.toDTO(savedStadium));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteStadium(@PathVariable Long id) {
        stadiumService.deleteStadium(id);
        return ResponseEntity.ok().body(true);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StadiumDTO> updateStadium(@PathVariable Long id, @RequestBody StadiumDTO stadiumDTO) {
        Stadium stadium = stadiumMapper.toEntity(stadiumDTO);
        Stadium updatedStadium = stadiumService.updateStadium(id, stadium);
        return ResponseEntity.ok(stadiumMapper.toDTO(updatedStadium));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StadiumDTO> getStadium(@PathVariable Long id) {
        Stadium stadium = stadiumService.getStadium(id);
        return ResponseEntity.ok(stadiumMapper.toDTO(stadium));
    }

    @GetMapping
    public ResponseEntity<List<StadiumDTO>> getAllStadiums() {
        List<Stadium> stadiums = stadiumService.getAllStadiums();
        List<StadiumDTO> stadiumDTOs = stadiums.stream()
                .map(stadiumMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(stadiumDTOs);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StadiumDTO>> searchStadiums(@RequestParam String name) {
        List<Stadium> stadiums = stadiumService.searchStadiums(name);
        List<StadiumDTO> stadiumDTOs = stadiums.stream()
                .map(stadiumMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(stadiumDTOs);
    }

    @GetMapping("/capacity")
    public ResponseEntity<List<StadiumDTO>> getStadiumsByMinCapacity(@RequestParam Integer minCapacity) {
        List<Stadium> stadiums = stadiumService.getStadiumsByMinCapacity(minCapacity);
        List<StadiumDTO> stadiumDTOs = stadiums.stream()
                .map(stadiumMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(stadiumDTOs);
    }

    @GetMapping("/price")
    public ResponseEntity<List<StadiumDTO>> getStadiumsByMaxPrice(@RequestParam Double maxPrice) {
        List<Stadium> stadiums = stadiumService.getStadiumsByMaxPrice(maxPrice);
        List<StadiumDTO> stadiumDTOs = stadiums.stream()
                .map(stadiumMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(stadiumDTOs);
    }
} 