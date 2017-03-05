package scrappers.xxi_cineplex;

import com.google.inject.Inject;
import models.City;
import models.Site;
import models.SiteCity;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import play.Logger;
import play.db.jpa.JPAApi;
import scrappers.WebDriver;

public class CitiesScrapper extends XXICineplexScrapper {

    @Inject
    public CitiesScrapper(WebDriver webDriver, JPAApi jpaApi) {
        super(webDriver, jpaApi);
    }

    public void scrap(Site site) {
        webDriver.navigate().to(site.getUrl());
        site.getCitiesEntryCssSelectors().forEach(selector -> webDriver.findElement(By.cssSelector(selector)).click());
        webDriver.findElements(By.cssSelector(site.getCityCssSelector())).forEach(element -> {
            City city = new City(StringUtils.capitalize(element.getText().toLowerCase()));
            SiteCity siteCity = new SiteCity(element.getText(), site, city);

            city.addSiteCity(siteCity);

            jpaApi.withTransaction(() -> jpaApi.em().persist(city));

            Logger.info("fetched " + city.getName());
        });
        webDriver.close();
    }

}
