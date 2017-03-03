package models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "primaryKeys.city")
    @Cascade(CascadeType.ALL)
    @Setter
    @Getter
    private List<SiteCity> siteCities;

    public City(String name) {
        this.name = name;
    }

}
