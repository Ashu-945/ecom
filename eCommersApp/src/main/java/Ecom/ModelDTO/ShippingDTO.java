package Ecom.ModelDTO;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class ShippingDTO {
	private Long shippingId;

	@NotNull @NotBlank private String address;

	@NotNull @NotBlank private String city;

	@NotNull @NotBlank private String state;

	@NotNull @NotBlank private String postalCode;
	private String country;
	private Integer orderId;
	private Integer shipperId;
}
