package Ecom.Service;

import Ecom.Exception.ShippingException;
import Ecom.ModelDTO.ShippingDTO;

public interface ShippingService {
	
  public ShippingDTO setShippingDetails(Integer orderId,Integer shipperId,ShippingDTO shippingDetails) throws ShippingException;
  
  public ShippingDTO updateShippingAddress(Integer shippingId,ShippingDTO shippingDTO)throws ShippingException;
  
  public void deleteShippingDetails(Integer shippingId)throws ShippingException;
  
  
}
