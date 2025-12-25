package br.com.encurtador.url;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do repositório usando JPA (EntityManager).
 */
@Stateless
public class UrlRepository implements IUrlRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UrlEntity save(UrlEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public UrlEntity update(UrlEntity entity) {
        return entityManager.merge(entity);
    }

    @Override
    public Optional<UrlEntity> findById(Long id) {
        UrlEntity entity = entityManager.find(UrlEntity.class, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public List<UrlEntity> findAll() {
        TypedQuery<UrlEntity> query = entityManager.createQuery(
                "select u from UrlEntity u order by u.id desc",
                UrlEntity.class
        );
        return query.getResultList();
    }

    @Override
    public void deleteById(Long id) {
        Optional<UrlEntity> opt = findById(id);
        if (opt.isPresent()) {
            entityManager.remove(opt.get());
        }
    }

    @Override
    public Optional<UrlEntity> findByCodeOrAlias(String codeOrAlias) {
        TypedQuery<UrlEntity> query = entityManager.createQuery(
                "select u from UrlEntity u where u.shortCode = :code or u.alias = :code",
                UrlEntity.class
        );
        query.setParameter("code", codeOrAlias);

        List<UrlEntity> list = query.getResultList();
        if (list == null || list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    @Override
    public boolean existsAlias(String alias) {
        if (alias == null || alias.trim().isEmpty()) {
            return false;
        }

        TypedQuery<Long> query = entityManager.createQuery(
                "select count(u) from UrlEntity u where u.alias = :alias",
                Long.class
        );
        query.setParameter("alias", alias.trim());

        Long count = query.getSingleResult();
        return count != null && count > 0;
    }

    @Override
    public boolean existsShortCode(String shortCode) {
        if (shortCode == null || shortCode.trim().isEmpty()) {
            return false;
        }

        TypedQuery<Long> query = entityManager.createQuery(
                "select count(u) from UrlEntity u where u.shortCode = :code",
                Long.class
        );
        query.setParameter("code", shortCode.trim());

        Long count = query.getSingleResult();
        return count != null && count > 0;
    }
}
