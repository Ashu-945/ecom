package Ecom.Controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import Ecom.ModelDTO.CustomerDTO;
import Ecom.ModelDTO.UserDTO;
import Ecom.ModelDTO.UserResponseDTO;
import Ecom.Service.UserService;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecom/customers")
public class CustomerController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody CustomerDTO user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserResponseDTO addedUser = userService.addUser(user);
        return ResponseEntity.ok(addedUser);
    }

    @PutMapping("/update-password/{customerId}")
    public ResponseEntity<UserResponseDTO> updateUserPassword(@PathVariable("customerId") Integer customerId,
                                                  @Valid @RequestBody UserDTO userdto,
                                                  Authentication authentication) {
        ensureSelfOrAdmin(customerId, authentication);
        UserResponseDTO updatedUser = userService.changePassword(customerId, userdto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/deactivate/{customerid}")
    public ResponseEntity<String> deactivateUser(@PathVariable("customerid") Integer customerId,
                                                 Authentication authentication) {
        ensureSelfOrAdmin(customerId, authentication);
        System.out.println("inside the deactivate method");
        String message = userService.deactivateUser(customerId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{customerid}")
    public ResponseEntity<UserResponseDTO> getUserDetails(@PathVariable("customerid") Integer customerId,
                                                           Authentication authentication) {
        ensureSelfOrAdmin(customerId, authentication);
        UserResponseDTO user = userService.getUserDetails(customerId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get-all-customer")
    public ResponseEntity<List<UserResponseDTO>> getAllUserDetails() {
        List<UserResponseDTO> users = userService.getAllUserDetails();
        return ResponseEntity.ok(users);
    }

    private void ensureSelfOrAdmin(Integer requestedUserId, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
        if (!isAdmin && !requestedUserId.equals(userService.getUserByEmailId(authentication.getName()).getUserId())) {
            throw new AccessDeniedException("You may only access your own account.");
        }
    }

}
