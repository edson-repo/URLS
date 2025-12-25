package br.com.encurtador.url;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) da entidade Url.
 *
 * Responsável por transportar dados entre as camadas
 * (Controller ↔ Service) e para o cliente via API.
 *
 * Não contém lógica de negócio.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlDTO {

    /**
     * Identificador da URL.
     */
    private Long id;

    /**
     * URL original informada pelo usuário.
     */
    private String originalUrl;

    /**
     * Código encurtado gerado automaticamente pelo sistema.
     */
    private String shortCode;

    /**
     * Alias opcional definido pelo usuário.
     */
    private String alias;

    /**
     * Data/hora de criação do registro.
     */
    private LocalDateTime createdAt;

    /**
     * Quantidade de acessos (hits) da URL.
     */
    private Long hits;
}
