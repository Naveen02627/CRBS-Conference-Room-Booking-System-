package Techno.Carts.CRBS.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "hallsAvailable")
@CompoundIndex(
        name = "location_state_city_idx",
        def = "{ 'state': 1 }"
)
public class HallAvailableDataBase {

    @Id
    private String id;

    private String state;

    private List<String> city;
}

