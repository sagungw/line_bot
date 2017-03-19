package scrappers.xxi_cineplex;

import com.google.inject.Inject;
import models.Movie;
import models.Site;
import models.Theater;
import models.TheaterMovie;
import org.openqa.selenium.WebElement;
import play.Logger;
import play.db.jpa.JPAApi;
import scrappers.WebDriver;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MoviesScrapper extends XXICineplexScrapper {

    @Inject
    public MoviesScrapper(WebDriver webDriver, JPAApi jpaApi) {
        super(webDriver, jpaApi);
    }

    public void scrap(Site site) {
        List<Theater> theaters = jpaApi.withTransaction(entityManager -> {
            Query query = entityManager.createQuery("SELECT t FROM Theater t");
            return query.getResultList();
        });

        theaters.forEach(theater -> {
            List<TheaterMovie> theaterMovies = jpaApi.withTransaction(entityManager -> {
                Query query = entityManager.createQuery("SELECT tm FROM theater_movie tm JOIN FETCH tm.showTimes WHERE tm.primaryKeys.theater.id = " + theater.getId() + " AND tm.isNowPlaying = true");
                return query.getResultList();
            });

            theaterMovies.forEach(tm -> {
                tm.getShowTimes().clear();
                tm.setNowPlaying(false);

                jpaApi.withTransaction(() -> jpaApi.em().merge(tm));
            });

            webDriver.navigate().to(theater.getUrl());
            List<String> movieLinks = webDriver.smartFindElements(site.getMovieLinkCssSelector()).stream().map(WebElement::getText).collect(Collectors.toList());
            List<String> showTimeStrings = webDriver.smartFindElements(site.getMovieShowTimeCssSelector()).stream().map(WebElement::getText).collect(Collectors.toList());

            for (int i = 0; i < movieLinks.size(); i++) {

                String rawTitle = movieLinks.get(i);
                String title = this.normalizeTitle(rawTitle);

                List<TheaterMovie> existingTheaterMovies =  jpaApi.withTransaction(entityManager -> {
                    Query q = entityManager.createQuery("SELECT DISTINCT tm FROM theater_movie tm LEFT JOIN FETCH tm.showTimes WHERE tm.primaryKeys.movie.title = :title AND tm.primaryKeys.theater.id = :theater_id");
                    q.setParameter("title", title);
                    q.setParameter("theater_id", theater.getId());
                    return q.getResultList();
                });

                List<String> showTimes = Arrays.asList(showTimeStrings.get(i).split("  "));
                showTimes.set(showTimes.size() - 1, showTimes.get(showTimes.size() - 1).replace(" ", ""));

                if(existingTheaterMovies.isEmpty()) {
                    List<Movie> existingMovies = jpaApi.withTransaction(entityManager -> {
                        Query query = entityManager.createQuery("SELECT m FROM Movie m WHERE m.title = :title");
                        query.setParameter("title", title);
                        return query.getResultList();
                    });

                    if (existingMovies.isEmpty()) {
                        Movie movie = new Movie(title);

                        TheaterMovie theaterMovie = new TheaterMovie(rawTitle, movie, theater, showTimes);
                        theaterMovie.setNowPlaying(true);
                        movie.getTheaterMovies().add(theaterMovie);

                        jpaApi.withTransaction(() -> jpaApi.em().persist(movie));

                        Logger.info("fetched " + theaterMovie.getTheater().getName() + ": " + theaterMovie.getMovie().getTitle());
                    } else {
                        Movie movie = existingMovies.get(0);

                        TheaterMovie theaterMovie = new TheaterMovie(rawTitle, movie, theater, showTimes);
                        theaterMovie.setNowPlaying(true);

                        jpaApi.withTransaction(() -> jpaApi.em().persist(theaterMovie));

                        Logger.info("fetched " + theaterMovie.getTheater().getName() + ": " + theaterMovie.getMovie().getTitle());
                    }
                } else {
                    TheaterMovie theaterMovie = existingTheaterMovies.get(0);
                    theaterMovie.setNowPlaying(true);
                    showTimes.forEach(theaterMovie::addShowTime);

                    jpaApi.withTransaction(() -> jpaApi.em().merge(theaterMovie));

                    Logger.info("skipped " + existingTheaterMovies.get(0).getTheater().getName() + ": " + existingTheaterMovies.get(0).getMovie().getTitle());
                }

            }
        });
    }

    private String normalizeTitle(String rawTitle) {
        if (rawTitle.contains("(IMAX 3D)")) {
            rawTitle = rawTitle.replace(" (IMAX 3D)", "");
        } else if (rawTitle.contains("(IMAX 2D)")) {
            rawTitle = rawTitle.replace(" (IMAX 2D)", "");
        } else if (rawTitle.contains("(3D)")) {
            rawTitle = rawTitle.replace(" (3D)", "");
        }
        return rawTitle;
    }

}
