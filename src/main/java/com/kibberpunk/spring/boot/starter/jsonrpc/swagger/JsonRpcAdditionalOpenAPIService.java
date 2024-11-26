package com.kibberpunk.spring.boot.starter.jsonrpc.swagger;

import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcController;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.NonNull;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.SecurityService;
import org.springdoc.core.utils.PropertyResolverUtils;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Json rpc additional open api service.
 *
 * @author kibberpunk
 */
public class JsonRpcAdditionalOpenAPIService extends OpenAPIService {

    /**
     * Instantiates a new Open api builder.
     *
     * @param openAPI                   the open api
     * @param securityParser            the security parser
     * @param springDocConfigProperties the spring doc config properties
     * @param propertyResolverUtils     the property resolver utils
     * @param openApiBuilderCustomizers the open api builder customisers
     * @param serverBaseUrlCustomizers  the server base url customizers
     * @param javadocProvider           the javadoc provider
     */
    public JsonRpcAdditionalOpenAPIService(
            final @NonNull Optional<OpenAPI> openAPI,
            final @NonNull SecurityService securityParser,
            final @NonNull SpringDocConfigProperties springDocConfigProperties,
            final @NonNull PropertyResolverUtils propertyResolverUtils,
            final @NonNull Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomizers,
            final @NonNull Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomizers,
            final @NonNull Optional<JavadocProvider> javadocProvider) {
        super(openAPI, securityParser, springDocConfigProperties, propertyResolverUtils,
                openApiBuilderCustomizers, serverBaseUrlCustomizers, javadocProvider);
    }

    /**
     * Build additional {@link JsonRpcController} annotation.
     *
     * @param locale the locale
     * @return the open api
     */
    @Override
    public OpenAPI build(final Locale locale) {
        getMappingsMap().putAll(getContext().getBeansWithAnnotation(JsonRpcController.class));
        return super.build(locale);
    }
}
