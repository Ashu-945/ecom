package Ecom.ModelDTO;

import java.time.LocalDateTime;

import Ecom.Enum.UserAccountStatus;
import Ecom.Enum.UserRole;
import java.util.List;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserRole role;
    private LocalDateTime registerTime;
    private UserAccountStatus userAccountStatus;
    private List<AddressResponseDTO> address;
}
