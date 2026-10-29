package Ecom.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecom.Exception.ShipperException;
import Ecom.Model.Shipper;
import Ecom.Repository.ShipperRepository;
import Ecom.Repository.ShippingRepository;
import Ecom.Service.ShipperService;
import Ecom.ModelDTO.ShipperDTO;

@Service
public class ShipperServiceImpl implements ShipperService {

	private final ShipperRepository shipperRepository;
	
	@Autowired
	private ShippingRepository shippingRepository;

	@Autowired
	public ShipperServiceImpl(ShipperRepository shipperRepository) {
		this.shipperRepository = shipperRepository;
	}

	@Override
	public ShipperDTO saveShipper(ShipperDTO shipper) throws ShipperException {
		if (shipper == null) {
			throw new ShipperException("Shipper object cannot be null.");
		}
		Shipper entity = new Shipper();
		entity.setName(shipper.getName());
		entity.setPhoneNumber(shipper.getPhoneNumber());
		return toDTO(shipperRepository.save(entity));
	}

	@Override
	public void deleteShipperById(Integer id) throws ShipperException {
		Optional<Shipper> optionalShipper = shipperRepository.findById(id);
		if (optionalShipper.isPresent()) {
			shipperRepository.deleteById(id);
		} else {
			throw new ShipperException("Shipper with ID " + id + " not found.");
		}
	}

	@Override
	public ShipperDTO getShipperById(Integer id) throws ShipperException {
		return shipperRepository.findById(id).map(this::toDTO)
				.orElseThrow(() -> new ShipperException("Shipper with ID " + id + " not found."));
	}

	@Override
	public List<ShipperDTO> getAllShippers() throws ShipperException {
		List<Shipper> shippers = shipperRepository.findAll();
		if (shippers.isEmpty()) {
			throw new ShipperException("No shippers found.");
		}
		return shippers.stream().map(this::toDTO).toList();
	}

	private ShipperDTO toDTO(Shipper shipper) {
		ShipperDTO dto = new ShipperDTO();
		dto.setShipperId(shipper.getShipperId());
		dto.setName(shipper.getName());
		dto.setPhoneNumber(shipper.getPhoneNumber());
		return dto;
	}
}
