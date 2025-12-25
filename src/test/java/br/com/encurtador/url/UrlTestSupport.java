package br.com.encurtador.url;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Classe utilitária para padronizar os testes de integração.
 * <p>
 * Objetivo:
 * - Evitar repetição de código (criação de dados, extração de ID, etc.)
 * - Deixar os testes mais legíveis e "didáticos"
 * </p>
 */
public final class UrlTestSupport {

    private UrlTestSupport() {
        // utilitário
    }

    /**
     * Obtém baseUrl do sistema e valida se foi informado.
     */
    public static String getBaseUrl() {
        String baseUrl = System.getProperty("baseUrl");
        Assert.assertNotNull("Informe -DbaseUrl (ex: http://localhost:8080/encurtador-url)", baseUrl);
        return baseUrl;
    }

    /**
     * Cria uma URL (POST /save) com alias único e retorna o alias usado.
     */
    public static String criarUrlComAliasUnico(String baseUrl, String prefixoAlias) {

        String aliasUnico = prefixoAlias + "-" + UUID.randomUUID().toString().substring(0, 8);

        String saveEndpoint = baseUrl + "/rest/api/url/save";

        // Usando JSON explícito para evitar dependência de POJO/Map e manter clareza do payload
// Usando JSON explícito para evitar dependência de POJO/Map e manter clareza do payload
        String body = "{"
                + "\"originalUrl\":\"https://www.google.com\","
                + "\"alias\":\"" + aliasUnico + "\""
                + "}";

        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(body)
                .when()
                    .post(saveEndpoint)
                .then()
                    .statusCode(200);

        return aliasUnico;
    }

    /**
     * Busca o ID de uma URL pelo alias, consultando GET /list e varrendo o JSON.
     * <p>
     * Retorna:
     * - id (Long) se encontrar
     * - falha o teste se não encontrar
     * </p>
     */
    public static Long buscarIdPorAlias(String baseUrl, String alias) {

        String listEndpoint = baseUrl + "/rest/api/url/list";

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

        JsonPath jsonPath = new JsonPath(responseBody);

        List<Map<String, Object>> itens = jsonPath.getList("$");
        Assert.assertNotNull("Resposta /list deveria ser uma lista JSON.", itens);

        for (Map<String, Object> item : itens) {
            Object aliasObj = item.get("alias");
            if (aliasObj != null && alias.equals(String.valueOf(aliasObj))) {

                Object idObj = item.get("id");
                Assert.assertNotNull("Item encontrado por alias, mas sem 'id'. Item: " + item, idObj);

                // RestAssured normalmente lê números como Integer
                if (idObj instanceof Integer) {
                    return ((Integer) idObj).longValue();
                }
                if (idObj instanceof Long) {
                    return (Long) idObj;
                }
                return Long.valueOf(String.valueOf(idObj));
            }
        }

        Assert.fail("Não encontrei nenhum registro com alias '" + alias + "' no endpoint /list. Body: " + responseBody);
        return null;
    }

    /**
     * Cria uma URL e retorna o ID criado (via /list).
     */
    public static Long criarUrlEObterId(String baseUrl, String prefixoAlias) {
        String alias = criarUrlComAliasUnico(baseUrl, prefixoAlias);
        return buscarIdPorAlias(baseUrl, alias);
    }
}
