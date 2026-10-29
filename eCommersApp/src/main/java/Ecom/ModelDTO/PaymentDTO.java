package Ecom.ModelDTO;

import java.time.LocalDateTime;
import Ecom.Enum.PaymentMethod;
import Ecom.Enum.PaymentStatus;
import lombok.Data;

@Data
public class PaymentDTO {
    private Long paymentId;
    private Integer orderId;
    private Integer userId;
    private LocalDateTime paymentDate;
    private double paymentAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
}
