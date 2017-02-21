package controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class TestActorSchedulerController extends Controller {

    private ActorSystem actorSystem;

    private ActorRef testActor;

    private Cancellable schedule;

    @Inject
    public TestActorSchedulerController(ActorSystem actorSystem, @Named("test-actor") ActorRef testActor) {
        this.actorSystem = actorSystem;
        this.testActor = testActor;
    }

    public Result startActor() {
        this.schedule = actorSystem.scheduler().schedule(Duration.create(0, TimeUnit.MILLISECONDS), Duration.create(3, TimeUnit.SECONDS), testActor, "Jay", actorSystem.dispatcher(), null);
        return ok("Actor started");
    }

    public Result stopActor() {
//        actorSystem.stop(testActor);
        try {
            this.schedule.cancel();
        } catch(NullPointerException npe) {

        }
        return ok("Actor stopped");
    }

}
