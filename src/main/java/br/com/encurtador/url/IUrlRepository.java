package br.com.encurtador.url;

import br.com.encurtador.generic.IGenericRepository;

import java.util.Optional;

/**
 * Repositório específico de Url.
 */
public interface IUrlRepository extends IGenericRepository<UrlEntity, Long> {

    Optional<UrlEntity> findByCodeOrAlias(String codeOrAlias);

    boolean existsAlias(String alias);

    boolean existsShortCode(String shortCode);
}
