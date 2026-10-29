package Ecom.ModelDTO;

import lombok.Data;

@Data
public class CartItemDTO {
    private Integer cartItemId;
    private Integer quantity;
    private ProductDTO product;
}
