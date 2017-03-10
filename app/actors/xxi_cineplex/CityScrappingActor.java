package actors.xxi_cineplex;

import akka.actor.UntypedActor;
import com.google.inject.Inject;
import models.Site;
import scrappers.xxi_cineplex.CitiesScrapper;

public class CityScrappingActor extends UntypedActor {

    private CitiesScrapper scrapper;

    @Inject
    public CityScrappingActor(CitiesScrapper scrapper) {
        this.scrapper = scrapper;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        Site site = (Site) message;
        this.scrapper.scrap(site);
    }

}
