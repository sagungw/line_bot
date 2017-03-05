package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.Site;
import models.builders.SiteBuilder;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import scrappers.xxi_cineplex.CitiesScrapper;
import scrappers.xxi_cineplex.TheatersScrapper;

import java.io.IOException;

public class ScrappingController extends Controller {

    private SiteBuilder siteBuilder;

    private CitiesScrapper citiesScrapper;

    private TheatersScrapper theaterScrapper;

    @Inject
    public ScrappingController(SiteBuilder siteBuilder, CitiesScrapper citiesScrapper, TheatersScrapper theaterScrapper) {
        this.siteBuilder = siteBuilder;
        this.citiesScrapper = citiesScrapper;
        this.theaterScrapper = theaterScrapper;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result scrapCity() {
        JsonNode json = request().body().asJson();
        String sitePropertiesFileName = json.findValue("file_prop_name").asText();

        try {
            Site site = siteBuilder.buildSiteFromJson(sitePropertiesFileName);
            citiesScrapper.scrap(site);
            return ok("Cities data from the website is successfully fetched");
        } catch (IOException e) {
            e.printStackTrace();
            return notFound();
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result scrapTheater() {
//        JsonNode json = request().body().asJson();
//        String sitePropertiesFileName = json.findValue("file_prop_name").asText();
//
//        try {
//            Site site = siteBuilder.buildSiteFromJson(sitePropertiesFileName);
//            theaterScrapper.scrap(site);
            return ok("Cities data from the website is successfully fetched");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return notFound();
//        }
    }

}