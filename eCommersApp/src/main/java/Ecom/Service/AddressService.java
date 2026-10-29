package Ecom.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import Ecom.Exception.AddressException;
import Ecom.ModelDTO.AddressResponseDTO;

@Service
public interface AddressService {
	
	public AddressResponseDTO addAddressToUser(Integer userId,AddressResponseDTO address,Integer requesterUserId) throws AddressException;
	
	public AddressResponseDTO updateAddress( AddressResponseDTO address,Integer addressId,Integer requesterUserId) throws AddressException ;
	
	public void   removeAddress(Integer addressId,Integer requesterUserId)throws AddressException;
	
	public List<AddressResponseDTO> getAllUserAddress(Integer userId,Integer requesterUserId)throws AddressException;

}
