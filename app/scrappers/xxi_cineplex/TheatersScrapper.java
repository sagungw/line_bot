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

        List<WebElement> theaterLinks = webDriver.findElements(By.cssSelector(site.getTheaterCssSelector()));

        for (int i = 0; i < theaterLinks.size(); i++) {
            WebElement theaterLink = theaterLinks.get(i);

            WebElement span = theaterLink.findElement(By.cssSelector("span"));
            webDriver.executeScript("arguments[0].parentNode.removeChild(arguments[0])", span);

            String theaterName = theaterLink.getAttribute("textContent");

            List<Theater> existingTheaters = jpaApi.withTransaction(entityManager -> {
                Query query = entityManager.createQuery("SELECT t FROM Theater t WHERE t.name = '" + theaterName + "'");
                return query.getResultList();
            });

            if (existingTheaters.isEmpty()) {
                Theater theater = new Theater(theaterName, theaterLink.getAttribute("href"));

                jpaApi.withTransaction(() -> jpaApi.em().persist(theater));
                Logger.info("fetched " + theaterName);
            } else {
                Logger.info("skipped " + theaterName);
            }

        }

    }

}
