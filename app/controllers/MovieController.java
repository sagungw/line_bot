package controllers;

import com.google.inject.Inject;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.MovieFetchingService;

public class MovieController extends Controller {

    private MovieFetchingService movieFetcher;

    @Inject
    public MovieController(MovieFetchingService movieFetchingService) {
        this.movieFetcher = movieFetchingService;
    }

    public Result fetchMovies() {
//        List<Movie> latestMovies = movieFetcher.fetchNLatestMovies(0);
//        return ok(Json.toJson(latestMovies));
        return ok("Movies fetched");
    }

}
