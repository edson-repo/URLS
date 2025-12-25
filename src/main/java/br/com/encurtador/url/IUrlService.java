package br.com.encurtador.url;

import java.util.List;

/**
 * Contrato de serviço da entidade Url.
 *
 * Centraliza as regras de negócio do encurtador de URLs,
 * servindo de ponte entre o Controller e o Repositório.
 */
public interface IUrlService {

    /**
     * Cria uma nova URL encurtada.
     *
     * @param dto dados da URL a ser criada
     * @return mensagem de sucesso ou erro
     */
    String save(UrlDTO dto);

    /**
     * Lista todas as URLs cadastradas.
     *
     * @return lista de URLs no formato DTO
     */
    List<UrlDTO> list();

    /**
     * Busca uma URL pelo ID.
     *
     * @param id identificador da URL
     * @return UrlDTO correspondente
     */
    UrlDTO findById(Long id);

    /**
     * Atualiza os dados de uma URL existente.
     *
     * @param id  identificador da URL
     * @param dto novos dados da URL
     * @return mensagem de sucesso ou erro
     */
    String update(Long id, UrlDTO dto);

    /**
     * Remove uma URL pelo ID.
     *
     * @param id identificador da URL
     * @return mensagem de sucesso ou erro
     */
    String delete(Long id);

    /**
     * Resolve um alias ou shortCode para a URL original
     * e incrementa o contador de acessos (hits).
     *
     * @param codeOrAlias alias ou código encurtado
     * @return URL original para redirecionamento
     */
    String resolveAndCount(String codeOrAlias);
}
