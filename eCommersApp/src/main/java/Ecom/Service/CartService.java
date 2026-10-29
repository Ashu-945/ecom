package Ecom.Service;

import java.util.List;

import Ecom.Exception.CartException;
import Ecom.ModelDTO.CartDTO;

public interface CartService {
	
	public CartDTO addProductToCart(Integer userId, Integer productId,Integer requesterUserId) throws CartException;
	
	public CartDTO increaseProductQuantity(Integer cartId,Integer productId,Integer requesterUserId) throws CartException;
	
	public CartDTO decreaseProductQuantity(Integer cartId,Integer productId,Integer requesterUserId) throws CartException;
	
	public void removeProductFromCart(Integer cartId,Integer productId,Integer requesterUserId) throws CartException;
	
	public void removeAllProductFromCart(Integer cartId,Integer requesterUserId) throws CartException;
	
	public CartDTO getAllCartProduct(Integer cartId,Integer requesterUserId)throws CartException;
	
	

}
