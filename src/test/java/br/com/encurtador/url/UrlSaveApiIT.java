package br.com.encurtador.url;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * Teste de Integração (IT) para o endpoint:
 * - POST /rest/api/url/save
 *
 * OBS: WildFly precisa estar rodando com o WAR deployado.
 */
public class UrlSaveApiIT {

    @Test
    public void deveCriarUrlEncurtadaComSucesso() {

        // ---------- Arrange ----------
        String baseUrl = UrlTestSupport.getBaseUrl();
        String endpoint = baseUrl + "/rest/api/url/save";

        // Alias único para evitar colisão no banco
        String aliasUnico = "save-" + UUID.randomUUID().toString().substring(0, 8);

// Body JSON (montado de forma simples e segura para Java 8)
        String body = "{"
                + "\"originalUrl\":\"https://www.google.com\","
                + "\"alias\":\"" + aliasUnico + "\""
                + "}";

        // ---------- Act ----------
        String responseBody =
                RestAssured
                        .given()
                            .contentType(ContentType.JSON)
                            .body(body)
                        .when()
                            .post(endpoint)
                        .then()
                            .statusCode(200)
                            .extract()
                            .asString();

        // ---------- Assert ----------
        Assert.assertTrue(
                "Resposta não contém mensagem de sucesso. Resposta: " + responseBody,
                responseBody.toLowerCase().contains("sucesso")
        );
    }
}
