package br.com.encurtador.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller de autenticação (mock) para simular login.
 * - Se user/senha baterem, grava "user" na sessão.
 * - Gera um JWT simples e devolve no response (apenas demo).
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

            // ---------- Validação do body ----------
            if (dto == null) {
                Map<String, Object> body = new HashMap<>();
                body.put("mensagem", "Body é obrigatório.");
                return Response.status(Response.Status.BAD_REQUEST).entity(body).build();
            }

            boolean okUser = USER_MOCK.equals(dto.getUser());
            boolean okSenha = SENHA_MOCK.equals(dto.getSenha());

            if (!okUser || !okSenha) {
                Map<String, Object> body = new HashMap<>();
                body.put("mensagem", "Usuário ou senha inválidos.");
                return Response.status(Response.Status.UNAUTHORIZED).entity(body).build();
            }

            // ---------- Sessão ----------
            HttpSession session = request.getSession(true);
            session.setAttribute("user", dto.getUser());

            // ---------- JWT (demo) ----------
            String token = JwtUtil.gerarToken(dto.getUser());

            // ---------- Response ----------
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "Login realizado com sucesso.");
            response.put("token", token);

            return Response.ok(response).build();

        } catch (Exception e) {

            Map<String, Object> body = new HashMap<>();
            body.put("mensagem", e.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).entity(body).build();
        }
    }
}
