package Ecom.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import Ecom.Exception.UserException;
import Ecom.ModelDTO.AdminDTO;
import Ecom.ModelDTO.CustomerDTO;
import Ecom.ModelDTO.UserDTO;
import Ecom.ModelDTO.UserResponseDTO;


@Service
public interface UserService {
	
	
	
	public UserResponseDTO getUserByEmailId(String emailId)throws UserException;

	public UserResponseDTO addUser(CustomerDTO customer)  throws UserException;
	
	public UserResponseDTO addUserAdmin(AdminDTO admin	)  throws UserException;

	public UserResponseDTO changePassword(Integer userId, UserDTO customer)  throws UserException;

	public String deactivateUser(Integer userId) throws UserException;

	public UserResponseDTO getUserDetails(Integer userId)throws UserException;

	public List<UserResponseDTO> getAllUserDetails() throws UserException;
}
