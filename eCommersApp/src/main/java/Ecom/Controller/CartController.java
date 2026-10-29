package Ecom.Controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Ecom.ModelDTO.CartDTO;
import Ecom.ModelDTO.UserResponseDTO;
import Ecom.Service.CartService;
import Ecom.Service.UserService;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/ecom/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @PostMapping("/add-product")
    public ResponseEntity<CartDTO> addProductToCart(@RequestParam Integer userId, @RequestParam Integer productId, Authentication authentication) {
        CartDTO cart = cartService.addProductToCart(userId, productId, requesterId(authentication));
        return new ResponseEntity<>(cart, HttpStatus.CREATED);

    }

    @PutMapping("/increase-productQty/{cartId}/{productId}")
    public ResponseEntity<CartDTO> increaseProductQuantity(
            @PathVariable Integer cartId,
            @PathVariable Integer productId,
            Authentication authentication

    ) {
        CartDTO cart = cartService.increaseProductQuantity(cartId, productId, requesterId(authentication));
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/decrease-productQty/{cartId}/{productId}")
    public ResponseEntity<CartDTO> decreaseProductQuantity(
            @PathVariable Integer cartId,
            @PathVariable Integer productId,
            Authentication authentication

    ) {
        CartDTO cart = cartService.decreaseProductQuantity(cartId, productId, requesterId(authentication));
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove-product/{cartId}/{productId}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable Integer cartId, @PathVariable Integer productId, Authentication authentication) {
        cartService.removeProductFromCart(cartId, productId, requesterId(authentication));
        String msg = "Prodcut is removed from cart";
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    @DeleteMapping("/empty-Cart/{cartId}")
    public ResponseEntity<String> removeAllProductFromCart(@PathVariable Integer cartId, Authentication authentication) {
        cartService.removeAllProductFromCart(cartId, requesterId(authentication));
        String msg = "All product Remove From cart";
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    @GetMapping("/products/{cartId}")
    public ResponseEntity<CartDTO> getAllCartProducts(@PathVariable Integer cartId, Authentication authentication) {
        CartDTO products = cartService.getAllCartProduct(cartId, requesterId(authentication));
        return ResponseEntity.ok(products);
    }

    private Integer requesterId(Authentication authentication) {
        return userService.getUserByEmailId(authentication.getName()).getUserId();
    }
}
