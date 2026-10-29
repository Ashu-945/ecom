package Ecom.ModelDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShipperDTO {
    private Integer shipperId;
    @NotBlank private String name;
    @NotBlank @Size(min = 10, max = 12) private String phoneNumber;
}
