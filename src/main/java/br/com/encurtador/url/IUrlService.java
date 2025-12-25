package br.com.encurtador.url;

import java.util.List;

/**
 * Contrato de serviço para Url.
 */
public interface IUrlService {

    String save(UrlDTO dto);

    List<UrlDTO> list();

    UrlDTO findById(Long id);

    String update(Long id, UrlDTO dto);

    String delete(Long id);

    String resolveAndCount(String codeOrAlias);
}
