package com.kibberpunk.spring.boot.starter.jsonrpc.method;

import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcMethod;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.List;

/**
 * JSON-RPC {@link java.lang.reflect.Method} provider.
 *
 * @author kibberpunk
 */
public interface JsonRpcMethodProvider {

    /**
     * Get mapped by {@link JsonRpcMethod} by name.
     *
     * @param name Method name. Method name must not be empty
     * @return Mapped by {@link JsonRpcMethod} by name with controller object and {@link Method} parameters
     */
    Triple<Method, Object, List<MethodParameter>> get(@NonNull String name);
}
