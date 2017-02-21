package services;

import com.google.inject.Inject;
import models.Movie;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import scrapper.WebScrapper;

import java.util.List;
import java.util.stream.Collectors;

public class MovieFetchingService {

    private WebScrapper webScrapper;

    @Inject
    public MovieFetchingService(WebScrapper webScrapper) {
        this.webScrapper = webScrapper;
    }

    public List<Movie> fetchNLatestMovies (int n) {
        webScrapper.navigate().to("http://www.21cineplex.com/");
        webScrapper.findElementByLinkText("NOW PLAYING").click();

        List<WebElement> moviesElements = webScrapper.findElementsByCssSelector("ul#mvlist > li");
        List<Movie> movies = moviesElements.stream().map(element -> {
            String title = element.findElement(By.cssSelector("h3 > a")).getText();
            String synopsis = element.findElement(By.cssSelector("p")).getText();
            return new Movie(title, synopsis);
        }).collect(Collectors.toList());

        webScrapper.close();
        return movies;
    }

}
