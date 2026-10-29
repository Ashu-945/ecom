package Ecom.ServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import Ecom.Exception.ProductException;
import Ecom.Model.Product;
import Ecom.ModelDTO.ProductDTO;
import Ecom.Repository.ProductRepository;
import Ecom.Service.ProductService;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductDTO addProduct(ProductDTO product) throws ProductException {
        if (product == null) throw new ProductException("Product cannot be null");
        Product entity = new Product();
        copyToEntity(product, entity);
        return toDTO(productRepository.save(entity));
    }

    @Override
    public ProductDTO updateProduct(Integer productId, ProductDTO updatedProduct) throws ProductException {

        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new ProductException("Product with ID " + productId + " not found.");
        }
        Product existingProduct = product.get();

        copyToEntity(updatedProduct, existingProduct);
        return toDTO(productRepository.save(existingProduct));
    }

    @Override
    public List<ProductDTO> getProductByName(String name) throws ProductException {

        List<Product> existProductByName = productRepository.findByName(name);
        if (existProductByName.isEmpty()) {
            throw new ProductException("Product Not found with name " + name);
        }
        return existProductByName.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getAllProduct(String keyword, String sortDirection, String sortBy) throws ProductException {

        Set<String> allowedSortFields = Set.of("name", "price", "category", "productId");
        String safeSortBy = allowedSortFields.contains(sortBy) ? sortBy : "price";
        Sort sort = Sort.by("desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC, safeSortBy);

        List<Product> products;

        if (keyword != null) {

            products = productRepository.findAllByNameContainingIgnoreCase(keyword, sort);
        } else {
            products = productRepository.findAll(sort);
        }
        if (products.isEmpty()) {
            throw new ProductException("Product List Empty");
        }

        return products.stream().map(this::toDTO).collect(Collectors.toList());

    }

    @Override
    public List<ProductDTO> getProductByCategory(String category) throws ProductException {
        // Retrieve products by category from the database
        List<Product> allproductCategoryName = productRepository.getProductCategoryName(category);
        if (allproductCategoryName.isEmpty())
            throw new ProductException("Product with category Name " + category + " not found.");

        return allproductCategoryName.stream().map(this::toDTO).collect(Collectors.toList());
    }


    @Override
    public void removeProduct(Integer productId) throws ProductException {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product with ID " + productId + " not found."));

        productRepository.delete(existingProduct);
    }

    @Override
    public ProductDTO getSingleProduct(Integer productId) {

        Product single = productRepository.findById(productId).orElseThrow(() -> new ProductException("Product not found"));
        return toDTO(single);
    }

    private void copyToEntity(ProductDTO source, Product target) {
        target.setName(source.getName());
        target.setCategory(source.getCategory());
        target.setPrice(source.getPrice());
        target.setImageUrl(source.getImageUrl());
        target.setDescription(source.getDescription());
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setImageUrl(product.getImageUrl());
        dto.setAvailable(product.isAvailable());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        int reviewCount = product.getReviews() == null ? 0 : product.getReviews().size();
        dto.setReviewCount(reviewCount);
        dto.setAverageRating(reviewCount == 0 ? null : product.getReviews().stream()
                .mapToInt(review -> review.getRating() == null ? 0 : review.getRating())
                .average().orElse(0));
        return dto;
    }


}
