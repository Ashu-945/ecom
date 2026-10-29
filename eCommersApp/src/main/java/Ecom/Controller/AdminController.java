package Ecom.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Ecom.ModelDTO.AdminDTO;
import Ecom.ModelDTO.UserDTO;
import Ecom.ModelDTO.UserResponseDTO;
import Ecom.Service.UserService;

@RestController
@RequestMapping("/ecom/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody AdminDTO user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserResponseDTO addedUser = userService.addUserAdmin(user);
        return ResponseEntity.ok(addedUser);
    }

    @PutMapping("/updatepassword/{adminId}")
    public ResponseEntity<UserResponseDTO> updateUserPassword(@PathVariable("adminId") Integer customerId, @RequestBody UserDTO userdto) {
        UserResponseDTO updatedUser = userService.changePassword(customerId, userdto);
        return ResponseEntity.ok(updatedUser);
    }


}
