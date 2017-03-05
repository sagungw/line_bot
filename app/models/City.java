package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class City {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Integer id;

    @Column(unique = true)
    @Getter
    @Setter
    private String name;

    @OneToMany(mappedBy = "primaryKeys.city", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter
    @Getter
    private List<SiteCity> siteCities = new ArrayList<>();

    public City() {

    }

    public City(String name) {
        this.name = name;
    }

    public void addSiteCity(SiteCity siteCity) {
        if(siteCities.contains(siteCity))
            return;

        siteCities.add(siteCity);
        siteCity.setCity(this);
    }

}
