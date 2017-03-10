package actors.xxi_cineplex;

import akka.actor.UntypedActor;
import com.google.inject.Inject;
import models.Site;
import scrappers.xxi_cineplex.MoviesScrapper;

public class MovieScrappingActor extends UntypedActor {

    private MoviesScrapper scrapper;

    @Inject
    public MovieScrappingActor(MoviesScrapper scrapper) {
        this.scrapper = scrapper;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        Site site = (Site) message;
        this.scrapper.scrap(site);
    }

}
