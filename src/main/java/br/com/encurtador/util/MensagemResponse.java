package br.com.encurtador.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response simples para padronizar mensagens retornadas pela API.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MensagemResponse {

    private String mensagem;
    private String token;
}
