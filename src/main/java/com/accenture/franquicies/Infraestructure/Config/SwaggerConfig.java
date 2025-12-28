package com.accenture.franquicies.Infraestructure.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Franquicias API")
                        .version("1.0.0")
                        .description("API REST para la gestión de franquicias, sucursales y productos. " +
                                "Permite realizar operaciones CRUD sobre franquicias, sus sucursales asociadas " +
                                "y los productos de cada sucursal."));

                        
    }
}
