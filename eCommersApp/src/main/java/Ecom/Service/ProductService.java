package Ecom.Service;

import java.util.List;

import Ecom.Exception.ProductException;
import Ecom.ModelDTO.ProductDTO;

public interface ProductService {
	
	public ProductDTO addProduct(ProductDTO products)throws ProductException;
	
	public ProductDTO updateProduct(Integer productId,ProductDTO product)throws ProductException;
	
	public List<ProductDTO> getProductByName(String name)throws ProductException;
	
	public List<ProductDTO> getAllProduct(String keyword, String sortDirection, String sortBy)throws ProductException;
	
	public List<ProductDTO> getProductByCategory(String catagory) throws ProductException;
	
	public void removeProduct(Integer productId)throws ProductException;

	public ProductDTO getSingleProduct(Integer productId);
}

