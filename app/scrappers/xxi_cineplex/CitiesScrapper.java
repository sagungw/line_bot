package scrappers.xxi_cineplex;

import com.google.inject.Inject;
import models.City;
import models.Site;
import org.openqa.selenium.By;
import play.Logger;
import play.db.jpa.JPAApi;
import scrappers.WebDriver;

import javax.persistence.Query;
import java.util.List;

public class CitiesScrapper extends XXICineplexScrapper {

    @Inject
    public CitiesScrapper(WebDriver webDriver, JPAApi jpaApi) {
        super(webDriver, jpaApi);
    }

    public void scrap(Site site) {
        webDriver.navigate().to(site.getUrl());
        site.getCitiesEntryCssSelectors().forEach(selector -> webDriver.findElement(By.cssSelector(selector)).click());

        webDriver.findElements(By.cssSelector(site.getCityCssSelector())).forEach(element -> {
            City city = new City(Integer.parseInt(element.getAttribute("value")), element.getText());

            List<City> existingCities = jpaApi.withTransaction(entityManager -> {
                Query query = entityManager.createQuery("SELECT c FROM City c WHERE c.name = '" + city.getName() + "'");
                return query.getResultList();
            });

            if (existingCities.isEmpty()) {
                jpaApi.withTransaction(() -> jpaApi.em().persist(city));
                Logger.info("fetched " + city.getName());
            } else {
                Logger.info("skiped " + city.getName());
            }

        });

    }

}
