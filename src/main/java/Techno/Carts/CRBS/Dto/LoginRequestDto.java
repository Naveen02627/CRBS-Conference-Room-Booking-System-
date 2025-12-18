package Techno.Carts.CRBS.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class LoginRequestDto {
    private String username;
    private String password;


}
