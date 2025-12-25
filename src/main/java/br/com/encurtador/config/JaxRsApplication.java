//package br.com.encurtador.config;
//
//import javax.ws.rs.ApplicationPath;
//import javax.ws.rs.core.Application;
//
///**
// * Ativador do JAX-RS no WildFly.
// * <p>
// * Define o prefixo base das rotas REST.
// * </p>
// */
//@ApplicationPath("/rest")
//public class JaxRsApplication extends Application {
//}



package br.com.encurtador.config;

import br.com.encurtador.url.UrlController;
import br.com.encurtador.auth.AuthController;


import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Ativador do JAX-RS no WildFly.
 * <p>
 * Registra explicitamente os recursos REST para garantir que todos
 * os endpoints fiquem disponíveis no deploy.
 * </p>
 */
@ApplicationPath("/rest")
public class JaxRsApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> resources = new HashSet<>();

        // Endpoints principais do encurtador
        resources.add(UrlController.class);

        resources.add(AuthController.class);

        return resources;
    }
}
