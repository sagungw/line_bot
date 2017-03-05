package models;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
    private Integer value;

    @Column(unique = true)
    @Getter
    @Setter
    private String name;

    @Column
    @Getter
    @Setter
    private String displayName;

    public City() {
    }

    public City(String displayName) {
        this.displayName = displayName;
        this.name = StringUtils.capitalize(displayName.toLowerCase());
    }

    public City(Integer value, String displayName) {
        this.value = value;
        this.displayName = displayName;
        this.name = StringUtils.capitalize(displayName.toLowerCase());
    }

}
