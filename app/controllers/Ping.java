package controllers;

import play.mvc.*;

import views.html.*;

public class Ping extends Controller {

    public Result ping() {
        return ok("Pong");
    }

}
