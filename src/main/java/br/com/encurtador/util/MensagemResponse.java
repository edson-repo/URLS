package br.com.encurtador.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe de resposta padrão para a API.
 *
 * Utilizada para retornar mensagens ao cliente
 * e, quando necessário, informações adicionais
 * como token de autenticação.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MensagemResponse {

    /**
     * Mensagem descritiva retornada pela API.
     */
    private String mensagem;

    /**
     * Token de autenticação (quando aplicável).
     * Pode ser nulo em respostas que não envolvem login.
     */
    private String token;
}
