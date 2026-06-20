package com.TechnoCarts.CRBS.ServiceTesting;


import com.TechnoCarts.CRBS.Dto.HallResponse;
import com.TechnoCarts.CRBS.Entity.Hall;
import com.TechnoCarts.CRBS.Entity.pair;
import com.TechnoCarts.CRBS.HandleExaption.HallNotFound;
import com.TechnoCarts.CRBS.Repository.HallRepository;
import com.TechnoCarts.CRBS.Services.BookingService.CheckAvailability;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CheckAvailabilityTest {

    @Mock
    private HallRepository hallRepository;

    @InjectMocks
    private CheckAvailability checkAvailability;

    @Test
    void shouldReturnAvailableHallWhenDateNotExists() {

        LocalDate date = LocalDate.of(2026, 6, 19);

        Hall hall = new Hall();
        hall.setName("Hall A");
        hall.setDetails("Big Hall");
        hall.setImageUrl("image.jpg");
        hall.setSlots(new ConcurrentHashMap<>());

        when(hallRepository.findAll())
                .thenReturn(List.of(hall));

        ResponseEntity<List<HallResponse>> response =
                checkAvailability.CheckHall(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<HallResponse> body = response.getBody();

        assertNotNull(body);
        assertEquals(1, body.size());

        assertEquals("Hall A", body.get(0).getHallName());

        verify(hallRepository, times(1)).findAll();
    }
    @Test
    void shouldReturnHallWhenAtLeastOneSlotIsAvailable() {

        LocalDate date = LocalDate.of(2026, 6, 19);

        pair[] slots = {
                new pair(1,0),
                new pair(0,0),
                new pair(1,0),
                new pair(1,0),
                new pair(1,0),
                new pair(1,0)
        };

        ConcurrentHashMap<LocalDate,pair[]> map =
                new ConcurrentHashMap<>();

        map.put(date, slots);

        Hall hall = new Hall();
        hall.setName("Hall B");
        hall.setDetails("Conference Hall");
        hall.setImageUrl("img.jpg");
        hall.setSlots(map);

        when(hallRepository.findAll())
                .thenReturn(List.of(hall));

        ResponseEntity<List<HallResponse>> response =
                checkAvailability.CheckHall(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<HallResponse> body = response.getBody();

        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals("Hall B", body.get(0).getHallName());
    }
    @Test
    void shouldThrowHallNotFoundWhenNoHallExists() {

        LocalDate date = LocalDate.of(2026, 6, 19);

        when(hallRepository.findAll())
                .thenReturn(Collections.emptyList());

        assertThrows(
                HallNotFound.class,
                () -> checkAvailability.CheckHall(date)
        );

    }

    @Test
    void shouldThrowHallNotFoundWhenAllSlotsBooked() {

        LocalDate date = LocalDate.of(2026, 6, 19);

        pair[] slots = {
                new pair(1,101),
                new pair(1,102),
                new pair(1,103)
        };

        ConcurrentHashMap<LocalDate,pair[]> map =
                new ConcurrentHashMap<>();

        map.put(date, slots);

        Hall hall = new Hall();
        hall.setName("Hall C");
        hall.setSlots(map);

        when(hallRepository.findAll())
                .thenReturn(List.of(hall));

        HallNotFound exception = assertThrows(
                HallNotFound.class,
                () -> checkAvailability.CheckHall(date)
        );

        assertEquals(
                "No hall available on " + date,
                exception.getMessage()
        );

    }

}
