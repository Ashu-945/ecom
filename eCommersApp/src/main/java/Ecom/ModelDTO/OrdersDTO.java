package Ecom.ModelDTO;

import java.util.List;



import lombok.Data;

@Data
public class OrdersDTO {

    private Integer orderId;
    private String status;
    private String orderDate;
    private Double orderAmount;
    private Double totalAmount;
    private String paymentStatus;
    private Integer userId;
    private List<OrderItemDTO> orderItem;
    private UserResponseDTO user;
    private PaymentDTO payment;

}
