package br.com.encurtador.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import br.com.encurtador.auth.AuthController;
import br.com.encurtador.url.UrlController;

/**
 * Classe responsável por ativar o JAX-RS no WildFly.
 *
 * Define o prefixo base das rotas REST e registra explicitamente
 * os controllers da aplicação.
 *
 * Essa abordagem garante previsibilidade no deploy e evita
 * problemas de descoberta automática de recursos.
 */
@ApplicationPath("/rest")
public class JaxRsApplication extends Application {

    /**
     * Registra manualmente todos os endpoints REST da aplicação.
     *
     * @return conjunto de classes que expõem recursos JAX-RS
     */
    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> resources = new HashSet<>();

        // Endpoints principais do sistema
        resources.add(UrlController.class);
        resources.add(AuthController.class);

        return resources;
    }
}
