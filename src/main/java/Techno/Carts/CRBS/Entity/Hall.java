package Techno.Carts.CRBS.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "halls")
@CompoundIndex(
        name = "location_state_city_idx",
        def = "{ 'location.state': 1, 'location.city': 1 }"
)
public class Hall {

    @Id
    private String id;

    private String hallName;
    private String description;
    private String profileImageUrl;

    private Location location;

    private double price;

    private int capacity;

    private Map<String, Set<Integer>> bookedSlots = new HashMap<>();
}