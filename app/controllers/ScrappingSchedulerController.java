package controllers;

import actors.TestActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.Props;
import com.google.inject.Inject;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class ScrappingSchedulerController extends Controller {

    private ActorSystem actorSystem;

    private ActorRef testActor;

    private Cancellable schedule;

    @Inject
    public ScrappingSchedulerController(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    public Result startActor() {
        testActor = actorSystem.actorOf(Props.create(TestActor.class));
        this.schedule = actorSystem.scheduler().schedule(Duration.create(0, TimeUnit.MILLISECONDS), Duration.create(3, TimeUnit.SECONDS), testActor, "Jay", actorSystem.dispatcher(), null);
        return ok("Actor started");
    }

    public Result stopActor() {
        actorSystem.stop(testActor);
//        try {
//            this.schedule.cancel();
//        } catch(NullPointerException npe) {
//
//        }
        return ok("Actor stopped");
    }

}
