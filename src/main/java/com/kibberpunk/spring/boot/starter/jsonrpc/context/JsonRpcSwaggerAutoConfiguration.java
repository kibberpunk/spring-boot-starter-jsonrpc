package com.kibberpunk.spring.boot.starter.jsonrpc.context;

import com.kibberpunk.spring.boot.starter.jsonrpc.context.properties.JsonRpcConfigurationProperties;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodNameResolver;
import com.kibberpunk.spring.boot.starter.jsonrpc.swagger.JsonRpcAdditionalOpenAPIService;
import com.kibberpunk.spring.boot.starter.jsonrpc.swagger.JsonRpcAdditionalOpenApiWebMvcResource;
import com.kibberpunk.spring.boot.starter.jsonrpc.swagger.JsonRpcPathPatternParser;
import com.kibberpunk.spring.boot.starter.jsonrpc.swagger.JsonRpcRequestMappingHandlerMapping;
import com.kibberpunk.spring.boot.starter.jsonrpc.swagger.JsonRpcSwaggerInitializerTransformer;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.NonNull;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;
import org.springdoc.core.service.SecurityService;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springdoc.webmvc.api.OpenApiWebMvcResource;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Optional;

/**
 * JSON-RPC Swagger auto configuration.
 *
 * @author kibberpunk
 */
@Profile("Swagger")
@Configuration
public class JsonRpcSwaggerAutoConfiguration {

    /**
     * Create {@link OpenAPIService}.
     *
     * @param openAPI                   See {@link OpenAPI}
     * @param securityService           See {@link SecurityService}
     * @param springDocConfigProperties See {@link SpringDocConfigProperties}
     * @param propertyResolverUtils     See {@link PropertyResolverUtils}
     * @param openApiBuilderCustomizers See {@link OpenApiBuilderCustomizer} list
     * @param serverBaseUrlCustomizers  See {@link ServerBaseUrlCustomizer} list
     * @param javadocProvider           See {@link JavadocProvider}
     * @return See {@link JsonRpcAdditionalOpenAPIService}
     */
    @Bean
    @ConditionalOnMissingBean
    @Lazy(false)
    OpenAPIService openAPIBuilder(
            final Optional<OpenAPI> openAPI,
            final SecurityService securityService,
            final SpringDocConfigProperties springDocConfigProperties,
            final PropertyResolverUtils propertyResolverUtils,
            final Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomizers,
            final Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomizers,
            final Optional<JavadocProvider> javadocProvider
    ) {
        return new JsonRpcAdditionalOpenAPIService(
                openAPI,
                securityService,
                springDocConfigProperties,
                propertyResolverUtils,
                openApiBuilderCustomizers,
                serverBaseUrlCustomizers,
                javadocProvider
        );
    }

    /**
     * Create {@link OpenApiWebMvcResource}.
     *
     * @param openAPIBuilderObjectFactory See {@link ObjectFactory}
     * @param requestService              See {@link AbstractRequestService}
     * @param responseBuilder             See {@link GenericResponseService}
     * @param operationService            See {@link OperationService}
     * @param springDocConfigProperties   See {@link SpringDocConfigProperties}
     * @param springDocProviders          See {@link SpringDocProviders}
     * @param springDocCustomizers        See {@link SpringDocCustomizers}
     * @return See {@link OpenApiWebMvcResource}
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnExpression
            ("(${springdoc.use-management-port:false} == false ) and ${springdoc.enable-default-api-docs:true}")
    @Lazy(false)
    public OpenApiWebMvcResource openApiResource(
            final ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory,
            final AbstractRequestService requestService,
            final GenericResponseService responseBuilder,
            final OperationService operationService,
            final SpringDocConfigProperties springDocConfigProperties,
            final SpringDocProviders springDocProviders,
            final SpringDocCustomizers springDocCustomizers
    ) {
        return new JsonRpcAdditionalOpenApiWebMvcResource(
                openAPIBuilderObjectFactory,
                requestService,
                responseBuilder,
                operationService,
                springDocConfigProperties,
                springDocProviders,
                springDocCustomizers
        );
    }

    /**
     * Create {@link SwaggerIndexPageTransformer}.
     *
     * @param jsonRpcConfigurationProperties See {@link JsonRpcConfigurationProperties}
     * @param swaggerUiConfigProperties      See {@link SwaggerUiConfigProperties}
     * @param swaggerUiOAuthProperties       See {@link SwaggerUiOAuthProperties}
     * @param swaggerUiConfigParameters      See {@link SwaggerUiConfigParameters}
     * @param swaggerWelcomeCommon           See {@link SwaggerWelcomeCommon}
     * @param objectMapperProvider           See {@link ObjectMapperProvider}
     * @return See {@link JsonRpcSwaggerInitializerTransformer}
     */
    @Bean
    @ConditionalOnMissingBean
    public SwaggerIndexPageTransformer swaggerIndexPageTransformer(
            final @NonNull JsonRpcConfigurationProperties jsonRpcConfigurationProperties,
            final SwaggerUiConfigProperties swaggerUiConfigProperties,
            final SwaggerUiOAuthProperties swaggerUiOAuthProperties,
            final SwaggerUiConfigParameters swaggerUiConfigParameters,
            final SwaggerWelcomeCommon swaggerWelcomeCommon,
            final ObjectMapperProvider objectMapperProvider
    ) {
        return new JsonRpcSwaggerInitializerTransformer(
                jsonRpcConfigurationProperties,
                swaggerUiConfigProperties,
                swaggerUiOAuthProperties,
                swaggerUiConfigParameters,
                swaggerWelcomeCommon,
                objectMapperProvider
        );
    }

    /**
     * Create {@link JsonRpcPathPatternParser}.
     *
     * @return {@link JsonRpcPathPatternParser}
     */
    @Bean
    @ConditionalOnMissingBean
    public JsonRpcPathPatternParser jsonRpcPathPatternParser() {
        return new JsonRpcPathPatternParser();
    }

    /**
     * Create {@link JsonRpcRequestMappingHandlerMapping}.
     *
     * @param jsonRpcPathPatternParser  {@link JsonRpcPathPatternParser}
     * @param jsonRpcMethodNameResolver See {@link JsonRpcMethodNameResolver}
     * @return {@link JsonRpcRequestMappingHandlerMapping}
     */
    @Bean
    @ConditionalOnMissingBean
    public JsonRpcRequestMappingHandlerMapping jsonRpcRequestMappingHandlerMapping(
            final JsonRpcPathPatternParser jsonRpcPathPatternParser,
            final JsonRpcMethodNameResolver jsonRpcMethodNameResolver) {
        return new JsonRpcRequestMappingHandlerMapping(jsonRpcPathPatternParser, jsonRpcMethodNameResolver);
    }
}
