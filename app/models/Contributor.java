package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Contributor {

    @Id
    @GeneratedValue
    @Setter @Getter
    private Integer id;

    @Column(name = "name")
    @Setter @Getter
    private String name;

}
