package Ecom.Controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Ecom.Exception.AddressException;
import Ecom.ModelDTO.AddressResponseDTO;
import Ecom.Service.AddressService;
import Ecom.Service.UserService;
import Ecom.ModelDTO.UserResponseDTO;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecom/customer-addresses")
public class AddressController {

    private final AddressService addressService;
    private final UserService userService;

    @PostMapping("/{userId}")
    public ResponseEntity<AddressResponseDTO> addAddressToUser(
            @PathVariable Integer userId, @Valid @RequestBody AddressResponseDTO address, Authentication authentication) {
        Integer requesterId = requesterId(authentication);
        AddressResponseDTO addedAddress = addressService.addAddressToUser(userId, address, requesterId);
        return new ResponseEntity<>(addedAddress, HttpStatus.CREATED);
    }

    @PutMapping("/update/{addressId}")
    public ResponseEntity<AddressResponseDTO> updateAddress( @Valid @RequestBody AddressResponseDTO address,@PathVariable Integer addressId,
                                                               Authentication authentication) {
        AddressResponseDTO updatedAddress = addressService.updateAddress(address,addressId,requesterId(authentication));
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/delete/{addressId}")
    public ResponseEntity<String> removeAddress(@PathVariable Integer addressId, Authentication authentication) {
        addressService.removeAddress(addressId,requesterId(authentication));
        return ResponseEntity.ok("Address removed successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<AddressResponseDTO>> getAllUserAddress(@PathVariable Integer userId, Authentication authentication) {
        List<AddressResponseDTO> allUserAddress = addressService.getAllUserAddress(userId,requesterId(authentication));
        return ResponseEntity.ok(allUserAddress);
    }

    private Integer requesterId(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
        if (isAdmin) return null;
        UserResponseDTO currentUser = userService.getUserByEmailId(authentication.getName());
        if (currentUser.getUserId() == null) throw new AccessDeniedException("User account not found.");
        return currentUser.getUserId();
    }

}
