package com.spring.movie.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.spring.movie.model.Rating;

@Service
public class MovieRatingService {
	
	@HystrixCommand(fallbackMethod = "getFallbackUserRating")
	public List<Rating> getUserRating() {
		//get all rated movie ids
		List<Rating> ratings = Arrays.asList(new Rating("100", 3),
				new Rating("500", 5));
		return ratings;
	}
	
	public List<Rating> getFallbackUserRating () {
		return Arrays.asList(new Rating("0",0));
	}
}
