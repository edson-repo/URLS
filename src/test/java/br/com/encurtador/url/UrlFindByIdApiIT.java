package br.com.encurtador.url;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Test;

/**
 * Teste de Integração (IT) para o endpoint:
 * - GET /rest/api/url/find/{id}
 *
 * Estratégia:
 * 1) Cria um registro e obtém seu ID via /list
 * 2) Busca por ID e valida retorno com campos básicos (id, originalUrl, shortCode).
 */
public class UrlFindByIdApiIT {

    @Test
    public void deveBuscarUrlPorIdComSucesso() {

        // ---------- Arrange ----------
        String baseUrl = UrlTestSupport.getBaseUrl();

        Long id = UrlTestSupport.criarUrlEObterId(baseUrl, "find");
        Assert.assertNotNull("ID não deveria ser nulo.", id);

        String findEndpoint = baseUrl + "/rest/api/url/find/" + id;

        // ---------- Act ----------
        String responseBody =
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

        // ---------- Assert ----------
        JsonPath json = new JsonPath(responseBody);

        Long idRetornado = json.getLong("id");
        String originalUrl = json.getString("originalUrl");
        String shortCode = json.getString("shortCode");

        Assert.assertEquals("ID retornado diferente do esperado.", id, idRetornado);

        Assert.assertNotNull("originalUrl deveria existir.", originalUrl);
        Assert.assertTrue(
                "originalUrl deveria iniciar com http/https. Valor: " + originalUrl,
                originalUrl.startsWith("http://") || originalUrl.startsWith("https://")
        );

        Assert.assertNotNull("shortCode deveria existir.", shortCode);
        Assert.assertFalse("shortCode não deveria vir vazio.", shortCode.trim().isEmpty());
    }
}
