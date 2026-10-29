package Ecom.ModelDTO;

import java.util.List;
import lombok.Data;

@Data
public class CartDTO {
    private Integer cartId;
    private Double totalAmount;
    private List<CartItemDTO> cartItems;
}
