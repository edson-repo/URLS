package br.com.encurtador.url;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade JPA que representa a tabela "url".
 *
 * Responsável por mapear os dados persistidos no banco
 * referentes às URLs encurtadas.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "url")
public class UrlEntity {

    /**
     * Identificador único da URL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * URL original informada pelo usuário.
     */
    @Column(name = "original_url", length = 2048, nullable = false)
    private String originalUrl;

    /**
     * Código encurtado gerado automaticamente pelo sistema.
     */
    @Column(name = "short_code", length = 64, nullable = false, unique = true)
    private String shortCode;

    /**
     * Alias opcional definido pelo usuário.
     */
    @Column(name = "alias", length = 64, unique = true)
    private String alias;

    /**
     * Data e hora de criação do registro.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Contador de acessos (hits) da URL.
     */
    @Column(name = "hits", nullable = false)
    private Long hits;
}
