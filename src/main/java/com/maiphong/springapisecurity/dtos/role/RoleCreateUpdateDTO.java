package com.maiphong.springapisecurity.dtos.role;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateUpdateDTO {
    @NotBlank(message = "Name is required")
    @Length(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;

    @Length(max = 500, message = "Description must be less than 500 characters")
    private String description;
}
