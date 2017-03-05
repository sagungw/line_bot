package scrappers.xxi_cineplex;

import com.google.inject.Inject;
import models.City;
import models.Site;
import models.Theater;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import play.Logger;
import play.db.jpa.JPAApi;
import scrappers.WebDriver;

import javax.persistence.Query;
import java.util.List;

public class TheatersScrapper extends XXICineplexScrapper {

    @Inject
    public TheatersScrapper(WebDriver webDriver, JPAApi jpaApi) {
        super(webDriver, jpaApi);
    }

    public void scrap(Site site) {
        webDriver.navigate().to(site.getUrl());
        site.getTheatersEntryCssSelectors().forEach(selector -> webDriver.findElement(By.cssSelector(selector)).click());

        List<City> cities = jpaApi.withTransaction(entityManager -> {
            Query query = entityManager.createQuery("SELECT c FROM City c");
            return query.getResultList();
        });

        cities.forEach(city -> {
            Select citySelection = new Select(webDriver.findElement(By.cssSelector(site.getCitySelectFormSelector())));
            citySelection.selectByVisibleText(city.getDisplayName());

            String theaterCssSelector = site.replaceSelectorPlaceholderWithValue(site.getTheaterCssSelector(), city.getValue().toString());

            List<WebElement> movieLinks = webDriver.findElements(By.cssSelector(theaterCssSelector));

            for (int i = 0; i < movieLinks.size(); i++) {
                WebElement movieLink = webDriver.findElements(By.cssSelector(theaterCssSelector)).get(i);

                webDriver.withClickAndBack(movieLink, () -> {
                    WebElement theaterNameText = webDriver.findElement(By.cssSelector(site.getTheaterNameCssSelector()));
                    Theater theater = new Theater(theaterNameText.getText(), webDriver.getCurrentUrl());

                    List<Theater> existingTheaters = jpaApi.withTransaction(entityManager -> {
                        Query query = entityManager.createQuery("SELECT t FROM Theater t WHERE t.name = '" + theater.getName() + "'");
                        return query.getResultList();
                    });

                    if (existingTheaters.isEmpty()) {
                        jpaApi.withTransaction(() -> jpaApi.em().persist(theater));

                        Logger.info("fetched " + theater.getName());
                    }
                });
            }

        });

        webDriver.close();
    }

}
