package br.com.encurtador.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para receber as credenciais do login (mock).
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String user;
    private String senha;
}
