package br.com.encurtador.auth;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controller de autenticação (mock) usado para simular login no ambiente de teste.
 *
 * Regras:
 * - Valida se o body foi enviado.
 * - Compara credenciais com valores fixos (mock).
 * - Se válido: grava o usuário na sessão e retorna um token JWT (demo).
 *
 * Observação: por ser mock, não há consulta a banco nem criptografia real de senha.
 */
@Path("/api/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    private static final String USER_MOCK = "user";
    private static final String SENHA_MOCK = "123456";

    @POST
    @Path("/login")
    public Response login(LoginDTO dto, @Context HttpServletRequest request) {
        try {
            // 1) Validação do body
            if (dto == null) {
                return badRequest("Body é obrigatório.");
            }

            // 2) Validação das credenciais (mock)
            boolean usuarioValido = USER_MOCK.equals(dto.getUser());
            boolean senhaValida = SENHA_MOCK.equals(dto.getSenha());

            if (!usuarioValido || !senhaValida) {
                return unauthorized("Usuário ou senha inválidos.");
            }

            // 3) Criação/uso de sessão
            HttpSession session = request.getSession(true);
            session.setAttribute("user", dto.getUser());

            // 4) Geração de token JWT (demo)
            String token = JwtUtil.gerarToken(dto.getUser());

            // 5) Montagem da resposta
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("mensagem", "Login realizado com sucesso.");
            responseBody.put("token", token);

            return Response.ok(responseBody).build();

        } catch (Exception e) {
            // Erro inesperado: retorna mensagem para facilitar debug no ambiente de teste
            return badRequest(e.getMessage());
        }
    }

    /**
     * Cria resposta HTTP 400 (Bad Request) padronizada.
     */
    private Response badRequest(String mensagem) {
        Map<String, Object> body = new HashMap<>();
        body.put("mensagem", mensagem);
        return Response.status(Response.Status.BAD_REQUEST).entity(body).build();
    }

    /**
     * Cria resposta HTTP 401 (Unauthorized) padronizada.
     */
    private Response unauthorized(String mensagem) {
        Map<String, Object> body = new HashMap<>();
        body.put("mensagem", mensagem);
        return Response.status(Response.Status.UNAUTHORIZED).entity(body).build();
    }
}
