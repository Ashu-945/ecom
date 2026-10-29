package Ecom.Service;

import java.math.BigDecimal;

import Ecom.Exception.PaymentException;
import Ecom.ModelDTO.PaymentDTO;

public interface PaymentService {
	
	 PaymentDTO makePayment(Integer orderId,Integer userId) throws PaymentException;
}
