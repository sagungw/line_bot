package models;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity(name = "City")
@Table(name = "city")
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

    @Column(name = "display_name")
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
        this(displayName);
        this.value = value;
    }

}
