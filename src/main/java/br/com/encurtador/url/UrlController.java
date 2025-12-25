package br.com.encurtador.url;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller (JAX-RS Resource) de Url.
 *
 * Responsabilidade:
 * - Expor endpoints HTTP (REST) do CRUD de URLs
 * - Delegar toda regra de negócio para a Service
 *
 * Observação:
 * - Para evitar problemas de serialização/DTO de response,
 *   usamos Map<String, Object> para retornar mensagens em JSON:
 *   { "mensagem": "..." }
 */
@Path("/api/url")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UrlController {

    @Inject
    private IUrlService service;

    /**
     * Monta um JSON simples de resposta com mensagem.
     */
    private Map<String, Object> msg(String texto) {
        Map<String, Object> body = new HashMap<>();
        body.put("mensagem", texto);
        return body;
    }

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

    @GET
    @Path("/redirecionamento/{code}")
    @Produces(MediaType.WILDCARD)
    public Response redirect(@PathParam("code") String code) {
        try {
            String urlOriginal = service.resolveAndCount(code);

            // Redirect não deve retornar JSON, e sim 302/307 com Location
            return Response.temporaryRedirect(URI.create(urlOriginal)).build();

        } catch (Exception e) {

            // Aqui você escolhe: 404 com JSON (funciona para Postman)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(msg(e.getMessage()))
                    .build();
        }
    }
}
