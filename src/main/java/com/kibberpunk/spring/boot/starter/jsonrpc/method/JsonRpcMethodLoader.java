package com.kibberpunk.spring.boot.starter.jsonrpc.method;

import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcMethod;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.List;

/**
 * {@link Method} loader. Searches for {@link JsonRpcMethod} methods annotated in context classes.
 *
 * @author kibberpunk
 */
public interface JsonRpcMethodLoader {

    /**
     * Load method by specific name. See {@link JsonRpcMethodNameResolver}.
     *
     * @param name Specific method name. See {@link JsonRpcMethodNameResolver}
     * @return {@link Method}, controller object and {@link Method} parameters {@link Triple}
     */
    Triple<Method, Object, List<MethodParameter>> get(@NonNull String name);
}
