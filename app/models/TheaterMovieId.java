package models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class TheaterMovieId implements Serializable {

    @ManyToOne(targetEntity = Theater.class)
    @Setter
    @Getter
    private Theater theater;

    @ManyToOne(targetEntity = Movie.class)
    @Setter
    @Getter
    private Movie movie;

}
