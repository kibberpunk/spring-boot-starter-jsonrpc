package com.kibberpunk.spring.boot.starter.jsonrpc.swagger;

import com.kibberpunk.spring.boot.starter.jsonrpc.context.properties.JsonRpcConfigurationProperties;
import lombok.NonNull;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;

import java.io.IOException;
import java.io.InputStream;

/**
 * JSON-RPC Swagger index page transformer.
 * Modifying Swagger JS script to send requests via JSON_RPC api
 *
 * @author kibberpunk
 */
public class JsonRpcSwaggerInitializerTransformer
        extends SwaggerIndexPageTransformer {

    /**
     * 'SwaggerUIBundle' section end. See swagger-initializer.js.
     */
    protected static final String SwaggerUIBundle_SECTION_END = "})";

    /**
     * Spring JSON-RPC path placeholder.
     */
    protected static final String springJsonRpcPath_PLACEHOLDER = "#{spring.json-rpc.consumer.path}";

    /**
     * requestInterceptor sub-section. See swagger-initializer.js.
     */
    protected static final String requestInterceptor_SUB_SECTION = """
            ,requestInterceptor: function (request) {
                if (request.loadRemoteConfig) {
                    return request;
                }
                if (request.loadSpec) {
                    return request;
                }
                if (request.body.includes('jsonrpc')) {
                    request.url = window.location.protocol +
                    '//'+ window.location.host + '#{spring.json-rpc.consumer.path}';
                }
                return request;
            }
            """;

    /**
     * See {@link JsonRpcConfigurationProperties.Consumer#getPath()}.
     */
    private final String path;

    /**
     * Instantiates a new Swagger index transformer.
     *
     * @param jsonRpcConfigurationProperties See {@link JsonRpcConfigurationProperties}
     * @param swaggerUiConfigProperties      See {@link SwaggerUiConfigProperties}
     * @param swaggerUiOAuthProperties       See {@link SwaggerUiOAuthProperties}
     * @param swaggerUiConfigParameters      See {@link SwaggerUiConfigParameters}
     * @param swaggerWelcomeCommon           See {@link SwaggerWelcomeCommon}
     * @param objectMapperProvider           See {@link ObjectMapperProvider}
     */
    public JsonRpcSwaggerInitializerTransformer(
            final @NonNull JsonRpcConfigurationProperties jsonRpcConfigurationProperties,
            final SwaggerUiConfigProperties swaggerUiConfigProperties,
            final SwaggerUiOAuthProperties swaggerUiOAuthProperties,
            final SwaggerUiConfigParameters swaggerUiConfigParameters,
            final SwaggerWelcomeCommon swaggerWelcomeCommon,
            final ObjectMapperProvider objectMapperProvider
    ) {
        super(
                swaggerUiConfigProperties,
                swaggerUiOAuthProperties,
                swaggerUiConfigParameters,
                swaggerWelcomeCommon,
                objectMapperProvider);
        this.path = jsonRpcConfigurationProperties.getConsumer().getPath();
    }

    /**
     * Default transformations string.
     *
     * @param inputStream the input stream
     * @return the string
     * @throws IOException the io exception
     */
    @Override
    protected String defaultTransformations(final InputStream inputStream) throws IOException {
        final var script = super.defaultTransformations(inputStream);
        final var point = script.indexOf(SwaggerUIBundle_SECTION_END);
        if (point < 0) {
            return script;
        }
        return script.substring(0, point)
                + requestInterceptor_SUB_SECTION.replace(springJsonRpcPath_PLACEHOLDER, this.path)
                + script.substring(point);
    }
}
