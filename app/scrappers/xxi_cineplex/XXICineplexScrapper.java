package scrappers.xxi_cineplex;

import play.db.jpa.JPAApi;
import scrappers.WebDriver;
import scrappers.WebScrapper;

public abstract class XXICineplexScrapper implements WebScrapper {

    WebDriver webDriver;

    JPAApi jpaApi;

    public XXICineplexScrapper(WebDriver webDriver, JPAApi jpaApi) {
        this.webDriver = webDriver;
        this.jpaApi = jpaApi;
    }

}
