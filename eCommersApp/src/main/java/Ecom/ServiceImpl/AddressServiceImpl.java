package Ecom.ServiceImpl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecom.Exception.AddressException;
import Ecom.Exception.UserException;
import Ecom.Model.Address;
import Ecom.Model.User;
import Ecom.Repository.AddressRepository;
import Ecom.Repository.UserRepository;
import Ecom.Service.AddressService;
import Ecom.ModelDTO.AddressResponseDTO;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;


    @Override
    public AddressResponseDTO addAddressToUser(Integer userId, AddressResponseDTO address, Integer requesterUserId) throws AddressException {
        if (requesterUserId != null && !requesterUserId.equals(userId))
            throw new AddressException("You may only add an address to your own account.");
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Fouund"));
        Address entity = new Address();
        copyToEntity(address, entity);
        Address saveaAddress = addressRepository.save(entity);
        saveaAddress.setUser(existingUser);

        existingUser.getAddress().add(saveaAddress);
        userRepository.save(existingUser);
        return toDTO(addressRepository.save(saveaAddress));
    }

    @Override
    public AddressResponseDTO updateAddress(AddressResponseDTO address,Integer addressId,Integer requesterUserId) throws AddressException {

        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressException("Address not found"));

        if (requesterUserId != null && (existingAddress.getUser() == null ||
                !requesterUserId.equals(existingAddress.getUser().getUserId())))
            throw new AddressException("You may only update your own address.");


        copyToEntity(address, existingAddress);
        // Save the updated address in the repository
        return toDTO(addressRepository.save(existingAddress));
    }

    @Override
    public void removeAddress(Integer addressId,Integer requesterUserId) throws AddressException {
        // Find the existing address by ID
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressException("Address not found"));

        if (requesterUserId != null && (existingAddress.getUser() == null ||
                !requesterUserId.equals(existingAddress.getUser().getUserId())))
            throw new AddressException("You may only remove your own address.");

        // Delete the address from the repository
        addressRepository.delete(existingAddress);
    }

    @Override
    public List<AddressResponseDTO> getAllUserAddress(Integer userId,Integer requesterUserId) throws AddressException {
        if (requesterUserId != null && !requesterUserId.equals(userId))
            throw new AddressException("You may only view your own addresses.");
        List<Address> userAddressList = addressRepository.getUserAddressList(userId);
        if (userAddressList.isEmpty()) {
            System.out.println("empty");
            throw new AddressException("User does not have any address");
        }
        return userAddressList.stream().map(this::toDTO).toList();
    }

    private void copyToEntity(AddressResponseDTO source, Address target) {
        target.setFlatNo(source.getFlatNo());
        target.setZipCode(source.getZipCode());
        target.setStreet(source.getStreet());
        target.setCity(source.getCity());
        target.setState(source.getState());
    }

    private AddressResponseDTO toDTO(Address address) {
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setAddressID(address.getAddressID());
        dto.setFlatNo(address.getFlatNo());
        dto.setZipCode(address.getZipCode());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        return dto;
    }


}
