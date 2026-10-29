package Ecom.ModelDTO;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

@Data
public class OrderItemDTO {

	    private int productId;
	    private int quantity;
	    private BigDecimal price;
	    private Integer orderItemId;
	    private ProductDTO product;
	    
}
