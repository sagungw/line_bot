package controllers;

import com.google.inject.Inject;
import models.Site;
import models.builders.SiteBuilder;
import play.mvc.Controller;
import play.mvc.Result;
import scrappers.xxi_cineplex.CitiesScrapper;
import scrappers.xxi_cineplex.MoviesScrapper;
import scrappers.xxi_cineplex.TheatersScrapper;

import java.io.IOException;

// Controller to override scrapping by schedule
public class ScrappingController extends Controller {

    private SiteBuilder siteBuilder;

    private CitiesScrapper citiesScrapper;

    private TheatersScrapper theaterScrapper;

    private MoviesScrapper moviesScrapper;

    @Inject
    public ScrappingController(SiteBuilder siteBuilder, CitiesScrapper citiesScrapper, TheatersScrapper theaterScrapper, MoviesScrapper moviesScrapper) {
        this.siteBuilder = siteBuilder;
        this.citiesScrapper = citiesScrapper;
        this.theaterScrapper = theaterScrapper;
        this.moviesScrapper = moviesScrapper;
    }

    public Result scrapCity() {
        try {
            Site site = siteBuilder.buildSiteFromJson("21cineplex");
            citiesScrapper.scrap(site);
            return ok("Cities data from the website are succesfully scrapped");
        } catch (IOException e) {
            e.printStackTrace();
            return notFound();
        }
    }

    public Result scrapTheater() {
        try {
            Site site = siteBuilder.buildSiteFromJson("21cineplex");
            theaterScrapper.scrap(site);
            return ok("Theater data from the website are succesfully scrapped");
        } catch (IOException e) {
            e.printStackTrace();
            return notFound();
        }
    }

    public Result scrapMovie() {
        try {
            Site site = siteBuilder.buildSiteFromJson("21cineplex");
            moviesScrapper.scrap(site);
            return ok("Movie data from the website are succesfully scrapped");
        } catch (IOException e) {
            e.printStackTrace();
            return notFound();
        }
    }

}
