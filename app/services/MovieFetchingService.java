package services;

import com.google.inject.Inject;
import models.Movie;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import scrappers.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

public class MovieFetchingService {

    private WebDriver webDriver;

    @Inject
    public MovieFetchingService(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<Movie> fetchNLatestMovies (int n) {
        webDriver.navigate().to("http://www.21cineplex.com/");
        webDriver.findElementByLinkText("NOW PLAYING").click();

        List<WebElement> moviesElements = webDriver.findElementsByCssSelector("ul#mvlist > li");
        List<Movie> movies = moviesElements.stream().map(element -> {
            String title = element.findElement(By.cssSelector("h3 > a")).getText();
            String synopsis = element.findElement(By.cssSelector("p")).getText();
            return new Movie(title, synopsis);
        }).collect(Collectors.toList());

        webDriver.close();
        return movies;
    }

}
