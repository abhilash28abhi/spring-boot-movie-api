package com.spring.movie.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.movie.model.Rating;
import com.spring.movie.model.UserRating;


@RestController
@RequestMapping("/ratings")
public class RatingsInfoController {
	
	@GetMapping("/{movieId}")
	public List<Rating> getCatalog (@PathVariable String movieId) {
		return Arrays.asList(new Rating(movieId, 5));
	}
	
	@GetMapping("/users/{userId}")
	public UserRating getRating (@PathVariable String userId) {
		UserRating user = new UserRating();
		user.setUserRating(Arrays.asList(new Rating("1", 3),
				new Rating("5", 5)));
		return user;
	}
}
