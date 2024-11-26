package com.kibberpunk.spring.boot.starter.jsonrpc.exception;

/**
 * JSON-RPC exception occurred while converting the original string of the prop to an object.
 *
 * @author kibberpunk
 */
public class JsonRpcRequestReceivingException extends JsonRpcException {

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
    public JsonRpcRequestReceivingException(final Throwable cause) {
        super(cause);
    }
}
