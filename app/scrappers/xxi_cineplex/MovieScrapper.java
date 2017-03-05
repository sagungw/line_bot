package scrappers.xxi_cineplex;

import models.Movie;
import models.Site;
import models.Theater;
import models.TheaterMovie;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import play.db.jpa.JPAApi;
import scrappers.WebDriver;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieScrapper extends XXICineplexScrapper {

    public MovieScrapper(WebDriver webDriver, JPAApi jpaApi) {
        super(webDriver, jpaApi);
    }

    public void scrap(Site site) {
        List<Theater> theaters = jpaApi.withTransaction(entityManager -> {
            Query query = entityManager.createNativeQuery("select * from theater");
            return query.getResultList();
        });

        theaters.forEach(theater -> {
            webDriver.navigate().to(theater.getUrl());
            WebElement movieTable = webDriver.findElements(By.cssSelector(site.getMovieListCssSelector())).get(0);

            List<Movie> movies = new ArrayList<>();
            List<TheaterMovie> foundMovies = new ArrayList<>();

            movieTable.findElements(By.cssSelector(site.getMovieRowCssSelector())).forEach(element -> {
                WebElement movieLinkCssSelector = element.findElement(By.cssSelector(site.getMovieLinkCssSelector()));
                WebElement showTimeCssSelector = element.findElement(By.cssSelector(site.getMovieShowTimeCssSelector()));

                String movieTitle = movieLinkCssSelector.getText();

                String showTime = showTimeCssSelector.getText();
                showTime = showTime.replace("&nbsp", " ");
                List<String> showTimes = Arrays.asList(showTime.split(" "));

                TheaterMovie theaterMovie = new TheaterMovie(movieTitle);
                theaterMovie.setTheater(theater);
                theaterMovie.setShowTime(showTimes);

                webDriver.withClickAndBack(movieLinkCssSelector, () -> {
                    String synopsis = webDriver.findElement(By.cssSelector(site.getMovieSynopsisCssSelector())).getText();

                    Movie movie = new Movie(movieTitle, synopsis);
                    movie.getTheaterMovies().add(theaterMovie);

                    jpaApi.withTransaction(() -> jpaApi.em().persist(movie));
                });
            });
        });
    }

}
