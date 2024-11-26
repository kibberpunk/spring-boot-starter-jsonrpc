package com.kibberpunk.spring.boot.starter.jsonrpc.method;

import lombok.NonNull;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Method;

/**
 * JSON-RPC method name resolver.
 *
 * @author kibberpunk
 */
public interface JsonRpcMethodNameResolver {

    /**
     * Resolve method name.
     *
     * @param controllerBeanNames Controller bean names
     * @param methodName          Method name
     * @param controllerType      Controller {@link ParameterizedTypeReference}
     * @param controller          Controller object
     * @param method              Controller method
     * @param <T>                 Controller type
     * @return Method name generated based on the received data
     */
    @NonNull
    <T> String resolve(
            @NonNull String[] controllerBeanNames,
            @NonNull String methodName,
            @NonNull Class<T> controllerType,
            @NonNull Object controller,
            @NonNull Method method);
}
