package com.votrepackage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Autoriser toutes les ressources de l'application à accepter des requêtes de votre front-end
        registry.addMapping("/**")  // Remplace "/**" par le chemin spécifique si nécessaire
                .allowedOrigins("*") // Remplacez par l'URL de votre front-end (local ou en production)
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Liste des méthodes HTTP autorisées
                .allowedHeaders("*") // Autoriser tous les headers
                .allowCredentials(true); // Autoriser l'envoi de cookies si nécessaire
    }
}
