package controllers;

import actors.xxi_cineplex.CityScrappingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.inject.Inject;
import models.Site;
import models.builders.SiteBuilder;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CityScrappingSchedulerController extends Controller {

    private static Class ACTOR_CLASS = CityScrappingActor.class;

    private ActorSystem actorSystem;

    private ActorRef cityScrappingActor;

    private SiteBuilder siteBuilder;

    @Inject
    public CityScrappingSchedulerController(ActorSystem actorSystem, SiteBuilder siteBuilder) {
        this.actorSystem = actorSystem;
        this.siteBuilder = siteBuilder;
    }

    public Result startActor() {
        try {
            int frequencyInDays = Integer.parseInt(request().getQueryString("frequency_in_days"));

            this.cityScrappingActor = actorSystem.actorOf(Props.create(ACTOR_CLASS));

            Site site = this.siteBuilder.buildSiteFromJson("21cineplex");
            actorSystem.scheduler().schedule(Duration.create(0, TimeUnit.DAYS), Duration.create(frequencyInDays, TimeUnit.DAYS), this.cityScrappingActor, site, actorSystem.dispatcher(), null);
            return ok("Actor started");
        } catch (Exception e) {
            e.printStackTrace();
            return internalServerError();
        }
    }

    public Result stopActor() {
        try {
            this.actorSystem.stop(this.cityScrappingActor);
            return ok("Actor stopped");
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return internalServerError();
        }
    }

}
