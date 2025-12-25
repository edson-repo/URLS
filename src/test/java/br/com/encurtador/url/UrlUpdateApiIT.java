package br.com.encurtador.url;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * Teste de Integração (IT) para o endpoint:
 * - PUT /rest/api/url/update/{id}
 *
 * Estratégia:
 * 1) Cria registro e obtém ID
 * 2) Atualiza a originalUrl + alias
 * 3) Busca por ID e valida que a originalUrl mudou
 */
public class UrlUpdateApiIT {

    @Test
    public void deveAtualizarUrlComSucesso() {

        // ---------- Arrange ----------
        String baseUrl = UrlTestSupport.getBaseUrl();

        Long id = UrlTestSupport.criarUrlEObterId(baseUrl, "upd");
        Assert.assertNotNull("ID não deveria ser nulo.", id);

        String novoAlias = "upd2-" + UUID.randomUUID().toString().substring(0, 8);
        String novaUrl = "https://www.wikipedia.org";

        String updateEndpoint = baseUrl + "/rest/api/url/update/" + id;

        String bodyUpdate = "{"
                + "\"originalUrl\":\"" + novaUrl + "\","
                + "\"alias\":\"" + novoAlias + "\""
                + "}";

        // ---------- Act (update) ----------
        String updateResponse =
                RestAssured
                        .given()
                            .contentType(ContentType.JSON)
                            .body(bodyUpdate)
                        .when()
                            .put(updateEndpoint)
                        .then()
                            .statusCode(200)
                            .contentType(ContentType.JSON)
                            .extract()
                            .asString();

        // ---------- Assert (update message) ----------
        Assert.assertTrue("Resposta do update não parece sucesso. Body: " + updateResponse,
                updateResponse.toLowerCase().contains("sucesso"));

        // ---------- Assert (find again) ----------
        String findEndpoint = baseUrl + "/rest/api/url/find/" + id;

        String findBody =
                RestAssured
                        .given()
                            .accept(ContentType.JSON)
                        .when()
                            .get(findEndpoint)
                        .then()
                            .statusCode(200)
                            .contentType(ContentType.JSON)
                            .extract()
                            .asString();

        Assert.assertTrue("A URL não foi atualizada. Body: " + findBody, findBody.contains(novaUrl));
        Assert.assertTrue("O alias não foi atualizado. Body: " + findBody, findBody.contains(novoAlias));
    }
}
