package scrappers.xxi_cineplex;

import com.google.inject.Inject;
import models.Movie;
import models.Site;
import models.Theater;
import models.TheaterMovie;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import play.Logger;
import play.db.jpa.JPAApi;
import scrappers.WebDriver;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        theaters.stream().forEach(theater -> {

            List<TheaterMovie> theaterMovies = jpaApi.withTransaction(entityManager -> {
                Query query = entityManager.createQuery("SELECT tm FROM theater_movie tm WHERE tm.primaryKeys.theater = " + theater.getId() + " AND tm.isNowPlaying = true");
                return query.getResultList();
            });

            theaterMovies.forEach(tm -> {
                tm.setNowPlaying(false);
                jpaApi.withTransaction(() -> jpaApi.em().merge(tm));
            });

            webDriver.navigate().to(theater.getUrl());
            List<WebElement> movieRows = webDriver.smartFindElements(site.getMovieRowCssSelector());

            for (int i = 0; i < movieRows.size(); i++) {
                WebElement movieRow = webDriver.smartFindElements(site.getMovieRowCssSelector()).get(i);

                String showTime = movieRow.findElement(By.cssSelector(site.getMovieShowTimeCssSelector())).getText();
                List<String> showTimes = Arrays.asList(showTime.split("  "));

                WebElement movieLinkCssSelector = movieRow.findElement(By.cssSelector(site.getMovieLinkCssSelector()));
                webDriver.withClickAndBack(movieLinkCssSelector, () -> {
                    String synopsis = webDriver.findElement(By.cssSelector(site.getMovieSynopsisCssSelector())).getText();
                    String title = webDriver.findElement(By.cssSelector(site.getMovieNameCssSelector())).getText();

                    List<Movie> existingMovies = jpaApi.withTransaction(entityManager -> {
                        Query query = entityManager.createQuery("SELECT m FROM Movie m WHERE m.title = :title");
                        query.setParameter("title", this.normalizeTitle(title));
                        return query.getResultList();
                    });

                    if (existingMovies.isEmpty()) {
                        Movie movie = new Movie(this.normalizeTitle(title), synopsis);

                        TheaterMovie theaterMovie = new TheaterMovie(title, movie, theater, showTimes);
                        theaterMovie.setNowPlaying(true);

                        movie.getTheaterMovies().add(theaterMovie);

                        jpaApi.withTransaction(() -> jpaApi.em().persist(movie));

                        Logger.info("fetched " + theaterMovie.getTheater().getName() + ": " + theaterMovie.getMovie().getTitle());
                    } else {
                        Movie movie = existingMovies.get(0);

                        List<TheaterMovie> existingTms = jpaApi.withTransaction(entityManager -> {
                            Query query = entityManager.createQuery("SELECT tm FROM theater_movie tm WHERE tm.primaryKeys.movie.id = " + movie.getId() + " AND tm.primaryKeys.theater.id = " + theater.getId());
                            return query.getResultList();
                        });

                        if(existingTms.isEmpty()) {
                            TheaterMovie theaterMovie = new TheaterMovie(title, movie, theater, showTimes);
                            theaterMovie.setNowPlaying(true);

                            jpaApi.withTransaction(() -> jpaApi.em().persist(theaterMovie));

                            Logger.info("fetched " + theaterMovie.getTheater().getName() + ": " + theaterMovie.getMovie().getTitle());
                        } else {
                            TheaterMovie theaterMovie = existingTms.get(0);
                            theaterMovie.setNowPlaying(true);

                            jpaApi.withTransaction(() -> jpaApi.em().merge(theaterMovie));
                            Logger.info("skipped " + existingTms.get(0).getTheater().getName() + ": " + existingTms.get(0).getMovie().getTitle());
                        }
                    }

                });
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
