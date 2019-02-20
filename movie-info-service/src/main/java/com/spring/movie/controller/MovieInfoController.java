package com.spring.movie.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.movie.model.Movie;


@RestController
@RequestMapping("/movies")
public class MovieInfoController {
	
	@GetMapping("/{movieId}")
	public Movie getCatalog (@PathVariable String movieId) {
		return new Movie(movieId,"Interstellar");
	}
}
