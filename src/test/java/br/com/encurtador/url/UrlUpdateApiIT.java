package br.com.encurtador.url;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

/**
 * Teste de Integração (IT) do endpoint:
 * PUT /rest/api/url/update/{id}
 *
 * Estratégia:
 * 1) Cria um registro e obtém o ID
 * 2) Atualiza originalUrl + alias
 * 3) Busca por ID e valida se os campos foram atualizados
 */
public class UrlUpdateApiIT {

    @Test
    public void deveAtualizarUrlComSucesso() {

        // ==========================================================
        // 1) Arrange
        // ==========================================================
        String baseUrl = UrlTestSupport.getBaseUrl();

        Long id = UrlTestSupport.criarUrlEObterId(baseUrl, "upd");
        Assert.assertNotNull("ID não deveria ser nulo.", id);

        String novoAlias = "upd2-" + UUID.randomUUID().toString().substring(0, 8);
        String novaUrl = "https://www.wikipedia.org";

        String updateEndpoint = baseUrl + "/rest/api/url/update/" + id;

        String bodyUpdate =
                "{"
                        + "\"originalUrl\":\"" + novaUrl + "\","
                        + "\"alias\":\"" + novoAlias + "\""
                        + "}";

        // ==========================================================
        // 2) Act (update)
        // ==========================================================
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

        // ==========================================================
        // 3) Assert (mensagem de retorno)
        // ==========================================================
        Assert.assertTrue(
                "Resposta do update não parece sucesso. Body: " + updateResponse,
                updateResponse.toLowerCase().contains("sucesso")
        );

        // ==========================================================
        // 4) Assert (buscar novamente e validar campos)
        // ==========================================================
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
