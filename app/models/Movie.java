package models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.List;

@Entity
public class Movie {

    @Id
    @GeneratedValue
    @Setter
    @Getter
    private Integer id;

    @Column(unique = true)
    @Setter
    @Getter
    private String title;

    @Column
    @Setter
    @Getter
    private String synopsis;

    @Column(name = "image_url")
    @Setter
    @Getter
    private String imageUrl;

    @OneToMany(mappedBy = "primaryKeys.movie")
    @Cascade(CascadeType.ALL)
    @Setter
    @Getter
    private List<TheaterMovie> theaterMovies;

    public Movie(String title) {
        this.title = this.normalizeTitle(title);
    }

    public Movie(String title, String synopsis) {
        this(title);
        this.synopsis = synopsis;
    }

    private String normalizeTitle(String title) {
        if (title.contains("(IMAX 3D)")) {
            title = title.replace(" (IMAX 3D)", "");
        }
        return title;
    }

}
