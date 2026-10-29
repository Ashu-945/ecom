package Ecom.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import Ecom.Exception.OrdersException;
import Ecom.ModelDTO.OrdersDTO;

@Service 
public interface OrdersService {
	
	public OrdersDTO placeOrder(Integer orderId,Integer requesterUserId) throws OrdersException;
	
	public OrdersDTO updateOrders(Integer ordersid,OrdersDTO orderDTo)throws OrdersException;
	
	public OrdersDTO getOrdersDetails(Integer orderid,Integer requesterUserId)throws OrdersException;
	
	public List<OrdersDTO> getAllUserOrder(Integer userId,Integer requesterUserId)throws OrdersException;
	
	public List<OrdersDTO> viewAllOrders()throws OrdersException;
	
	public List<OrdersDTO> viewAllOrderByDate(LocalDate date)throws OrdersException;
	
	public void deleteOrders(Integer userId,Integer Orderid,Integer requesterUserId)throws OrdersException;

}
