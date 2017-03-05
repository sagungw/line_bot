package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Site {

    @Id
    @GeneratedValue
    @Setter
    @Getter
    private Integer id;

    @Column(unique = true)
    @Setter
    @Getter
    private String name;

    @Column(unique = true)
    @Setter
    @Getter
    private String url;

    @ElementCollection
    @CollectionTable(name = "cities_entry_css_selectors", joinColumns = {@JoinColumn(name = "site_id")})
    @Column(name = "cities_entry_css_selectors")
    @JsonProperty("cities_entry_css_selectors")
    @Setter
    @Getter
    private List<String> citiesEntryCssSelectors;

    @Column
    @JsonProperty("city_css_selector")
    @Setter
    @Getter
    private String cityCssSelector;

    @Column
    @JsonProperty("city_select_form_selector")
    @Setter
    @Getter
    private String citySelectFormSelector;

    @ElementCollection
    @CollectionTable(name = "theaters_entry_css_selectors", joinColumns = {@JoinColumn(name = "site_id")})
    @Column(name = "theaters_entry_css_selectors")
    @JsonProperty("theaters_entry_css_selectors")
    @Setter
    @Getter
    private List<String> theatersEntryCssSelectors;

    @Column
    @JsonProperty("theater_css_selector")
    @Setter
    @Getter
    private String theaterCssSelector;

    @Column
    @JsonProperty("theater_name_css_selector")
    @Setter
    @Getter
    private String theaterNameCssSelector;

    @Column
    @JsonProperty("movie_list_css_selector")
    @Setter
    @Getter
    private String movieListCssSelector;

    @Column
    @JsonProperty("movie_row_css_selector")
    @Setter
    @Getter
    private String movieRowCssSelector;

    @Column
    @JsonProperty("movie_link_css_selector")
    @Setter
    @Getter
    private String movieLinkCssSelector;

    @Column
    @JsonProperty("movie_show_time_css_selector")
    @Setter
    @Getter
    private String movieShowTimeCssSelector;

    @Column
    @JsonProperty("movie_name_css_selector")
    @Setter
    @Getter
    private String movieNameCssSelector;

    @Column
    @JsonProperty("movie_synopsis_css_selector")
    @Setter
    @Getter
    private String movieSynopsisCssSelector;

    @Column
    @JsonProperty("movies_scrapping_frequency_in_days")
    @Setter
    @Getter
    private Integer moviesScrappingFrequencyInDays = 0;

    @OneToMany(mappedBy = "primaryKeys.site", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter
    @Getter
    private List<SiteCity> siteCities = new ArrayList<>();

    public void addSiteCity(SiteCity siteCity) {
        if(siteCities.contains(siteCity))
            return;

        siteCities.add(siteCity);
        siteCity.setSite(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Site){
            Site other = (Site) obj;
            return other.getName().equals(this.getName()) &&
                    other.getUrl().equals(this.getUrl()) &&
                    other.getCityCssSelector().equals(this.getCityCssSelector()) &&
                    other.getCitySelectFormSelector().equals(this.getCitySelectFormSelector()) &&
                    other.getMovieLinkCssSelector().equals(this.getMovieLinkCssSelector()) &&
                    other.getMovieListCssSelector().equals(this.getMovieListCssSelector()) &&
                    other.getMovieNameCssSelector().equals(this.getMovieNameCssSelector()) &&
                    other.getMovieRowCssSelector().equals(this.getMovieRowCssSelector()) &&
                    other.getMovieShowTimeCssSelector().equals(this.getMovieShowTimeCssSelector()) &&
                    other.getMovieSynopsisCssSelector().equals(this.getMovieSynopsisCssSelector()) &&
                    other.getMoviesScrappingFrequencyInDays().equals(this.getMoviesScrappingFrequencyInDays());
        } else {
            return false;
        }
    }
}
