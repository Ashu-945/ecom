package Ecom.ServiceImpl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import Ecom.Exception.CartException;
import Ecom.Exception.ProductException;
import Ecom.Exception.UserException;
import Ecom.Model.Cart;
import Ecom.Model.CartItem;
import Ecom.Model.Product;
import Ecom.Model.User;
import Ecom.ModelDTO.CartDTO;
import Ecom.ModelDTO.CartItemDTO;
import Ecom.ModelDTO.ProductDTO;
import Ecom.Repository.CartItemRepository;
import Ecom.Repository.CartRepository;
import Ecom.Repository.ProductRepository;
import Ecom.Repository.UserRepository;
import Ecom.Service.CartService;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final ProductRepository productRepository;

	private final CartRepository cartRepository;

	private final CartItemRepository cartItemRepository;

	private final UserRepository userRepository;

	public CartDTO addProductToCart(Integer userId, Integer productId,Integer requesterUserId) throws CartException {
		if (requesterUserId != null && !requesterUserId.equals(userId))
			throw new CartException("You may only change your own cart.");

		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ProductException("Product not available in Stock..."));
		if (!existingProduct.isAvailable()) {
			throw new ProductException("Product is currently unavailable.");
		}

		User existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new UserException("User Not Found In Database"));

		if (existingUser.getCart() != null) {
			System.out.println("Cart is alredy alloted...");
			Cart userCart = existingUser.getCart();

			List<CartItem> cartItems = userCart.getCartItems();
			if (cartItems != null) {
				System.out.println("cart item imside loop...");
				for (int i = 0; i < cartItems.size(); i++) {
					System.out.println("inside loop");
					if (cartItems.get(i).getProduct().getProductId() == productId&&
							cartItems.get(i).getCart().getCartId()==userCart.getCartId()) {
						throw new CartException("Product Already in the Cart,Please Increase the Quantity");
					}
				}
			}
			CartItem cartItem = new CartItem();
			cartItem.setProduct(existingProduct);
			cartItem.setQuantity(1);
			cartItem.setCart(userCart);
			userCart.getCartItems().add(cartItem);

			userCart.setTotalAmount(calculateCartTotal(cartItems));
			cartRepository.save(userCart);
			return toDTO(userCart);

		} else {

			Cart newCart = new Cart();
			newCart.setUser(existingUser);
			existingUser.setCart(newCart);


			CartItem cartItem = new CartItem();

			cartItem.setProduct(existingProduct);
			cartItem.setQuantity(1);

			newCart.getCartItems().add(cartItem);
			cartItem.setCart(newCart);

			newCart.setTotalAmount(calculateCartTotal(newCart.getCartItems()));
			userRepository.save(existingUser);

			return toDTO(existingUser.getCart());
		}
	}

	private double calculateCartTotal(List<CartItem> cartItems) {
		double total = 0.0;
		for (CartItem item : cartItems) {
			double itemPrice = item.getProduct().getPrice();
			int itemQuantity = (item.getQuantity());
			total +=itemPrice * itemQuantity;
		}
		return total;
	}

	@Override
	public CartDTO increaseProductQuantity(Integer cartId, Integer productId,Integer requesterUserId) throws CartException {
		Cart userCart = cartRepository.findById(cartId)
				.orElseThrow(() -> new CartException("Cart Not Found"));
		assertCartOwner(userCart, requesterUserId);
		List<CartItem> cartItems = userCart.getCartItems();

		CartItem cartItemToUpdate = cartItems.stream()
				.filter(item -> item.getProduct().getProductId().equals(productId)
						&&item.getCart().getCartId().equals(userCart.getCartId())).findFirst()
				.orElseThrow(() -> new CartException("Cart Item Not Found"));

		int quantity = cartItemToUpdate.getQuantity();
		cartItemToUpdate.setQuantity(quantity + 1);
		userCart.setCartItems(cartItems);
		userCart.setTotalAmount(calculateCartTotal(cartItems));
		cartRepository.save(userCart);

		return toDTO(userCart);
	}
	@Override
	public CartDTO decreaseProductQuantity(Integer cartId, Integer productId,Integer requesterUserId) throws CartException {
		Cart userCart = cartRepository.findById(cartId)
				.orElseThrow(() -> new CartException("Cart Not Found"));
		assertCartOwner(userCart, requesterUserId);
		List<CartItem> cartItems = userCart.getCartItems();
		CartItem cartItemToUpdate = cartItems.stream()
				.filter(item -> item.getProduct().getProductId().equals(productId)
						&&item.getCart().getCartId().equals(userCart.getCartId())).findFirst()
				.orElseThrow(() -> new CartException("Cart Item Not Found"));

		int quantity = cartItemToUpdate.getQuantity();
		if(quantity==1){
			throw new CartException("Product can not be Further decresse...");
		}
		if (quantity > 1) {
			cartItemToUpdate.setQuantity(quantity - 1);


			userCart.setCartItems(cartItems);
			userCart.setTotalAmount(calculateCartTotal(cartItems));
			cartRepository.save(userCart);
		} else {
			cartItems.remove(cartItemToUpdate);
			userCart.setCartItems(cartItems);
			userCart.setTotalAmount(calculateCartTotal(cartItems));
			cartRepository.save(userCart);
		}
		return toDTO(userCart);
	}
	@Override
	public void removeProductFromCart(Integer cartId, Integer productId,Integer requesterUserId) throws CartException {
		Cart existingCart = cartRepository.findById(cartId).orElseThrow(() -> new CartException("Cart Not Found"));
		assertCartOwner(existingCart, requesterUserId);

		List<CartItem> list = existingCart.getCartItems();
		list.removeIf(item -> item.getProduct().getProductId().equals(productId));
		existingCart.setCartItems(list);
		cartItemRepository.removeProductFromCart(cartId, productId);
		existingCart.setTotalAmount(calculateCartTotal(list));
		cartRepository.save(existingCart);

	}

	@Override
	public CartDTO getAllCartProduct(Integer cartId,Integer requesterUserId) throws CartException {
		Cart existingCart = cartRepository.findById(cartId).orElseThrow(() -> new CartException("Cart Not Found"));
		assertCartOwner(existingCart, requesterUserId);

		List<CartItem> cartItems = existingCart.getCartItems();
		return toDTO(existingCart);
	}

	@Override
	public void removeAllProductFromCart(Integer cartId,Integer requesterUserId) throws CartException {
		Cart existingCart = cartRepository.findById(cartId).orElseThrow(() -> new CartException("Cart Not Found"));
		assertCartOwner(existingCart, requesterUserId);

		cartItemRepository.removeAllProductFromCart(cartId);

		existingCart.setTotalAmount(0.0);
		cartRepository.save(existingCart);
	}

	private void assertCartOwner(Cart cart, Integer requesterUserId) {
		if (requesterUserId != null && (cart.getUser() == null || !requesterUserId.equals(cart.getUser().getUserId())))
			throw new CartException("You may only change your own cart.");
	}

	private CartDTO toDTO(Cart cart) {
		CartDTO dto = new CartDTO();
		dto.setCartId(cart.getCartId());
		dto.setTotalAmount(cart.getTotalAmount());
		dto.setCartItems(cart.getCartItems().stream().map(item -> {
			CartItemDTO itemDTO = new CartItemDTO();
			itemDTO.setCartItemId(item.getCartItemId());
			itemDTO.setQuantity(item.getQuantity());
			Product product = item.getProduct();
			ProductDTO productDTO = new ProductDTO();
			productDTO.setProductId(product.getProductId());
			productDTO.setName(product.getName());
			productDTO.setImageUrl(product.getImageUrl());
			productDTO.setDescription(product.getDescription());
			productDTO.setPrice(product.getPrice());
			productDTO.setCategory(product.getCategory());
			productDTO.setAvailable(product.isAvailable());
			itemDTO.setProduct(productDTO);
			return itemDTO;
		}).toList());
		return dto;
	}
}
