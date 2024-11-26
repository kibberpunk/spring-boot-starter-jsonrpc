package com.kibberpunk.spring.boot.starter.jsonrpc.exception;

import java.lang.reflect.Method;

/**
 * JSON-RPC parse {@link Method} parameters {@link RuntimeException}.
 *
 * @author kibberpunk
 */
public class JsonRpcParseMethodParameterException extends JsonRpcException {

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public JsonRpcParseMethodParameterException(final String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of
     * {@code cause}).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A {@code null} value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.4
     */
    public JsonRpcParseMethodParameterException(final Throwable cause) {
        super(cause);
    }
}
