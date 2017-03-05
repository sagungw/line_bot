package repositories;

import com.google.inject.Inject;
import models.Site;
import play.db.jpa.JPAApi;

import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.util.List;

public class SiteRepository {

    private JPAApi jpaApi;

    @Inject
    public SiteRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public Site findByName(String name) {
        List<Site> sites = jpaApi.withTransaction(entityManager -> {
            Query query = entityManager.createQuery("SELECT s FROM Site s WHERE s.name = '" + name + "'");
            return query.getResultList();
        });

        return sites.isEmpty() ? null : sites.get(0);
    }

    public void save(Site site) {
        jpaApi.withTransaction(() -> jpaApi.em().persist(site));
    }

    public Site update(Site site) throws EntityNotFoundException {
        if(site.getId() != null) {
            return jpaApi.withTransaction(() -> { return jpaApi.em().merge(site); });
        } else {
            throw new EntityNotFoundException("Object is not yet persisted.");
        }
    }

}
