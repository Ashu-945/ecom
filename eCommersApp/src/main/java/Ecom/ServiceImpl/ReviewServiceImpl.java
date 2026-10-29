package Ecom.ServiceImpl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecom.Exception.ProductException;
import Ecom.Exception.ReviewException;
import Ecom.Exception.UserException;
import Ecom.Model.Product;
import Ecom.Model.Review;
import Ecom.Model.User;
import Ecom.Repository.ProductRepository;
import Ecom.Repository.ReviewRepository;
import Ecom.Repository.UserRepository;
import Ecom.Service.ReviewService;
import Ecom.ModelDTO.ReviewDTO;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ProductRepository productRepository;

	private final ReviewRepository reviewRepository;

	private final UserRepository userRepository;

	@Override
	public ReviewDTO addReviewToProduct(Integer productId, Integer userId, ReviewDTO reviewDTO) throws ReviewException {
		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ReviewException("Product Not Found"));

		User existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new ReviewException("User Not Found In Database"));

		Review review = new Review();
		review.setComment(reviewDTO.getComment());
		review.setRating(reviewDTO.getRating());
		review.setCreatedAt(java.time.LocalDateTime.now());
		existingUser.getReviews().add(review);
		review.setUser(existingUser);
		existingProduct.getReviews().add(review);
		review.setProduct(existingProduct);
		userRepository.save(existingUser);
		productRepository.save(existingProduct);

		return toDTO(reviewRepository.save(review));
	}

	@Override
	public ReviewDTO updateReviewToProduct(Integer reviewId, ReviewDTO reviewDTO) throws ReviewException {
		Review existingReview = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ReviewException("Review With Id "+reviewId+"Not Found In DataBase"));

		existingReview.setComment(reviewDTO.getComment());
		existingReview.setRating(reviewDTO.getRating());
		return toDTO(reviewRepository.save(existingReview));
	}

	@Override
	public void deleteReview(Integer reviewId) throws ReviewException {
		Review existingReview = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ReviewException("Review With Id "+reviewId+"Not Found In DataBase"));
		
		reviewRepository.delete(existingReview);

	}

	@Override
	public List<ReviewDTO> getAllReviewOfProduct(Integer productId) throws ReviewException {
		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ReviewException("Invalid Product id"));
		
		List<Review> allReviewsByProductId = reviewRepository.findAllReviewsByProductId(productId);
		if(allReviewsByProductId.isEmpty()) { 
			 throw new ReviewException ("No Rewiew Of Given Product is Available");
		}
		return allReviewsByProductId.stream().map(this::toDTO).toList();
	}

	private ReviewDTO toDTO(Review review) {
		ReviewDTO dto = new ReviewDTO();
		dto.setReviewId(review.getReviewId());
		dto.setRating(review.getRating());
		dto.setComment(review.getComment());
		dto.setCreatedAt(review.getCreatedAt());
		dto.setProductId(review.getProduct() == null ? null : review.getProduct().getProductId());
		dto.setUserId(review.getUser() == null ? null : review.getUser().getUserId());
		dto.setReviewerName(review.getUser() == null ? "Customer" : review.getUser().getFirstName());
		return dto;
	}

}
