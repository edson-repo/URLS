package br.com.encurtador.url;

import java.util.Optional;

import br.com.encurtador.generic.IGenericRepository;

/**
 * Repositório específico da entidade {@link UrlEntity}.
 *
 * Define operações de persistência adicionais além do CRUD básico,
 * voltadas às regras de negócio do encurtador de URLs.
 */
public interface IUrlRepository extends IGenericRepository<UrlEntity, Long> {

    /**
     * Busca uma URL pelo código encurtado ou pelo alias.
     *
     * Utilizado principalmente no fluxo de redirecionamento (/r/{code}).
     *
     * @param codeOrAlias código encurtado ou alias informado na URL
     * @return Optional contendo a UrlEntity, se encontrada
     */
    Optional<UrlEntity> findByCodeOrAlias(String codeOrAlias);

    /**
     * Verifica se já existe uma URL cadastrada com o alias informado.
     *
     * @param alias alias a ser verificado
     * @return true se o alias já existir, false caso contrário
     */
    boolean existsAlias(String alias);

    /**
     * Verifica se já existe uma URL cadastrada com o shortCode informado.
     *
     * @param shortCode código encurtado a ser verificado
     * @return true se o shortCode já existir, false caso contrário
     */
    boolean existsShortCode(String shortCode);
}
