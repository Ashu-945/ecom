package Ecom.ServiceImpl;

import org.springframework.stereotype.Service;

import Ecom.Exception.ShippingException;
import Ecom.Model.Orders;
import Ecom.Model.Shipper;
import Ecom.Model.ShippingDetails;
import Ecom.ModelDTO.ShippingDTO;
import Ecom.Repository.OrderRepository;
import Ecom.Repository.ShipperRepository;
import Ecom.Repository.ShippingRepository;
import Ecom.Service.ShippingService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

	private final ShippingRepository shippingRepository;

	private final OrderRepository orderRepository;

	private final ShipperRepository shipperRepository;

	@Override
	public ShippingDTO setShippingDetails(Integer orderId, Integer shipperId, ShippingDTO shippingDTO)
	        throws ShippingException {
	    if (shippingDTO == null)
	        throw new ShippingException("ShippingDetails cannot be null");

	    Orders existingOrder = orderRepository.findById(orderId)
	            .orElseThrow(() -> new ShippingException("Order not found"));

	    Shipper existingShipper = shipperRepository.findById(shipperId)
	            .orElseThrow(() -> new ShippingException("Shipper not found"));

	    ShippingDetails shippingDetails = new ShippingDetails();
	    shippingDetails.setAddress(shippingDTO.getAddress());
	    shippingDetails.setCity(shippingDTO.getCity());
	    shippingDetails.setState(shippingDTO.getState());
	    shippingDetails.setPostalCode(shippingDTO.getPostalCode());
	    shippingDetails.setCountry(shippingDTO.getCountry());
	    shippingDetails.setOrders(existingOrder);
	    shippingDetails.setShipper(existingShipper);
	    shippingDetails = shippingRepository.save(shippingDetails);

	    // Now update the Orders entity with the saved ShippingDetails
	    existingOrder.setShippingDetails(shippingDetails);
	    orderRepository.save(existingOrder);

	    return toDTO(shippingDetails);
	}


	@Override
	public ShippingDTO updateShippingAddress(Integer shippingId, ShippingDTO shippingDTO) throws ShippingException {
		ShippingDetails existing = shippingRepository.findById(shippingId)
				.orElseThrow(() -> new ShippingException("Shipping detail not found"));

		existing.setState(shippingDTO.getState());
		existing.setAddress(shippingDTO.getAddress());
		existing.setCity(shippingDTO.getCity());
		existing.setPostalCode(shippingDTO.getPostalCode());
		return toDTO(shippingRepository.save(existing));
	}

	@Override
	public void deleteShippingDetails(Integer shippingId) throws ShippingException {
		ShippingDetails existing = shippingRepository.findById(shippingId)
				.orElseThrow(() -> new ShippingException("Shipping detail not found"));

		shippingRepository.delete(existing);

	}

	private ShippingDTO toDTO(ShippingDetails shipping) {
		ShippingDTO dto = new ShippingDTO();
		dto.setShippingId(shipping.getShippingId());
		dto.setAddress(shipping.getAddress());
		dto.setCity(shipping.getCity());
		dto.setState(shipping.getState());
		dto.setPostalCode(shipping.getPostalCode());
		dto.setCountry(shipping.getCountry());
		dto.setOrderId(shipping.getOrders() == null ? null : shipping.getOrders().getOrderId());
		dto.setShipperId(shipping.getShipper() == null ? null : shipping.getShipper().getShipperId());
		return dto;
	}

}
