package Ecom.ServiceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecom.Enum.OrderStatus;
import Ecom.Enum.PaymentMethod;
import Ecom.Enum.PaymentStatus;
import Ecom.Exception.PaymentException;
import Ecom.Exception.UserException;
import Ecom.Model.Orders;
import Ecom.Model.Payment;
import Ecom.Model.User;
import Ecom.ModelDTO.PaymentDTO;
import Ecom.Repository.OrderRepository;
import Ecom.Repository.PaymentRepository;
import Ecom.Repository.UserRepository;
import Ecom.Service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public PaymentDTO makePayment(Integer orderId, Integer userId) throws PaymentException {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found in the database."));

        Orders order = orderRepository.findById(orderId)
        		.orElseThrow(() -> new UserException("order not found in the database."));;
		if (order.getUser() == null || !order.getUser().getUserId().equals(userId))
			throw new PaymentException("Order does not belong to the given customer.");
		if (order.getPayment() != null)
			throw new PaymentException("Payment has already been recorded for this order.");

        Payment payment = new Payment();
        payment.setPaymentAmount(order.getTotalAmount());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(PaymentMethod.UPI);
        payment.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        payment.setUser(existingUser);
    
        payment.setOrder(order);
        paymentRepository.save(payment);
       
        order.setStatus(OrderStatus.SHIPPED);

        // Set the payment for the order
        order.setPayment(payment);
        // Save the changes to the Order entity, including the associated Payment
        orderRepository.save(order);

        existingUser.getPayments().add(payment);
        // Save the changes to the User entity, including the new payment association
        userRepository.save(existingUser);
        // Save the payment to the database
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setOrderId(order.getOrderId());
        dto.setUserId(existingUser.getUserId());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setPaymentAmount(payment.getPaymentAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentStatus(payment.getPaymentStatus());
        return dto;
    }
}
