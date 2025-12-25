package br.com.encurtador.url;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

/**
 * Teste de Integração (IT) do endpoint:
 * POST /rest/api/url/save
 *
 * Observação:
 * O WildFly precisa estar em execução com o WAR devidamente deployado.
 */
public class UrlSaveApiIT {

    @Test
    public void deveCriarUrlEncurtadaComSucesso() {

        // ==========================================================
        // 1) Arrange
        // ==========================================================
        String baseUrl = UrlTestSupport.getBaseUrl();
        String endpoint = baseUrl + "/rest/api/url/save";

        // Alias único para evitar colisão no banco
        String aliasUnico = "save-" + UUID.randomUUID().toString().substring(0, 8);

        // Body JSON (montado manualmente para compatibilidade com Java 8)
        String body =
                "{"
                        + "\"originalUrl\":\"https://www.google.com\","
                        + "\"alias\":\"" + aliasUnico + "\""
                        + "}";

        // ==========================================================
        // 2) Act
        // ==========================================================
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

        // ==========================================================
        // 3) Assert
        // ==========================================================
        Assert.assertTrue(
                "Resposta não contém mensagem de sucesso. Resposta: " + responseBody,
                responseBody.toLowerCase().contains("sucesso")
        );
    }
}
