package br.com.encurtador.url;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO de Url.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UrlDTO {

    private Long id;
    private String originalUrl;
    private String shortCode;
    private String alias;
    private LocalDateTime createdAt;
    private Long hits;
}
