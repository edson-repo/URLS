package br.com.encurtador.url;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Teste de Integração (IT) para o endpoint:
 * - GET /rest/api/url/list
 *
 * Estratégia:
 * 1) Cria uma URL (POST /save) com alias único (garante massa).
 * 2) Faz GET /list e valida que a lista não vem vazia.
 */
public class UrlListApiIT {

    @Test
    public void deveListarUrlsComSucesso() {

        // ---------- Arrange ----------
        String baseUrl = UrlTestSupport.getBaseUrl();

        // Garante que exista pelo menos 1 registro
        UrlTestSupport.criarUrlComAliasUnico(baseUrl, "list");

        String listEndpoint = baseUrl + "/rest/api/url/list";

        // ---------- Act ----------
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

        // ---------- Assert ----------
        Assert.assertTrue("Resposta deveria iniciar com '['. Resposta: " + responseBody, responseBody.trim().startsWith("["));
        Assert.assertTrue("Resposta deveria terminar com ']'. Resposta: " + responseBody, responseBody.trim().endsWith("]"));
        Assert.assertFalse("Lista veio vazia. Resposta: " + responseBody, responseBody.trim().equals("[]"));
    }
}
