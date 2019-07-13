package com.spring.movie.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.spring.movie.model.Movie;
import com.spring.movie.model.MovieCatalogItem;
import com.spring.movie.model.Rating;
import com.spring.movie.model.UserRating;
import com.spring.movie.service.MovieCatalogService;
import com.spring.movie.service.MovieRatingService;


@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private WebClient.Builder builder;
	
	@Autowired
	private MovieRatingService ratingService;
	
	@Autowired
	private MovieCatalogService catalogService;
	
	@GetMapping("/{userId}")
	public List<MovieCatalogItem> getCatalog (@PathVariable String userId) {
		
		List<Rating> ratings = ratingService.getUserRating();
		
		//for each movie id, call movie info service and get the details
		return ratings.stream()
				.map(rating -> catalogService.getCatalogItem(rating))
				.collect(Collectors.toList());
		
		//put them all together
		//return Arrays.asList(new MovieCatalogItem("Interstellar","Test",5));
	}

	@GetMapping("/async/{userId}")
	public List<MovieCatalogItem> getCatalogAsync (@PathVariable String userId) {
		
		//get all rated movie ids from API call
		UserRating userRating = restTemplate.getForObject("http://localhost:8082/ratings/users/" + userId, UserRating.class);
		
		//for each movie id, call movie info service and get the details
		return userRating.getUserRating().stream().map(rating -> {
			//replacing rest template with web client
			Movie movie = builder.build()
			.get()
			.uri("http://localhost:8081/movies/" + rating.getMovieId())
			.retrieve()
			.bodyToMono(Movie.class)
			.block();//we are blocking execution here since we need to return a list of objects back
			return new MovieCatalogItem(movie.getName(), "Test", rating.getRating());
		}).collect(Collectors.toList());
	}
	
	@GetMapping("/eureka/{userId}")
	public List<MovieCatalogItem> getCatalogFromEureka (@PathVariable String userId) {
		
		//no need to hard code the service urls
		UserRating userRating = restTemplate.getForObject("http://ratings-service/ratings/users/" + userId, UserRating.class);
		
		//for each movie id, call movie info service and get the details
		return userRating.getUserRating().stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
			return new MovieCatalogItem(movie.getName(), "Test", rating.getRating());
		}).collect(Collectors.toList());
		
		//put them all together
		//return Arrays.asList(new MovieCatalogItem("Interstellar","Test",5));
	}
}
