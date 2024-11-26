package com.kibberpunk.spring.boot.starter.jsonrpc.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * JSON-RPC request object.
 * See <a href="https://www.jsonrpc.org/specification#request_object">label</a>
 *
 * @author kibberpunk
 */
@Getter
@Setter
@Accessors(chain = true)
public class JsonRpc20Request extends AbstractJsonRpc20Object {

    /**
     * An identifier established by the Client that MUST contain a String, Number, or NULL value if included.
     * If it is not included it is assumed to be a notification.
     * The value SHOULD normally not be Null and Numbers SHOULD NOT contain fractional parts
     */
    private JsonNode id;

    /**
     * A String containing the name of the method to be invoked.
     * Method names that begin with the word rpc followed by a period character (U+002E or ASCII 46)
     * are reserved for rpc-internal methods and extensions and MUST NOT be used for anything else
     */
    private String method;

    /**
     * A Structured value that holds the parameter values to be used during the invocation of the method.
     * This member MAY be omitted.
     */
    private JsonNode params;
}
