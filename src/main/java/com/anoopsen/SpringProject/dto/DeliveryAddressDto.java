package com.anoopsen.SpringProject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressDto {
	
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String addressLine1;

    private String addressLine2;

    @NotBlank
    @Size(min = 6, max = 6)
    private String postalCode;

    @NotBlank
    private String townCity;

    @NotNull
    @Size(min = 10, max = 10)
    private String phoneNumber;

    @NotBlank
    @Email
    private String email;;
}
