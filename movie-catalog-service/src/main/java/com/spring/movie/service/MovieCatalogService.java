package com.spring.movie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.spring.movie.model.Movie;
import com.spring.movie.model.MovieCatalogItem;
import com.spring.movie.model.Rating;

@Service
public class MovieCatalogService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "getFallbackCatalogItem",
			commandProperties = {
					@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000"),
					@HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="5"),
					@HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="50"),
					@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="5000")
			})
	public MovieCatalogItem getCatalogItem(Rating rating) {
		Movie movie = restTemplate.getForObject("http://localhost:8084/movies/" + rating.getMovieId(), Movie.class);
		return new MovieCatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
	}
	
	
	public MovieCatalogItem getFallbackCatalogItem (Rating rating) {
		return new MovieCatalogItem("No Movie", "", 0);
	}
}
