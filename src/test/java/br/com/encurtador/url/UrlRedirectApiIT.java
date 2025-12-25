package br.com.encurtador.url;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.config.RestAssuredConfig.config;

/**
 * Teste de Integração (IT) para o endpoint:
 * - GET /rest/api/url/r/{code}
 *
 * Estratégia:
 * 1) Cria registro com alias único
 * 2) Chama /r/{alias} sem seguir redirect
 * 3) Valida:
 *    - status 307 (temporary redirect)
 *    - header Location aponta para a URL original
 */
public class UrlRedirectApiIT {

    @Test
    public void deveRedirecionarParaUrlOriginal() {

        // ---------- Arrange ----------
        String baseUrl = UrlTestSupport.getBaseUrl();

        String alias = UrlTestSupport.criarUrlComAliasUnico(baseUrl, "redirecionamento");
        String redirectEndpoint = baseUrl + "/rest/api/url/r/" + alias;

        // ---------- Act ----------
        // Desliga follow redirect para conseguirmos validar status e header Location
        io.restassured.response.Response response =
                RestAssured
                        .given()
                            .config(config().redirect(redirectConfig().followRedirects(false)))
                        .when()
                            .get(redirectEndpoint)
                        .andReturn();

        // ---------- Assert ----------
        int statusCode = response.getStatusCode();

        // Em geral o temporaryRedirect gera 307, mas dependendo do container pode ser 302.
        // Aceitamos ambos para deixar o teste robusto.
        Assert.assertTrue("Status esperado 307 ou 302, veio: " + statusCode, statusCode == 307 || statusCode == 302);

        String location = response.getHeader("Location");
        Assert.assertNotNull("Header Location deveria existir.", location);

        Assert.assertTrue("Location deveria conter a URL original. Location: " + location,
                location.contains("https://www.google.com"));
    }
}
