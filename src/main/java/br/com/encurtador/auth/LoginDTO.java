package br.com.encurtador.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO responsável por transportar as credenciais
 * enviadas no processo de login.
 *
 * Usado exclusivamente para autenticação mock
 * no contexto da entrevista técnica.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    /**
     * Nome do usuário informado no login.
     */
    private String user;

    /**
     * Senha informada no login.
     */
    private String senha;
}
