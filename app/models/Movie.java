package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Movie {

    @Id
    @GeneratedValue
    @Setter @Getter
    private Integer id;

    @Column
    @Setter @Getter
    private String title;

    @Column
    @Setter @Getter
    private String synopsis;

    @Column(name = "image_url")
    @Setter @Getter
    private String imageUrl;

    public Movie(){

    }

    public Movie(String title, String synopsis){
        this.title = title;
        this.synopsis = synopsis;
    }

}
