package Ecom.Service;

import java.util.List;

import Ecom.Exception.ShipperException;
import Ecom.ModelDTO.ShipperDTO;

public interface ShipperService {

	public void deleteShipperById(Integer id) throws ShipperException;

	public ShipperDTO saveShipper(ShipperDTO shipper) throws ShipperException;

	public ShipperDTO getShipperById(Integer id) throws ShipperException;

	public List<ShipperDTO> getAllShippers() throws ShipperException;

}
