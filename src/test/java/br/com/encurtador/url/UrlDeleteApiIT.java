package br.com.encurtador.url;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

/**
 * Teste de Integração (IT) do endpoint:
 * DELETE /rest/api/url/delete/{id}
 *
 * Estratégia:
 * 1) Cria um registro e obtém o ID
 * 2) Deleta o registro
 * 3) Tenta buscar por ID e espera erro (400) com "não encontrado"
 *
 * Observação:
 * Considera o comportamento atual do controller, que retorna 400 quando o findById lança Exception.
 */
public class UrlDeleteApiIT {

    @Test
    public void deveDeletarUrlComSucesso() {

        // ==========================================================
        // 1) Arrange
        // ==========================================================
        String baseUrl = UrlTestSupport.getBaseUrl();

        Long id = UrlTestSupport.criarUrlEObterId(baseUrl, "del");
        Assert.assertNotNull("ID não deveria ser nulo.", id);

        String deleteEndpoint = baseUrl + "/rest/api/url/delete/" + id;

        // ==========================================================
        // 2) Act
        // ==========================================================
        String deleteResponse =
                RestAssured
                        .given()
                        .accept(ContentType.JSON)
                        .when()
                        .delete(deleteEndpoint)
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract()
                        .asString();

        String deleteResponseLower = deleteResponse.toLowerCase();

        Assert.assertTrue(
                "Resposta do delete não parece sucesso. Body: " + deleteResponse,
                deleteResponseLower.contains("sucesso")
                        || deleteResponseLower.contains("removid")
        );

        // ==========================================================
        // 3) Assert (buscar após delete deve falhar)
        // ==========================================================
        String findEndpoint = baseUrl + "/rest/api/url/find/" + id;

        String findAfterDelete =
                RestAssured
                        .given()
                        .accept(ContentType.JSON)
                        .when()
                        .get(findEndpoint)
                        .then()
                        .statusCode(400)
                        .contentType(ContentType.JSON)
                        .extract()
                        .asString();

        String findAfterDeleteLower = findAfterDelete.toLowerCase();

        Assert.assertTrue(
                "Esperado 'não encontrado' após delete. Body: " + findAfterDelete,
                findAfterDeleteLower.contains("não encontrado")
                        || findAfterDeleteLower.contains("nao encontrado")
        );
    }
}
