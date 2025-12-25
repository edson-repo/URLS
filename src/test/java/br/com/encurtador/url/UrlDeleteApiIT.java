package br.com.encurtador.url;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Teste de Integração (IT) para o endpoint:
 * - DELETE /rest/api/url/delete/{id}
 *
 * Estratégia:
 * 1) Cria registro e obtém ID
 * 2) Deleta
 * 3) Busca por ID e espera erro (400) com mensagem "não encontrado"
 */
public class UrlDeleteApiIT {

    @Test
    public void deveDeletarUrlComSucesso() {

        // ---------- Arrange ----------
        String baseUrl = UrlTestSupport.getBaseUrl();

        Long id = UrlTestSupport.criarUrlEObterId(baseUrl, "del");
        Assert.assertNotNull("ID não deveria ser nulo.", id);

        String deleteEndpoint = baseUrl + "/rest/api/url/delete/" + id;

        // ---------- Act (delete) ----------
        String deleteResponse =
                RestAssured
                        .given()
                            .accept(ContentType.JSON)
                        .when()
                            .delete(deleteEndpoint)
                        .then()
                            .statusCode(200)
                            .contentType(ContentType.JSON)
                            .extract()
                            .asString();

        Assert.assertTrue("Resposta do delete não parece sucesso. Body: " + deleteResponse,
                deleteResponse.toLowerCase().contains("sucesso") || deleteResponse.toLowerCase().contains("removid"));

        // ---------- Assert (find after delete) ----------
        String findEndpoint = baseUrl + "/rest/api/url/find/" + id;

        String findAfterDelete =
                RestAssured
                        .given()
                            .accept(ContentType.JSON)
                        .when()
                            .get(findEndpoint)
                        .then()
                            // Nosso controller retorna BAD_REQUEST (400) quando lança Exception no findById
                            .statusCode(400)
                            .contentType(ContentType.JSON)
                            .extract()
                            .asString();

        Assert.assertTrue("Esperado 'não encontrado' após delete. Body: " + findAfterDelete,
                findAfterDelete.toLowerCase().contains("não encontrado") || findAfterDelete.toLowerCase().contains("nao encontrado"));
    }
}
