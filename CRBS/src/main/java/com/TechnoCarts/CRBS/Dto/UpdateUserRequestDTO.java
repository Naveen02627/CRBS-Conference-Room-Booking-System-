package com.TechnoCarts.CRBS.Dto;


import com.TechnoCarts.CRBS.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDTO {

    private String email;
    private Role role;
    private String mobNumber;
}
