package Techno.Carts.CRBS.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HodDecisionDto {
    private Long requestId;
    private String remark;
}
