package br.com.encurtador.url;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

/**
 * Classe utilitária para padronizar os testes de integração (IT).
 *
 * Objetivo:
 * - Evitar repetição de código (criação de massa, extração de ID, etc.)
 * - Manter os testes curtos, legíveis e didáticos
 *
 * Observação:
 * - Métodos estáticos por ser um "helper" de testes.
 * - Construtor privado para impedir instanciação.
 */
public final class UrlTestSupport {

    private UrlTestSupport() {
        // Classe utilitária: não deve ser instanciada
    }

    /**
     * Obtém a baseUrl do sistema via JVM arg (-DbaseUrl).
     *
     * Exemplo:
     * -DbaseUrl=http://localhost:8080/encurtador-url
     *
     * @return baseUrl validada (não nula)
     */
    public static String getBaseUrl() {
        String baseUrl = System.getProperty("baseUrl");

        Assert.assertNotNull(
                "Informe -DbaseUrl (ex: http://localhost:8080/encurtador-url)",
                baseUrl
        );

        return baseUrl;
    }

    /**
     * Cria uma URL (POST /save) com alias único e retorna o alias utilizado.
     *
     * @param baseUrl      base do sistema (ex: http://localhost:8080/encurtador-url)
     * @param prefixoAlias prefixo para ajudar na identificação do teste
     * @return alias único criado
     */
    public static String criarUrlComAliasUnico(String baseUrl, String prefixoAlias) {

        String aliasUnico = prefixoAlias + "-" + UUID.randomUUID().toString().substring(0, 8);
        String saveEndpoint = baseUrl + "/rest/api/url/save";

        // JSON explícito para manter clareza do payload e evitar dependência de POJO/Map
        String body =
                "{"
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
     * Busca o ID de uma URL pelo alias consultando GET /list e varrendo o JSON.
     *
     * Retorno:
     * - ID (Long) se encontrar
     * - Falha o teste se não encontrar
     *
     * @param baseUrl base do sistema
     * @param alias   alias utilizado no registro
     * @return id do registro encontrado
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
            boolean encontrouAlias = (aliasObj != null) && alias.equals(String.valueOf(aliasObj));

            if (!encontrouAlias) {
                continue;
            }

            Object idObj = item.get("id");
            Assert.assertNotNull("Item encontrado por alias, mas sem 'id'. Item: " + item, idObj);

            // O RestAssured pode ler números como Integer. Fazemos a conversão com segurança.
            if (idObj instanceof Integer) {
                return ((Integer) idObj).longValue();
            }

            if (idObj instanceof Long) {
                return (Long) idObj;
            }

            return Long.valueOf(String.valueOf(idObj));
        }

        Assert.fail(
                "Não encontrei nenhum registro com alias '" + alias + "' no endpoint /list. Body: " + responseBody
        );

        return null; // nunca chega aqui (Assert.fail), mas mantém assinatura e compilação
    }

    /**
     * Cria uma URL e retorna o ID criado (obtido via /list).
     *
     * @param baseUrl      base do sistema
     * @param prefixoAlias prefixo para o alias único
     * @return id do registro recém-criado
     */
    public static Long criarUrlEObterId(String baseUrl, String prefixoAlias) {
        String alias = criarUrlComAliasUnico(baseUrl, prefixoAlias);
        return buscarIdPorAlias(baseUrl, alias);
    }
}
