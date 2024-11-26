package com.kibberpunk.spring.boot.starter.jsonrpc.exception;

import com.kibberpunk.spring.boot.starter.jsonrpc.utils.FormatUtils;

import java.lang.reflect.Method;

/**
 * JSON-RPC {@link Method} not found {@link RuntimeException}.
 *
 * @author kibberpunk
 */
public class JsonRpcMethodNotFoundException extends JsonRpcException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public JsonRpcMethodNotFoundException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param name Method name
     */
    public JsonRpcMethodNotFoundException(final String name) {
        super(FormatUtils.format("Not found mapped name by name [{}]", name));
    }
}
