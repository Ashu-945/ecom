package Ecom.Controller;

import java.time.LocalDate;
import java.util.List;

import Ecom.ModelDTO.OrdersDTO;
import Ecom.ModelDTO.UserResponseDTO;
import Ecom.Service.UserService;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Ecom.Service.OrdersService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecom/orders")
public class OrderController {

    private final OrdersService ordersService;
    private final UserService userService;


    @PostMapping("/placed/{userid}")
    public ResponseEntity<OrdersDTO> addOrderToCart(@PathVariable("userid") Integer userid, Authentication authentication) {
        OrdersDTO placeOrder = ordersService.placeOrder(userid, requesterId(authentication));
        return ResponseEntity.ok(placeOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrdersDTO> getOrdersDetails(@PathVariable("orderId") Integer orderId, Authentication authentication) {
        OrdersDTO order = ordersService.getOrdersDetails(orderId, requesterId(authentication));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity<List<OrdersDTO>> getAllUserOrder(@PathVariable Integer userId, Authentication authentication) {
        List<OrdersDTO> orders = ordersService.getAllUserOrder(userId, requesterId(authentication));
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrdersDTO>> viewAllOrders() {
        List<OrdersDTO> orders = ordersService.viewAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<OrdersDTO>> viewAllOrderByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<OrdersDTO> orders = ordersService.viewAllOrderByDate(date);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/{orderId}")
    public ResponseEntity<String> deleteOrders(@PathVariable Integer userId, @PathVariable Integer orderId, Authentication authentication) {
        ordersService.deleteOrders(userId, orderId, requesterId(authentication));
        return new ResponseEntity<>("Order successfully deleted.", HttpStatus.OK);
    }

    private Integer requesterId(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
        if (isAdmin) return null;
        UserResponseDTO currentUser = userService.getUserByEmailId(authentication.getName());
        return currentUser.getUserId();
    }

}
