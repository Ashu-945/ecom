package Ecom.ModelDTO;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class AddressResponseDTO {
    private Integer addressID;
    @NotNull @NotBlank private String flatNo;
    @NotNull @NotBlank private String street;
    @NotNull @NotBlank private String city;
    @NotNull @NotBlank private String zipCode;
    @NotNull @NotBlank private String state;
}
