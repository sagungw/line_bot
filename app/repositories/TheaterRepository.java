package repositories;

import com.google.inject.Inject;
import models.Theater;
import play.db.jpa.JPAApi;

import javax.persistence.Query;
import java.util.List;

public class TheaterRepository {

    private JPAApi jpaApi;

    @Inject
    public TheaterRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public List<Theater> findTheatersByName(String theaterName) {
        return jpaApi.withTransaction(entityManager -> {
            Query query = entityManager.createQuery("SELECT t FROM Theater t WHERE LOWER(t.name) LIKE '%" + theaterName.toLowerCase() + "%'");
            return query.getResultList();
        });
    }

}
