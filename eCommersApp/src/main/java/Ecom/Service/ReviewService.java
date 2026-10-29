package Ecom.Service;

import java.util.List;

import Ecom.Exception.ReviewException;
import Ecom.ModelDTO.ReviewDTO;

public interface ReviewService {
	
	 public ReviewDTO addReviewToProduct(Integer productId, Integer UserId,ReviewDTO review)
			 throws ReviewException;
	 
	 public ReviewDTO updateReviewToProduct(Integer reviewId,ReviewDTO review)
			 throws ReviewException;
	 
	 public void deleteReview(Integer reviewId) throws ReviewException;
	 
	 public List<ReviewDTO> getAllReviewOfProduct(Integer productId)throws ReviewException;

}
