package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "theater_movie")
@AssociationOverrides({
        @AssociationOverride(name = "primaryKeys.movie", joinColumns = {@JoinColumn(name = "movie_id")}),
        @AssociationOverride(name = "primaryKeys.theater", joinColumns = {@JoinColumn(name = "theater_id")})
})
public class TheaterMovie {

    @EmbeddedId
    @Setter
    @Getter
    private TheaterMovieId primaryKeys = new TheaterMovieId();

    @Column(nullable = false)
    @Setter
    @Getter
    private boolean is3D = false;

    @Column(nullable = false)
    @Setter
    @Getter
    private boolean isNowPlaying = false;

    @ElementCollection
    @CollectionTable(name = "show_times", joinColumns = {@JoinColumn(name = "theater_id"), @JoinColumn(name = "movie_id")})
    @Column(name = "show_times")
    @Setter
    @Getter
    private List<String> showTime;

    public TheaterMovie() {

    }

    public TheaterMovie(String rawMovieName) {
        this.is3D = this.is3D(rawMovieName);
    }

    public TheaterMovie(String rawMovieName, Movie movie, Theater theater) {
        this(rawMovieName);
        this.setMovie(movie);
        this.setTheater(theater);
    }

    public TheaterMovie(String rawMovieName, Movie movie, Theater theater, List<String> showTimes) {
        this(rawMovieName, movie, theater);
        this.showTime = showTimes;
    }

    public void setMovie(Movie movie) {
        this.primaryKeys.setMovie(movie);
    }

    @Transient
    public Movie getMovie() {
        return this.primaryKeys.getMovie();
    }

    public void setTheater(Theater theater) {
        this.primaryKeys.setTheater(theater);
    }

    @Transient
    public Theater getTheater() {
        return this.primaryKeys.getTheater();
    }

    private boolean is3D(String rawMovieName) {
        String titleLowCase = rawMovieName.toLowerCase();
        return titleLowCase.contains("(imax 3d)") || titleLowCase.contains("(3d)");
    }

}
