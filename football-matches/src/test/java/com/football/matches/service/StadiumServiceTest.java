package com.football.matches.service;

import com.football.matches.model.Stadium;
import com.football.matches.repository.StadiumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class StadiumServiceTest {

    @Mock
    private StadiumRepository stadiumRepository;

    @InjectMocks
    private StadiumService stadiumService;

    private Stadium testStadium;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testStadium = new Stadium();
        testStadium.setId(1L);
        testStadium.setName("Test Stadium");
        testStadium.setCapacity(50000);
        testStadium.setPricePerSeat(new BigDecimal("50.00"));
    }

    @Test
    void addStadium_ShouldReturnSavedStadium() {
        when(stadiumRepository.save(any(Stadium.class))).thenReturn(testStadium);

        Stadium savedStadium = stadiumService.addStadium(testStadium);

        assertNotNull(savedStadium);
        assertEquals(testStadium.getName(), savedStadium.getName());
        assertEquals(testStadium.getCapacity(), savedStadium.getCapacity());
        verify(stadiumRepository, times(1)).save(any(Stadium.class));
    }

    @Test
    void deleteStadium_ShouldDeleteStadium() {
        doNothing().when(stadiumRepository).deleteById(1L);

        stadiumService.deleteStadium(1L);

        verify(stadiumRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateStadium_ShouldUpdateAndReturnStadium() {
        Stadium updatedStadium = new Stadium();
        updatedStadium.setName("Updated Stadium");
        updatedStadium.setCapacity(60000);
        updatedStadium.setPricePerSeat(new BigDecimal("60.00"));

        when(stadiumRepository.findById(1L)).thenReturn(Optional.of(testStadium));
        when(stadiumRepository.save(any(Stadium.class))).thenReturn(updatedStadium);

        Stadium result = stadiumService.updateStadium(1L, updatedStadium);

        assertNotNull(result);
        assertEquals(updatedStadium.getName(), result.getName());
        assertEquals(updatedStadium.getCapacity(), result.getCapacity());
        verify(stadiumRepository, times(1)).save(any(Stadium.class));
    }

    @Test
    void updateStadium_ShouldThrowException_WhenStadiumNotFound() {
        Stadium updatedStadium = new Stadium();
        when(stadiumRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> stadiumService.updateStadium(1L, updatedStadium));
    }

    @Test
    void getStadium_ShouldReturnStadium() {
        when(stadiumRepository.findById(1L)).thenReturn(Optional.of(testStadium));

        Stadium result = stadiumService.getStadium(1L);

        assertNotNull(result);
        assertEquals(testStadium.getId(), result.getId());
        assertEquals(testStadium.getName(), result.getName());
    }

    @Test
    void getStadium_ShouldThrowException_WhenStadiumNotFound() {
        when(stadiumRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> stadiumService.getStadium(1L));
    }

    @Test
    void getAllStadiums_ShouldReturnListOfStadiums() {
        List<Stadium> stadiums = Arrays.asList(testStadium);
        when(stadiumRepository.findAll()).thenReturn(stadiums);

        List<Stadium> result = stadiumService.getAllStadiums();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testStadium.getId(), result.get(0).getId());
    }

    @Test
    void searchStadiums_ShouldReturnMatchingStadiums() {
        String searchTerm = "Test";
        List<Stadium> stadiums = Arrays.asList(testStadium);
        when(stadiumRepository.findByNameContainingIgnoreCase(searchTerm)).thenReturn(stadiums);

        List<Stadium> result = stadiumService.searchStadiums(searchTerm);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testStadium.getName(), result.get(0).getName());
    }

    @Test
    void getStadiumsByMinCapacity_ShouldReturnStadiumsWithMinCapacity() {
        Integer minCapacity = 40000;
        List<Stadium> stadiums = Arrays.asList(testStadium);
        when(stadiumRepository.findByMinCapacity(minCapacity)).thenReturn(stadiums);

        List<Stadium> result = stadiumService.getStadiumsByMinCapacity(minCapacity);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getCapacity() >= minCapacity);
    }

    @Test
    void getStadiumsByMaxPrice_ShouldReturnStadiumsWithMaxPrice() {
        Double maxPrice = 60.0;
        List<Stadium> stadiums = Arrays.asList(testStadium);
        when(stadiumRepository.findByMaxPricePerSeat(maxPrice)).thenReturn(stadiums);

        List<Stadium> result = stadiumService.getStadiumsByMaxPrice(maxPrice);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getPricePerSeat().doubleValue() <= maxPrice);
    }
} 