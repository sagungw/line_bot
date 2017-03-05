package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "site_city")
@AssociationOverrides({
        @AssociationOverride(name = "primaryKeys.site", joinColumns = {@JoinColumn(name = "site_id")}),
        @AssociationOverride(name = "primaryKeys.city", joinColumns = {@JoinColumn(name = "city_id")})
})
public class SiteCity {

    @EmbeddedId
    @Setter
    @Getter
    private SiteCityId primaryKeys = new SiteCityId();

    @Column
    @Setter
    @Getter
    private String displayName;

    public void setSite(Site site) {
        Site oldSite = this.getSite();
        if (oldSite != null && oldSite.equals(site))
            return;

        this.primaryKeys.setSite(site);
    }

    @Transient
    public Site getSite() {
        return this.primaryKeys.getSite();
    }

    public void setCity(City city) {
        City oldCity = this.getCity();
        if (oldCity != null && oldCity.equals(city))
            return;

        this.primaryKeys.setCity(city);
    }

    @Transient
    public City getCity() {
        return this.primaryKeys.getCity();
    }

    public SiteCity() {

    }

    public SiteCity(String displayName) {
        this.displayName = displayName;
    }

    public SiteCity(String displayName, Site site, City city) {
        this(displayName);
        this.setSite(site);
        this.setCity(city);
    }

}
