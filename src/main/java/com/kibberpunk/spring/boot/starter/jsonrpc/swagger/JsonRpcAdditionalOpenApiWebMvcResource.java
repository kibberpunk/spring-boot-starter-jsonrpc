package com.kibberpunk.spring.boot.starter.jsonrpc.swagger;

import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcMethod;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;
import org.springdoc.webmvc.api.OpenApiWebMvcResource;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;

import java.util.Locale;
import java.util.Map;

/**
 * Json rpc additional open api web mvc resource.
 *
 * @author kibberpunk
 */
@Slf4j
public class JsonRpcAdditionalOpenApiWebMvcResource extends OpenApiWebMvcResource {

    /**
     * Constructor.
     *
     * @param openAPIBuilderObjectFactory See {@link ObjectFactory}
     * @param requestBuilder              See {@link AbstractRequestService}
     * @param responseBuilder             See {@link GenericResponseService}
     * @param operationParser             See {@link GenericResponseService}
     * @param springDocConfigProperties   See {@link OperationService}
     * @param springDocProviders          See {@link SpringDocConfigProperties}
     * @param springDocCustomizers        See {@link SpringDocCustomizers}
     */
    public JsonRpcAdditionalOpenApiWebMvcResource(
            final @NonNull ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory,
            final @NonNull AbstractRequestService requestBuilder,
            final @NonNull GenericResponseService responseBuilder,
            final @NonNull OperationService operationParser,
            final @NonNull SpringDocConfigProperties springDocConfigProperties,
            final @NonNull SpringDocProviders springDocProviders,
            final @NonNull SpringDocCustomizers springDocCustomizers) {
        super(openAPIBuilderObjectFactory,
                requestBuilder,
                responseBuilder,
                operationParser,
                springDocConfigProperties,
                springDocProviders,
                springDocCustomizers);
    }

    /**
     * Get path's.
     *
     * @param restControllers the find rest controllers
     * @param locale          the locale
     * @param openAPI         the open api
     */
    @Override
    protected void getPaths(
            final @NonNull Map<String, Object> restControllers,
            final @NonNull Locale locale,
            final @NonNull OpenAPI openAPI) {
        super.getPaths(restControllers, locale, openAPI);
    }

    /**
     * Is rest controller boolean.
     *
     * @param restControllers the rest controllers
     * @param handlerMethod   the handler method
     * @param operationPath   the operation path
     * @return the boolean
     */
    @Override
    protected boolean isRestController(
            final @NonNull Map<String, Object> restControllers,
            final @NonNull HandlerMethod handlerMethod,
            final @NonNull String operationPath) {
        return
                (containsResponseBody(handlerMethod)
                        || AnnotatedElementUtils.hasAnnotation(handlerMethod.getMethod(), JsonRpcMethod.class)
                        && restControllers.containsKey(handlerMethod.getBean().toString())
                        || isAdditionalRestController(handlerMethod.getBeanType())
                )
                        && springDocConfigProperties.isModelAndViewAllowed();
    }
}
