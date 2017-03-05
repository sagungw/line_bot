package scrappers.xxi_cineplex;

import com.google.inject.Inject;
import models.Site;
import models.SiteCity;
import models.Theater;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
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
        Select citySelection = new Select(webDriver.findElement(By.cssSelector(site.getCitySelectFormSelector())));

        List<SiteCity> siteCities = jpaApi.withTransaction(entityManager -> {
            Query query = entityManager.createQuery("SELECT ");
            return query.getResultList();
        });

        siteCities.forEach(siteCity -> {
            citySelection.selectByValue(siteCity.getCity().getName().toUpperCase());
            webDriver.findElements(By.cssSelector(site.getTheaterCssSelector())).forEach(element -> {
                webDriver.withClickAndBack(element, () -> {
                    WebElement theaterNameText = webDriver.findElement(By.cssSelector(site.getTheaterNameCssSelector()));

                    Theater theater = new Theater(StringUtils.capitalize(theaterNameText.getText().toLowerCase()));
                    theater.setUrl(webDriver.getCurrentUrl());

                    jpaApi.withTransaction(() -> jpaApi.em().persist(theater));
                });
            });
        });
    }

}
