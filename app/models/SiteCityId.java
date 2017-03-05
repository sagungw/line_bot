package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class SiteCityId implements Serializable {

    @ManyToOne(targetEntity = Site.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter
    @Getter
    private Site site;

    @ManyToOne(targetEntity = City.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter
    @Getter
    private City city;

}
