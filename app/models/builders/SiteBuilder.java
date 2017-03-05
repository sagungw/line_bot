package models.builders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import models.Site;
import play.api.Environment;
import play.db.jpa.JPAApi;
import repositories.SiteRepository;

import java.io.IOException;
import java.io.InputStream;

public class SiteBuilder {

    private final String jsonFileLocation = "sites_json/";

    private ObjectMapper jsonMapper;

    private Environment environment;

    private SiteRepository siteRepository;

    @Inject
    public SiteBuilder(Environment environment, SiteRepository siteRepository) {
        this.jsonMapper = new ObjectMapper();
        this.environment = environment;
        this.siteRepository = siteRepository;
    }

    public Site buildSiteFromJson(String jsonFileName) throws IOException {
        InputStream is = environment.classLoader().getResourceAsStream(jsonFileLocation + jsonFileName + ".json");
        Site site = jsonMapper.readValue(is, Site.class);

        Site oldSite = siteRepository.findByName(site.getName());
        if (oldSite == null) {
            siteRepository.save(site);
        } else {
            site.setId(oldSite.getId());
            site = siteRepository.update(site);
        }

        return site;
    }

}
