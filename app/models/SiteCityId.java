package models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class SiteCityId implements Serializable {

    @ManyToOne(targetEntity = Site.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    @Setter
    @Getter
    private Site site;

    @ManyToOne(targetEntity = City.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    @Setter
    @Getter
    private City city;

}
