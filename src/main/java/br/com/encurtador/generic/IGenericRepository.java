package br.com.encurtador.generic;

import java.util.List;
import java.util.Optional;

/**
 * Repositório genérico para padronizar operações básicas de persistência.
 */
public interface IGenericRepository<T, ID> {

    /** Persiste uma entidade nova. */
    T save(T entity);

    /** Atualiza uma entidade existente. */
    T update(T entity);

    /** Busca por ID. */
    Optional<T> findById(ID id);

    /** Lista tudo. */
    List<T> findAll();

    /** Remove por ID. */
    void deleteById(ID id);
}
