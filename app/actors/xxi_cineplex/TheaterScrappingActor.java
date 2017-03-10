package actors.xxi_cineplex;

import akka.actor.UntypedActor;
import com.google.inject.Inject;
import models.Site;
import scrappers.xxi_cineplex.TheatersScrapper;

public class TheaterScrappingActor extends UntypedActor {

    private TheatersScrapper scrapper;

    @Inject
    public TheaterScrappingActor(TheatersScrapper scrapper) {
        this.scrapper = scrapper;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        Site site = (Site) message;
        this.scrapper.scrap(site);
    }

}
