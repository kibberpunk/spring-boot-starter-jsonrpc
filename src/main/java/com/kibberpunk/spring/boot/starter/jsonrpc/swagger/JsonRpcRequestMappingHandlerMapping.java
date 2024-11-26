package com.kibberpunk.spring.boot.starter.jsonrpc.swagger;

import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcController;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodNameResolver;
import lombok.NonNull;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * JSON-RPC request mapping handler mapping.
 *
 * @author kibberpunk
 */
public class JsonRpcRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    /**
     * See {@link JsonRpcMethodNameResolver}.
     */
    private final JsonRpcMethodNameResolver methodNameResolver;

    /**
     * Constructor.
     *
     * @param jsonRpcPathPatternParser See {@link JsonRpcPathPatternParser}
     * @param jsonRpcMethodNameResolver       See {@link JsonRpcMethodNameResolver}
     */
    public JsonRpcRequestMappingHandlerMapping(
            final @NonNull JsonRpcPathPatternParser jsonRpcPathPatternParser,
            final @NonNull JsonRpcMethodNameResolver jsonRpcMethodNameResolver) {
        setPatternParser(jsonRpcPathPatternParser);
        this.methodNameResolver = jsonRpcMethodNameResolver;
    }

    /**
     * Determine the names of candidate beans in the application context.
     *
     * @see #setDetectHandlerMethodsInAncestorContexts
     * @see BeanFactoryUtils#beanNamesForTypeIncludingAncestors
     * @since 5.1
     */
    @Override
    protected String @NonNull [] getCandidateBeanNames() {
        return Optional.ofNullable(super.getApplicationContext())
                .map(context -> context.getBeanNamesForAnnotation(JsonRpcController.class))
                .orElse(new String[]{});
    }

    /**
     * {@inheritDoc}
     * <p>Expects a handler to have a type-level @{@link JsonRpcController} annotation.
     *
     * @param beanType
     */
    @Override
    protected boolean isHandler(final @NonNull Class<?> beanType) {
        return AnnotatedElementUtils.hasAnnotation(beanType, JsonRpcController.class);
    }

    /**
     * Get {@link RequestMappingInfo} to JSON-RPC method.
     *
     * @param method      JSON-RPC method to provide a mapping for
     * @param handlerType the handler type, possibly a subtype of the JSON-RPC method's declaring class
     * @return {@link RequestMappingInfo}
     */
    @Override
    protected RequestMappingInfo getMappingForMethod(
            final @NonNull Method method,
            final @NonNull Class<?> handlerType) {
        return Optional.ofNullable(super.getApplicationContext())
                .map(context -> context.getBeanNamesForType(handlerType))
                .map(beanNames -> methodNameResolver.resolve(
                        beanNames,
                        method.getName(),
                        handlerType,
                        super.getApplicationContext().getBean(handlerType),
                        method)
                )
                .map(name -> RequestMappingInfo
                        .paths(name)
                        .methods(RequestMethod.POST)
                        .params()
                        .headers()
                        .consumes()
                        .produces()
                        .mappingName(name)
                )
                .map(builder -> builder.options(getBuilderConfiguration()))
                .map(RequestMappingInfo.Builder::build)
                .orElseThrow();
    }
}
