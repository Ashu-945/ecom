package Ecom.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Ecom.Enum.UserAccountStatus;
import Ecom.Enum.UserRole;
import Ecom.Exception.UserException;
import Ecom.Model.User;
import Ecom.ModelDTO.AdminDTO;
import Ecom.ModelDTO.CustomerDTO;
import Ecom.ModelDTO.UserDTO;
import Ecom.ModelDTO.UserResponseDTO;
import Ecom.Repository.UserRepository;
import Ecom.Service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserResponseDTO getUserByEmailId(String emailId) throws UserException {
		return toResponseDTO(userRepository.findByEmail(emailId).orElseThrow(() -> new UserException("User not found")));

	}

	@Override
	public UserResponseDTO addUser(CustomerDTO customer) throws UserException {
		if (customer == null)
			throw new UserException("customer Can not be Null");
		Optional<User> findByEmail = userRepository.findByEmail(customer.getEmail());
		if (findByEmail.isPresent()) {
			throw new UserException("Email is already registered");
		}

		User newCustomer = new User();
		newCustomer.setEmail(customer.getEmail());
		newCustomer.setPassword(customer.getPassword());
		newCustomer.setFirstName(customer.getFirstName());
		newCustomer.setLastName(customer.getLastName());
		newCustomer.setPhoneNumber(customer.getPhoneNumber());
		newCustomer.setRole(UserRole.ROLE_USER);
		newCustomer.setRegisterTime(LocalDateTime.now());
		newCustomer.setUserAccountStatus(UserAccountStatus.ACTIVE);

		return toResponseDTO(userRepository.save(newCustomer));
	}

	@Override
	public UserResponseDTO addUserAdmin(AdminDTO customer) throws UserException {
		if (customer == null)
			throw new UserException("admin Can not be Null");
		Optional<User> findByEmail = userRepository.findByEmail(customer.getEmail());
		if (findByEmail.isPresent()) {
			throw new UserException("Email is already registered");
		}
		User newAdmin = new User();
		newAdmin.setEmail(customer.getEmail());
		newAdmin.setPassword(customer.getPassword());
		newAdmin.setFirstName(customer.getFirstName());
		newAdmin.setLastName(customer.getLastName());
		newAdmin.setPhoneNumber(customer.getPhoneNumber());
		newAdmin.setRole(UserRole.ROLE_ADMIN);
		newAdmin.setRegisterTime(LocalDateTime.now());
		newAdmin.setUserAccountStatus(UserAccountStatus.ACTIVE);

		return toResponseDTO(userRepository.save(newAdmin));
	}

	public UserResponseDTO changePassword(Integer userId, UserDTO customer) throws UserException {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
		if (customer.getNewPassword().length() >= 5 && customer.getNewPassword().length() <= 10) {
			user.updatePassword(customer.getNewPassword(), passwordEncoder);
			return toResponseDTO(userRepository.save(user));
		} else {
			throw new UserException("provide valid  password");
		}

	}

	
	@Override
	public String deactivateUser(Integer userId) throws UserException {
		User existingUser = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
		existingUser.setUserAccountStatus(UserAccountStatus.DEACTIVETE);
		userRepository.save(existingUser);
		return "Account deactivet Succesfully";
	}

	@Override
	public UserResponseDTO getUserDetails(Integer userId) throws UserException {
		User existingUser = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
		return toResponseDTO(existingUser);
	}

	@Override
	public List<UserResponseDTO> getAllUserDetails() throws UserException {

		List<User> existingAllUser = userRepository.findAll();
		if (existingAllUser.isEmpty()) {
			throw new UserException("User list is empty");
		}
		return existingAllUser.stream().map(this::toResponseDTO).toList();
	}

	private UserResponseDTO toResponseDTO(User user) {
		UserResponseDTO dto = new UserResponseDTO();
		dto.setUserId(user.getUserId());
		dto.setEmail(user.getEmail());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setPhoneNumber(user.getPhoneNumber());
		dto.setRole(user.getRole());
		dto.setRegisterTime(user.getRegisterTime());
		dto.setUserAccountStatus(user.getUserAccountStatus());
		dto.setAddress(user.getAddress().stream().map(address -> {
			Ecom.ModelDTO.AddressResponseDTO addressDTO = new Ecom.ModelDTO.AddressResponseDTO();
			addressDTO.setAddressID(address.getAddressID());
			addressDTO.setFlatNo(address.getFlatNo());
			addressDTO.setStreet(address.getStreet());
			addressDTO.setCity(address.getCity());
			addressDTO.setZipCode(address.getZipCode());
			addressDTO.setState(address.getState());
			return addressDTO;
		}).toList());
		return dto;
	}

	

}
