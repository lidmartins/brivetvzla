package com.brivetvzla.backend.config;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registra el módulo Hibernate6Module en Jackson para que sepa
 * serializar proxies lazy de Hibernate (como Role en Usuario.role)
 * sin lanzar InvalidDefinitionException.
 *
 * Sin esto, cualquier entity con una relacion @ManyToOne(LAZY) falla
 * al serializarse a JSON porque Jackson intenta serializar el proxy
 * de Hibernate (ByteBuddyInterceptor) en vez del objeto real.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Hibernate6Module hibernate6Module() {
        Hibernate6Module module = new Hibernate6Module();
        // No forzar la carga de relaciones lazy no inicializadas;
        // si no fueron cargadas, Jackson las serializa como null
        // en vez de lanzar una LazyInitializationException.
        module.disable(Hibernate6Module.Feature.USE_TRANSIENT_ANNOTATION);
        return module;
    }
}