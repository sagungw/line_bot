package models.builders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import models.Site;
import play.api.Environment;

import java.io.IOException;
import java.io.InputStream;

public class SiteBuilder {

    private final String jsonFileLocation = "sites_json/";

    private ObjectMapper jsonMapper;

    private Environment environment;

    @Inject
    public SiteBuilder(Environment environment) {
        this.jsonMapper = new ObjectMapper();
        this.environment = environment;
    }

    public Site buildSiteFromJson(String jsonFileName) throws IOException {
        InputStream is = environment.classLoader().getResourceAsStream(jsonFileLocation + jsonFileName + ".json");
        return jsonMapper.readValue(is, Site.class);
    }

}
