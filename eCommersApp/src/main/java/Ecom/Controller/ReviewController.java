package Ecom.Controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Ecom.ModelDTO.ReviewDTO;
import Ecom.Service.ReviewService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ecom/product-reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    @PostMapping("/{productId}/{userId}")
    public ResponseEntity<ReviewDTO> addReviewToProduct(@PathVariable Integer productId, @PathVariable Integer userId,
                                                    @Valid @RequestBody ReviewDTO review) {
        ReviewDTO addedReview = reviewService.addReviewToProduct(productId, userId, review);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedReview);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReviewToProduct(@PathVariable Integer reviewId, @Valid @RequestBody ReviewDTO review) {
        ReviewDTO updatedReview = reviewService.updateReviewToProduct(reviewId, review);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<ReviewDTO>> getAllReviewOfProduct(@PathVariable Integer productId) {
        List<ReviewDTO> allReviews = reviewService.getAllReviewOfProduct(productId);
        return new ResponseEntity<>(allReviews, HttpStatus.OK);
    }
}
