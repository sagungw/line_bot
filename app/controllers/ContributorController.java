package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.Contributor;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class ContributorController extends Controller {

    private JPAApi jpaApi;

    @Inject
    public ContributorController(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public Result createUser() {
        JsonNode json = request().body().asJson();
        Contributor contributor = Json.fromJson(json, Contributor.class);
        jpaApi.em().persist(contributor);

        return ok("Hello " + contributor.getName());
    }

}
