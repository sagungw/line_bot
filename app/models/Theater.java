package models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.List;

@Entity
public class Theater {

    private enum Chain {
        CINEMA_XXI("XXI"),
        CINEMA_21("21"),
        THE_PREMIERE("Premiere");

        private String displayName;

        Chain(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }

    @Id
    @GeneratedValue
    @Setter
    @Getter
    private Integer id;

    @Column(unique = true)
    @Getter
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Getter
    private Chain chain;

    @Column
    @Setter
    @Getter
    private String url;

    @Column
    @Setter
    @Getter
    private Integer cityValue;

    @OneToMany(mappedBy = "primaryKeys.theater", cascade = javax.persistence.CascadeType.ALL)
    @Setter
    @Getter
    private List<TheaterMovie> theaterMovies;

    public Theater() {
    }

    public Theater(String name) {
        this.setNameWithChain(name);
    }

    public Theater(String name, String url, Integer cityValue) {
        this(name);
        this.url = url;
        this.cityValue = cityValue;
    }

    public void setNameWithChain(String name) {
        this.name = name;
        this.chain = this.defineChain(name);
    }

    private Chain defineChain(String name) {
        String[] strings = name.split(" ");
        String suffix = strings[strings.length - 1];
        switch (suffix.toLowerCase()) {
            case "premiere":
                return Chain.THE_PREMIERE;
            case "xxi":
                return Chain.CINEMA_XXI;
            default:
                return Chain.CINEMA_21;
        }
    }

}
