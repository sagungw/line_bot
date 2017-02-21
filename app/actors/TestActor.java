package actors;

import akka.actor.UntypedActor;
import play.Logger;

public class TestActor extends UntypedActor{

    private int counter = 0;

    @Override
    public void onReceive(Object message) throws Throwable {
        String msg = (String) message;
        Logger.info(msg + " " + counter++);
    }
}
