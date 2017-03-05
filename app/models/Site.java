package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Site {

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String url;

    @JsonProperty("cities_entry_css_selectors")
    @Setter
    @Getter
    private List<String> citiesEntryCssSelectors;

    @JsonProperty("city_css_selector")
    @Setter
    @Getter
    private String cityCssSelector;

    @JsonProperty("city_select_form_selector")
    @Setter
    @Getter
    private String citySelectFormSelector;

    @JsonProperty("theaters_entry_css_selectors")
    @Setter
    @Getter
    private List<String> theatersEntryCssSelectors;
    
    @JsonProperty("theater_css_selector")
    @Setter
    @Getter
    private String theaterCssSelector;
    
    @JsonProperty("theater_name_css_selector")
    @Setter
    @Getter
    private String theaterNameCssSelector;
    
    @JsonProperty("movie_list_css_selector")
    @Setter
    @Getter
    private String movieListCssSelector;
    
    @JsonProperty("movie_row_css_selector")
    @Setter
    @Getter
    private String movieRowCssSelector;
    
    @JsonProperty("movie_link_css_selector")
    @Setter
    @Getter
    private String movieLinkCssSelector;

    @JsonProperty("movie_show_time_css_selector")
    @Setter
    @Getter
    private String movieShowTimeCssSelector;

    @JsonProperty("movie_name_css_selector")
    @Setter
    @Getter
    private String movieNameCssSelector;

    @JsonProperty("movie_synopsis_css_selector")
    @Setter
    @Getter
    private String movieSynopsisCssSelector;

    @JsonProperty("movies_scrapping_frequency_in_days")
    @Setter
    @Getter
    private Integer moviesScrappingFrequencyInDays = 0;

    public String replaceSelectorPlaceholderWithValue(String selector, String value) {
        return selector.replace("{{value}}", value);
    }

}
