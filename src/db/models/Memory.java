package db.models;

import app.Constants;
import models.Assistant;
import models.Review;

import java.util.ArrayList;
import java.util.List;

public class Memory {

	private List<Review> reviews;

	public Memory() {
		this.reviews = new ArrayList<>();
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public synchronized void addReview(Review review) {
		Assistant assistant = review.getAssistant();
		assistant.setFirstName(assistant.getFirstName().toUpperCase());
		assistant.setLastName(assistant.getLastName().toUpperCase());

		if (assistant.getFirstName().equals(Constants.FORBIDDEN_NAME)) {
			review.setRating(0);
		}

		reviews.add(review);
	}
}
