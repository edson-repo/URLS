package br.com.encurtador.url;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controller (JAX-RS Resource) responsável por expor os endpoints REST de URL.
 *
 * Responsabilidades:
 * - Receber requisições HTTP (REST)
 * - Delegar toda regra de negócio para a camada de Service
 * - Padronizar retornos de sucesso/erro (mensagem em JSON)
 *
 * Observação:
 * Para padronizar respostas simples, este controller retorna Map<String, Object> no formato:
 * { "mensagem": "..." }
 */
@Path("/api/url")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UrlController {

    @Inject
    private IUrlService service;

    /**
     * Cria um JSON simples no padrão: { "mensagem": "..." }.
     */
    private Map<String, Object> msg(String texto) {
        Map<String, Object> body = new HashMap<>();
        body.put("mensagem", texto);
        return body;
    }

    /**
     * Endpoint: POST /api/url/save
     * Cria uma URL encurtada.
     */
    @POST
    @Path("/save")
    public Response save(UrlDTO dto) {
        try {
            String criado = service.save(dto);
            return Response.ok(msg(criado)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg(e.getMessage()))
                    .build();
        }
    }

    /**
     * Endpoint: GET /api/url/list
     * Lista todas as URLs cadastradas.
     */
    @GET
    @Path("/list")
    public Response list() {
        try {
            List<UrlDTO> list = service.list();
            return Response.ok(list).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg(e.getMessage()))
                    .build();
        }
    }

    /**
     * Endpoint: GET /api/url/find/{id}
     * Busca uma URL pelo ID.
     */
    @GET
    @Path("/find/{id}")
    public Response findById(@PathParam("id") Long id) {
        try {
            UrlDTO dto = service.findById(id);
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg(e.getMessage()))
                    .build();
        }
    }

    /**
     * Endpoint: PUT /api/url/update/{id}
     * Atualiza uma URL existente.
     */
    @PUT
    @Path("/update/{id}")
    public Response update(@PathParam("id") Long id, UrlDTO dto) {
        try {
            String atualizado = service.update(id, dto);
            return Response.ok(msg(atualizado)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg(e.getMessage()))
                    .build();
        }
    }

    /**
     * Endpoint: DELETE /api/url/delete/{id}
     * Remove uma URL pelo ID.
     */
    @DELETE
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            String removido = service.delete(id);
            return Response.ok(msg(removido)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg(e.getMessage()))
                    .build();
        }
    }

    /**
     * Endpoint: GET /api/url/redirecionamento/{code}
     *
     * Resolve o alias/shortCode e responde com redirect HTTP (Location).
     *
     * Observação:
     * - Aqui o retorno NÃO é JSON (é redirect).
     * - Mantém @Produces(WILDCARD) para não forçar JSON nesse cenário.
     */
    @GET
    @Path("/redirecionamento/{code}")
    @Produces(MediaType.WILDCARD)
    public Response redirect(@PathParam("code") String code) {
        try {
            String urlOriginal = service.resolveAndCount(code);

            // Redirect: responde com 307 (temporary redirect) e header Location
            return Response.temporaryRedirect(URI.create(urlOriginal)).build();

        } catch (Exception e) {

            // Quando falhar o resolve, retornamos 404 com JSON (útil no Postman)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(msg(e.getMessage()))
                    .build();
        }
    }
}
