package Ecom.Controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Ecom.ModelDTO.ShippingDTO;
import Ecom.Service.ShippingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ecom/order-shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping("/{orderId}/{shipperId}")
    public ResponseEntity<ShippingDTO> setShippingDetails(@PathVariable Integer orderId,
                                                              @PathVariable Integer shipperId,
                                                              @Valid @RequestBody ShippingDTO shippingDetails) {
        ShippingDTO savedShippingDetails = shippingService.setShippingDetails(orderId, shipperId,
                shippingDetails);
        return new ResponseEntity<>(savedShippingDetails, HttpStatus.CREATED);
    }

    @PutMapping("/{shippingId}")
    public ResponseEntity<ShippingDTO> updateShippingAddress(@PathVariable Integer shippingId,
                                                                 @Valid @RequestBody ShippingDTO shippingDTO) {
        ShippingDTO updatedShippingDetails = shippingService.updateShippingAddress(shippingId, shippingDTO);
        return new ResponseEntity<>(updatedShippingDetails, HttpStatus.OK);
    }

    @DeleteMapping("/{shippingId}")
    public ResponseEntity<Void> deleteShippingDetails(@PathVariable Integer shippingId) {
        shippingService.deleteShippingDetails(shippingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
