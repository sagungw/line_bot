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
        this.primaryKeys.setSite(site);
    }

    @Transient
    public Site getSite() {
        return this.primaryKeys.getSite();
    }

    public void setCity(City city) {
        this.primaryKeys.setCity(city);
    }

    @Transient
    public City getCity() {
        return this.primaryKeys.getCity();
    }

    public SiteCity(String displayName) {
        this.displayName = displayName;
    }

}
