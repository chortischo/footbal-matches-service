package com.football.matches.mapper;

import com.football.matches.dto.StadiumDTO;
import com.football.matches.model.Stadium;
import org.springframework.stereotype.Component;

@Component
public class StadiumMapper {
    
    public StadiumDTO toDTO(Stadium stadium) {
        if (stadium == null) {
            return null;
        }
        
        StadiumDTO dto = new StadiumDTO();
        dto.setId(stadium.getId());
        dto.setName(stadium.getName());
        dto.setCapacity(stadium.getCapacity());
        dto.setPricePerSeat(stadium.getPricePerSeat());
        return dto;
    }
    
    public Stadium toEntity(StadiumDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Stadium stadium = new Stadium();
        stadium.setId(dto.getId());
        stadium.setName(dto.getName());
        stadium.setCapacity(dto.getCapacity());
        stadium.setPricePerSeat(dto.getPricePerSeat());
        return stadium;
    }
} 