package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.List;

@Entity
public class Site {

    @Id
    @GeneratedValue
    @Setter
    @Getter
    private Integer id;

    @Column
    @Setter
    @Getter
    private String name;

    @Column
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
    private Integer moviesScrappingFrequencyInDays;

    @OneToMany(mappedBy = "primaryKeys.site")
    @Cascade(CascadeType.ALL)
    @Setter
    @Getter
    private List<SiteCity> siteCities;

}
