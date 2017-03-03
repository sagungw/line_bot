package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class Webhook extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public Result webhook(){
        JsonNode json = request().body().asJson();
        return ok(json.toString());
    }

}
