package br.com.encurtador.url;

import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.config.RestAssuredConfig.config;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;

/**
 * Teste de Integração (IT) do endpoint:
 * GET /rest/api/url/r/{code}
 *
 * Estratégia:
 * 1) Cria um registro com alias único
 * 2) Chama /r/{alias} sem seguir o redirect
 * 3) Valida:
 *    - status 307 (ou 302 dependendo do container)
 *    - header Location aponta para a URL original
 */
public class UrlRedirectApiIT {

    @Test
    public void deveRedirecionarParaUrlOriginal() {

        // ==========================================================
        // 1) Arrange
        // ==========================================================
        String baseUrl = UrlTestSupport.getBaseUrl();

        String alias = UrlTestSupport.criarUrlComAliasUnico(baseUrl, "redirecionamento");
        String redirectEndpoint = baseUrl + "/rest/api/url/r/" + alias;

        // ==========================================================
        // 2) Act
        // ==========================================================
        // Desliga follow redirects para validar status e header Location
        io.restassured.response.Response response =
                RestAssured
                        .given()
                        .config(config().redirect(redirectConfig().followRedirects(false)))
                        .when()
                        .get(redirectEndpoint)
                        .andReturn();

        // ==========================================================
        // 3) Assert
        // ==========================================================
        int statusCode = response.getStatusCode();

        // Em geral o temporary redirect é 307, mas dependendo do container pode ser 302.
        Assert.assertTrue(
                "Status esperado 307 ou 302, veio: " + statusCode,
                statusCode == 307 || statusCode == 302
        );

        String location = response.getHeader("Location");
        Assert.assertNotNull("Header Location deveria existir.", location);

        Assert.assertTrue(
                "Location deveria conter a URL original. Location: " + location,
                location.contains("https://www.google.com")
        );
    }
}
