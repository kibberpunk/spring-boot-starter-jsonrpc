package com.kibberpunk.spring.boot.starter.jsonrpc.dto;

import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcRequestId;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * JSON-RPC error object.
 * See <a href="https://www.jsonrpc.org/specification#error_object">label</a>
 *
 * @author kibberpunk
 */
@Getter
@Setter
@Builder
public class JsonRpcError {

    /**
     * -32700             Parse error        Invalid JSON was received by the server.
     * An error occurred on the server while parsing the JSON text.
     * -32600             Invalid Request    The JSON sent is not a valid Request object.
     * -32601             Method not found   The method does not exist / is not available.
     * -32602             Invalid params     Invalid method parameter(s).
     * -32603             Internal error     Internal JSON-RPC error.
     * -32000 to -32099   Server error       Reserved for implementation-defined server-errors.
     * The error codes from and including -32768 to -32000 are reserved for pre-defined errors.
     * Any code within this range, but not defined explicitly below is reserved for future use
     */
    @Getter
    @RequiredArgsConstructor
    public enum Code {

        /**
         * -32700             Parse error                Invalid JSON was received by the server.
         */
        PARSE_ERROR(-32700, "Parse error"),

        /**
         * -32600             Invalid Request            The JSON sent is not a valid Request object.
         */
        INVALID_REQUEST(-32600, "Invalid Request"),

        /**
         * -32601             Method not found           The method does not exist / is not available.
         */
        METHOD_NOT_FOUND(-32601, "Method not found"),

        /**
         * -32602             Invalid params             Invalid method parameter(s).
         */
        INVALID_PARAMS(-32602, "Invalid params"),

        /**
         * -32603             Internal error             Internal JSON-RPC error.
         */
        INTERNAL_ERROR(-32603, "Internal error"),

        /**
         * -32768             Request id is empty        If the {@link JsonRpcRequestId#required()} is true.
         */
        REQUEST_ID_IS_EMPTY(-32769, "Request id is empty");

        /**
         * A Number that indicates the error type that occurred. This MUST be an integer.
         * Code message meaning. @see {@link Code}
         */
        private final int code;

        /**
         * Error code message.
         */
        private final String message;
    }

    /**
     * A Number that indicates the error type that occurred. This MUST be an integer.
     * Code message meaning. @see {@link Code}
     */
    private int code;

    /**
     * A String providing a short description of the error.
     * The message SHOULD be limited to a concise single sentence.
     */
    private String message;

    /**
     * A Primitive or Structured value that contains additional information about the error.
     * This may be omitted.
     * The value of this member is defined by the Server (e.g. detailed error information, nested errors etc.).
     */
    private Object data;
}
