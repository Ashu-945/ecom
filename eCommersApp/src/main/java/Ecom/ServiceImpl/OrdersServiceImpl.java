package Ecom.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import Ecom.Enum.OrderStatus;
import Ecom.Exception.OrdersException;
import Ecom.Exception.UserException;
import Ecom.Model.Cart;
import Ecom.Model.CartItem;
import Ecom.Model.OrderItem;
import Ecom.Model.Orders;
import Ecom.Model.User;
import Ecom.ModelDTO.OrdersDTO;
import Ecom.ModelDTO.OrderItemDTO;
import Ecom.ModelDTO.ProductDTO;
import Ecom.ModelDTO.UserResponseDTO;
import Ecom.ModelDTO.PaymentDTO;
import Ecom.Repository.CartItemRepository;
import Ecom.Repository.CartRepository;
import Ecom.Repository.OrderItemRepository;
import Ecom.Repository.OrderRepository;
import Ecom.Repository.ProductRepository;
import Ecom.Repository.UserRepository;
import Ecom.Service.OrdersService;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
@jakarta.transaction.Transactional
public class OrdersServiceImpl implements OrdersService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    private final CartRepository cartRepository;

    @Override
    public OrdersDTO placeOrder(Integer userId,Integer requesterUserId) throws OrdersException {
        if (requesterUserId != null && !requesterUserId.equals(userId))
            throw new OrdersException("You may only place an order for your own account.");
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User Not Found In Database"));

        Cart usercart = existingUser.getCart();
        if(usercart == null || usercart.getTotalAmount() == null || usercart.getTotalAmount() <= 0){
            throw new OrdersException("Add item To the cart first.......");
        }
        Integer cartId = usercart.getCartId();

        Orders newOrder = new Orders();

        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setStatus(OrderStatus.PENDING);

        existingUser.getOrders().add(newOrder);
        newOrder.setUser(existingUser);
        userRepository.save(existingUser);
        orderRepository.save(newOrder);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem itemDTO : usercart.getCartItems()) {
            System.out.println("inside the loop");
            if (itemDTO.getCart().getCartId() == cartId) {

                OrderItem orderItem = new OrderItem();// creating New orderItem;

                orderItem.setQuantity(itemDTO.getQuantity());
                orderItem.setProduct(itemDTO.getProduct());
                orderItem.setOrderId(newOrder.getOrderId());
                orderItems.add(orderItem);
                System.out.println("inside the loop and if");
            }
        }

        newOrder.setOrderItem(orderItems);
        newOrder.setTotalAmount(usercart.getTotalAmount());
        orderRepository.save(newOrder);


        usercart.setTotalAmount(usercart.getTotalAmount() - newOrder.getTotalAmount());
        cartItemRepository.removeAllProductFromCart(cartId);
        cartRepository.save(usercart);

        return toDTO(newOrder);

    }

    @Transactional
    public OrdersDTO getOrdersDetails(Integer orderId,Integer requesterUserId) throws OrdersException {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrdersException("Order not found in the database."));
        if (requesterUserId != null && (order.getUser() == null ||
                !requesterUserId.equals(order.getUser().getUserId())))
            throw new OrdersException("You may only view your own orders.");
        return toDTO(order);
    }

    @Override
    public List<OrdersDTO> getAllUserOrder(Integer userId,Integer requesterUserId) throws OrdersException {
        if (requesterUserId != null && !requesterUserId.equals(userId))
            throw new OrdersException("You may only view your own orders.");
        try {
            List<Orders> orders = orderRepository.getAllOrderByUserId(userId);
            if (orders.isEmpty()) {
                throw new OrdersException("No orders found for the user in the database.");
            }
            return orders.stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new OrdersException("Failed to fetch orders for the user: " + e.getMessage());
        }
    }

    @Override
    public List<OrdersDTO> viewAllOrders() throws OrdersException {

        List<Orders> orders = orderRepository.findAll();

        if (orders.isEmpty()) {
            throw new OrdersException("No orders found in the database.");
        }
        return orders.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrdersDTO> viewAllOrderByDate(LocalDate date) throws OrdersException {

        List<Orders> orders = orderRepository.findByOrderDateGreaterThanEqual(date.atStartOfDay());

        if (orders.isEmpty()) {
            throw new OrdersException("No orders found for the given date.");
        }

        return orders.stream().map(this::toDTO).collect(Collectors.toList());

    }

    @Override
    public void deleteOrders(Integer userId, Integer Orderid,Integer requesterUserId) throws OrdersException {
        if (requesterUserId != null && !requesterUserId.equals(userId))
            throw new OrdersException("You may only cancel your own orders.");
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User Not Found In Database"));
        Orders existingOrder = orderRepository.findById(Orderid)
                .orElseThrow(() -> new UserException("order Not Found In Database"));

        if (existingOrder.getUser() == null || !existingOrder.getUser().getUserId().equals(userId)) {
            throw new OrdersException("Order does not belong to the given user.");
        }

        orderRepository.delete(existingOrder);
    }

    @Override
    public OrdersDTO updateOrders(Integer ordersid, OrdersDTO orderDTo) throws OrdersException {
        Orders order = orderRepository.findById(ordersid)
                .orElseThrow(() -> new OrdersException("Order not found in the database."));
        try {
            order.setStatus(OrderStatus.valueOf(orderDTo.getStatus().toUpperCase()));
        } catch (RuntimeException ex) {
            throw new OrdersException("A valid order status is required.");
        }
        return toDTO(orderRepository.save(order));
    }

    private OrdersDTO toDTO(Orders order) {
        OrdersDTO dto = new OrdersDTO();
        dto.setOrderId(order.getOrderId());
        dto.setStatus(order.getStatus() == null ? null : order.getStatus().name());
        dto.setOrderDate(order.getOrderDate() == null ? null : order.getOrderDate().toString());
        dto.setOrderAmount(order.getTotalAmount());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPaymentStatus(order.getPayment() == null || order.getPayment().getPaymentStatus() == null
                ? "PENDING" : order.getPayment().getPaymentStatus().name());
        dto.setUserId(order.getUser() == null ? null : order.getUser().getUserId());
        if (order.getUser() != null) {
            UserResponseDTO userDTO = new UserResponseDTO();
            userDTO.setUserId(order.getUser().getUserId());
            userDTO.setEmail(order.getUser().getEmail());
            userDTO.setFirstName(order.getUser().getFirstName());
            userDTO.setLastName(order.getUser().getLastName());
            userDTO.setPhoneNumber(order.getUser().getPhoneNumber());
            dto.setUser(userDTO);
        }
        if (order.getPayment() != null) {
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setPaymentId(order.getPayment().getPaymentId());
            paymentDTO.setOrderId(order.getOrderId());
            paymentDTO.setUserId(order.getUser() == null ? null : order.getUser().getUserId());
            paymentDTO.setPaymentDate(order.getPayment().getPaymentDate());
            paymentDTO.setPaymentAmount(order.getPayment().getPaymentAmount());
            paymentDTO.setPaymentMethod(order.getPayment().getPaymentMethod());
            paymentDTO.setPaymentStatus(order.getPayment().getPaymentStatus());
            dto.setPayment(paymentDTO);
        }
        dto.setOrderItem(order.getOrderItem().stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setOrderItemId(item.getOrderItemId());
            itemDTO.setProductId(item.getProduct().getProductId());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(java.math.BigDecimal.valueOf(item.getProduct().getPrice()));
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductId(item.getProduct().getProductId());
            productDTO.setName(item.getProduct().getName());
            productDTO.setImageUrl(item.getProduct().getImageUrl());
            productDTO.setDescription(item.getProduct().getDescription());
            productDTO.setPrice(item.getProduct().getPrice());
            productDTO.setCategory(item.getProduct().getCategory());
            itemDTO.setProduct(productDTO);
            return itemDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

}
