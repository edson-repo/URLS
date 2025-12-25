package br.com.encurtador.url;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

/**
 * Teste de Integração (IT) do endpoint:
 * GET /rest/api/url/list
 *
 * Estratégia:
 * 1) Cria uma URL com alias único (garante massa de dados)
 * 2) Executa o GET /list
 * 3) Valida que a resposta é uma lista JSON não vazia
 */
public class UrlListApiIT {

    @Test
    public void deveListarUrlsComSucesso() {

        // ==========================================================
        // 1) Arrange
        // ==========================================================
        String baseUrl = UrlTestSupport.getBaseUrl();

        // Garante que exista ao menos um registro no banco
        UrlTestSupport.criarUrlComAliasUnico(baseUrl, "list");

        String listEndpoint = baseUrl + "/rest/api/url/list";

        // ==========================================================
        // 2) Act
        // ==========================================================
        String responseBody =
                RestAssured
                        .given()
                        .accept(ContentType.JSON)
                        .when()
                        .get(listEndpoint)
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract()
                        .asString();

        String responseTrim = responseBody.trim();

        // ==========================================================
        // 3) Assert
        // ==========================================================
        Assert.assertTrue(
                "Resposta deveria iniciar com '['. Resposta: " + responseBody,
                responseTrim.startsWith("[")
        );

        Assert.assertTrue(
                "Resposta deveria terminar com ']'. Resposta: " + responseBody,
                responseTrim.endsWith("]")
        );

        Assert.assertFalse(
                "Lista veio vazia. Resposta: " + responseBody,
                responseTrim.equals("[]")
        );
    }
}
