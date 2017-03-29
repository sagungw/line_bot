package models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
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

    @Column(length = 10000)
    @Setter
    @Getter
    private String synopsis;

    @Column
    @Setter
    @Getter
    private String movieUrl;

    @Column(name = "image_url")
    @Setter
    @Getter
    private String imageUrl;

    @OneToMany(mappedBy = "primaryKeys.movie", fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    @Setter
    @Getter
    private List<TheaterMovie> theaterMovies = new ArrayList<>();

    public Movie(){}

    public Movie(String title) {
        this.title = title;
    }

    public Movie(String title, String movieUrl) {
        this(title);
        this.movieUrl = movieUrl;
    }

    public Movie(String title, String imageUrl, String movieUrl) {
        this(title);
        this.imageUrl = imageUrl;
        this.movieUrl = movieUrl;
    }

}
