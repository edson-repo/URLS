package br.com.encurtador.generic;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório genérico.
 *
 * Define um contrato padrão para operações básicas
 * de persistência (CRUD), permitindo reutilização
 * e consistência entre os repositórios da aplicação.
 *
 * @param <T>  tipo da entidade
 * @param <ID> tipo do identificador da entidade
 */
public interface IGenericRepository<T, ID> {

    /**
     * Persiste uma nova entidade no banco de dados.
     *
     * @param entity entidade a ser salva
     * @return entidade persistida
     */
    T save(T entity);

    /**
     * Atualiza uma entidade já existente.
     *
     * @param entity entidade a ser atualizada
     * @return entidade atualizada
     */
    T update(T entity);

    /**
     * Busca uma entidade pelo seu identificador.
     *
     * @param id identificador da entidade
     * @return Optional contendo a entidade, se encontrada
     */
    Optional<T> findById(ID id);

    /**
     * Retorna todas as entidades do tipo informado.
     *
     * @return lista de entidades
     */
    List<T> findAll();

    /**
     * Remove uma entidade com base no identificador.
     *
     * @param id identificador da entidade
     */
    void deleteById(ID id);
}
