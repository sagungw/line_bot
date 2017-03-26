package repositories;

import com.google.inject.Inject;
import models.TheaterMovie;
import play.db.jpa.JPAApi;

import javax.persistence.Query;
import java.util.List;

public class TheaterMovieRepository {

    private JPAApi jpaApi;

    @Inject
    public TheaterMovieRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public List<TheaterMovie> findMoviesInTheaterById(Integer theaterId) {
        return jpaApi.withTransaction(entityManager -> {
            Query query = entityManager.createQuery("SELECT tm FROM theater_movie tm WHERE tm.primaryKeys.theater.id = :theaterId");
            query.setParameter("theaterId", theaterId);
            return query.getResultList();
        });
    }

    public List<TheaterMovie> findMoviesScheduleInTheaterById(Integer theaterId) {
        return jpaApi.withTransaction(entityManager -> {
            Query query = entityManager.createQuery("SELECT tm FROM theater_movie tm JOIN FETCH tm.showTimes WHERE tm.primaryKeys.theater.id = :theaterId");
            query.setParameter("theaterId", theaterId);
            return query.getResultList();
        });
    }

}
