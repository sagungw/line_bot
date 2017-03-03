package scrappers.xxi_cineplex;

import models.City;
import models.Site;
import models.SiteCity;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import play.db.jpa.JPAApi;
import scrappers.WebDriver;

public class CitiesScrapper extends XXICineplexScrapper {

    public CitiesScrapper(WebDriver webDriver, JPAApi jpaApi) {
        this.webDriver = webDriver;
        this.jpaApi = jpaApi;
    }

    public void scrap(Site site) {
        site.getCitiesEntryCssSelectors().forEach(selector ->  webDriver.findElement(By.cssSelector(selector)).click());
        webDriver.findElements(By.cssSelector(site.getCityCssSelector())).forEach(element -> {
            SiteCity siteCity = new SiteCity(element.getText());
            City city = new City(StringUtils.capitalize(element.getText().toLowerCase()));

            siteCity.setSite(site);
            siteCity.setCity(city);

            jpaApi.em().persist(siteCity);
        });
    }

}
