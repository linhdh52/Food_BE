package com.foodbe.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class StartupLinksLogger {
    private static final Logger log = LoggerFactory.getLogger(StartupLinksLogger.class);

    private final Environment env;
    private final ServletWebServerApplicationContext server;

    public StartupLinksLogger(Environment env, ServletWebServerApplicationContext server) {
        this.env = env;
        this.server = server;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        int port = server.getWebServer().getPort();
        String host = Optional.ofNullable(env.getProperty("server.address")).orElse("localhost");
        String contextPath = Optional.ofNullable(env.getProperty("server.servlet.context-path")).orElse("");
        String baseUrl = "http://" + host + ":" + port + contextPath;

        String swaggerPath = env.getProperty("springdoc.swagger-ui.path");
        String swaggerUrl = swaggerPath != null
                ? baseUrl + (swaggerPath.startsWith("/") ? swaggerPath : "/" + swaggerPath)
                : baseUrl + "/swagger-ui.html";

        String docsPath = env.getProperty("springdoc.api-docs.path", "/v3/api-docs");
        String docsUrl = baseUrl + (docsPath.startsWith("/") ? docsPath : "/" + docsPath);

        log.info("\n----------------------------------------------------------" +
                        "\n\tApplication is running!" +
                        "\n\tLocal:       {}" +
                        "\n\tSwagger UI:  {}" +
                        "\n\tAPI Docs:    {}" +
                        "\n----------------------------------------------------------",
                baseUrl, swaggerUrl, docsUrl);
    }
}
