package Techno.Carts.CRBS.Services;
import Techno.Carts.CRBS.Dto.HallSearchRequestDto;
import Techno.Carts.CRBS.Dto.HallSearchResponseDto;
import Techno.Carts.CRBS.Entity.Hall;
import Techno.Carts.CRBS.Repository.HallRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
@AllArgsConstructor
public class BookingSearchServices {

    private final HallRepository hallRepository;
    private static final int MAX_SLOT = 3;

    public List<HallSearchResponseDto> getAvailableHalls(
            HallSearchRequestDto hallSearchRequestDto) {

        List<Hall> halls = hallRepository
                .findByLocationStateAndLocationCity(
                        hallSearchRequestDto.getState(),
                        hallSearchRequestDto.getCity());

        List<HallSearchResponseDto> availableHalls = new ArrayList<>();

        String dateKey = hallSearchRequestDto
                .getRequestedDate()
                .toString();

        for (Hall hall : halls) {

            Map<String, Set<Integer>> bookedSlotsMap = hall.getBookedSlots();

            Set<Integer> slots =
                    bookedSlotsMap != null
                            ? bookedSlotsMap.getOrDefault(dateKey, new HashSet<>())
                            : new HashSet<>();

            // If all slots booked, skip hall
            if (slots.size() >= MAX_SLOT) {
                continue;
            }

            // Convert Set → List for response
            List<Integer> bookedSlotIds = new ArrayList<>(slots);

            availableHalls.add(
                    HallSearchResponseDto.builder()
                            .hallId(hall.getId())
                            .hallName(hall.getHallName())
                            .address(hall.getLocation().getAddress())
                            .city(hall.getLocation().getCity())
                            .state(hall.getLocation().getState())
                            .bookedSlots(bookedSlotIds)
                            .capacity(hall.getCapacity())
                            .price(hall.getPrice())
                            .description(hall.getDescription())
                            .imageUrl(hall.getProfileImageUrl())
                            .build()
            );
        }

        return availableHalls;
    }

}
