package Ecom.Controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Ecom.ModelDTO.ProductDTO;
import Ecom.Service.ProductService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ecom/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO product) {
        ProductDTO newProduct = productService.addProduct(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductDTO> updateProduct( @PathVariable Integer productId, @Valid @RequestBody ProductDTO updatedProduct) {
        ProductDTO updatedProductResult = productService.updateProduct(productId, updatedProduct);
        return new ResponseEntity<>(updatedProductResult, HttpStatus.OK);
    }

    @GetMapping("/product-By-name/{name}")
    public ResponseEntity<List<ProductDTO>> getProductByName(@PathVariable String name) {
        List<ProductDTO> products = productService.getProductByName(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> search(
       @RequestParam (required = false)  String keyword,
       @RequestParam(required = false,defaultValue = "asc") String sort,
       @RequestParam(required = false,defaultValue = "price") String sortBy

    ) {
        List<ProductDTO> products = productService.getAllProduct(keyword, sort, sortBy);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }



    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDTO>> getProductByCategory(@PathVariable String category) {
        List<ProductDTO> products = productService.getProductByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getSingleProduct(@PathVariable Integer productId) {
        ProductDTO singleProsuct = productService.getSingleProduct(productId);
        return new ResponseEntity<>(singleProsuct, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeProduct(@PathVariable Integer productId) {
        productService.removeProduct(productId);
        return new ResponseEntity<>("Product removed successfully.", HttpStatus.OK);
    }
}
