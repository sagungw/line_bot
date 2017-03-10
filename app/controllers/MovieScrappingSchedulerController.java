package controllers;

import actors.xxi_cineplex.MovieScrappingActor;
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

public class MovieScrappingSchedulerController extends Controller {

    private static Class ACTOR_CLASS = MovieScrappingActor.class;

    private ActorSystem actorSystem;

    private ActorRef movieScrappingActor;

    private SiteBuilder siteBuilder;

    @Inject
    public MovieScrappingSchedulerController(ActorSystem actorSystem, SiteBuilder siteBuilder) {
        this.actorSystem = actorSystem;
        this.siteBuilder = siteBuilder;
    }

    public Result startActor() {
        try {
            int frequencyInDays = Integer.parseInt(request().getQueryString("frequency_in_days"));

            this.movieScrappingActor = actorSystem.actorOf(Props.create(ACTOR_CLASS));

            Site site = this.siteBuilder.buildSiteFromJson("21cineplex");
            actorSystem.scheduler().schedule(Duration.create(0, TimeUnit.DAYS), Duration.create(frequencyInDays, TimeUnit.DAYS), this.movieScrappingActor, site, actorSystem.dispatcher(), null);
            return ok("Actor started");
        } catch (Exception e) {
            e.printStackTrace();
            return internalServerError();
        }
    }

    public Result stopActor() {
        try {
            this.actorSystem.stop(this.movieScrappingActor);
            return ok("Actor stopped");
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return internalServerError();
        }
    }

}
