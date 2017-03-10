package controllers;

import actors.xxi_cineplex.TheaterScrappingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.inject.Inject;
import models.Site;
import models.builders.SiteBuilder;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.duration.Duration;
import scrappers.xxi_cineplex.TheatersScrapper;

import java.util.concurrent.TimeUnit;

public class TheaterScrappingSchedulerController extends Controller {

    private static Class ACTOR_CLASS = TheaterScrappingActor.class;

    private ActorSystem actorSystem;

    private ActorRef theaterScrappingActor;

    private SiteBuilder siteBuilder;

    private TheatersScrapper theaterScrapper;

    @Inject
    public TheaterScrappingSchedulerController(ActorSystem actorSystem, SiteBuilder siteBuilder, TheatersScrapper theaterScrapper) {
        this.actorSystem = actorSystem;
        this.siteBuilder = siteBuilder;
        this.theaterScrapper = theaterScrapper;
    }

    public Result startActor() {
        try {
            int frequencyInDays = Integer.parseInt(request().getQueryString("frequency_in_days"));

            this.theaterScrappingActor = actorSystem.actorOf(Props.create(ACTOR_CLASS, theaterScrapper));

            Site site = this.siteBuilder.buildSiteFromJson("21cineplex");
            actorSystem.scheduler().schedule(Duration.create(0, TimeUnit.DAYS), Duration.create(frequencyInDays, TimeUnit.DAYS), this.theaterScrappingActor, site, actorSystem.dispatcher(), null);
            return ok("Actor started");
        } catch (Exception e) {
            e.printStackTrace();
            return internalServerError();
        }
    }

    public Result stopActor() {
        try {
            this.actorSystem.stop(this.theaterScrappingActor);
            return ok("Actor stopped");
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return internalServerError();
        }
    }

}
