package com.kibberpunk.spring.boot.starter.jsonrpc.method.impl;

import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcMethod;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodLoader;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;


/**
 * {@link JsonRpcMethodProvider} implementation.
 *
 * @author kibberpunk
 */
@Slf4j
@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class JsonRpcMethodProviderImpl implements JsonRpcMethodProvider {

    /**
     * See {@link JsonRpcMethodLoader}.
     */
    private final JsonRpcMethodLoader loader;

    /**
     * Get mapped by {@link JsonRpcMethod} by name.
     *
     * @param name Method name. Method name must not be empty
     * @return Mapped by {@link JsonRpcMethod} by name with controller object and {@link Method} parameters
     */
    @Override
    public Triple<Method, Object, List<MethodParameter>> get(final @NonNull String name) {
        if (isBlank(name)) {
            log.warn("Method name should be not blank");
            return null;
        }
        return loader.get(name);
    }
}
