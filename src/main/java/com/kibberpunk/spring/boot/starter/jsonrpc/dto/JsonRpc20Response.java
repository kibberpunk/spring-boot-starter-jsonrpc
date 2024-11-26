package com.kibberpunk.spring.boot.starter.jsonrpc.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * JSON-RPC response.
 * See <a href="https://www.jsonrpc.org/specification#response_object">label</a>
 *
 * @author kibberpunk
 */
@Getter
@Setter
@Accessors(chain = true)
@Builder
public class JsonRpc20Response extends AbstractJsonRpc20Object {

    /**
     * Create error response with id and {@link JsonRpcError.Code}.
     *
     * @param id   Request id
     * @param code Error {@link JsonRpcError.Code}
     * @return Error response with id and {@link JsonRpcError.Code}
     */
    @NonNull
    public static JsonRpc20Response error(final JsonNode id, final @NonNull JsonRpcError.Code code) {
        return error(id, code, null);
    }

    /**
     * Create error response with id, {@link JsonRpcError.Code} and specific error data.
     *
     * @param id   Request id
     * @param code Error {@link JsonRpcError.Code}
     * @param data Optional error data
     * @return Error response with id, {@link JsonRpcError.Code} and specific error data.
     */
    @NonNull
    public static JsonRpc20Response error(final JsonNode id, final @NonNull JsonRpcError.Code code, final Object data) {
        return builder().id(id)
                .error(new JsonRpcError(code.getCode(), code.getMessage(), data)).build();
    }

    /**
     * This member is REQUIRED.
     * It MUST be the same as the value of the id member in the Request Object.
     * If there was an error in detecting the id in the Request object
     * (e.g. Parse error/Invalid Request), it MUST be Null.
     */
    private JsonNode id;

    /**
     * This member is REQUIRED on success.
     * This member MUST NOT exist if there was an error invoking the method.
     * The value of this member is determined by the method invoked on the Server.
     */
    private Object result;

    /**
     * This member is REQUIRED on error.
     * This member MUST NOT exist if there was no error triggered during invocation.
     * The value for this member MUST be an Object as defined in section 5.1.
     */
    private JsonRpcError error;
}
