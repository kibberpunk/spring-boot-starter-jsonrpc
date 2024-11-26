package com.kibberpunk.spring.boot.starter.jsonrpc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kibberpunk.spring.boot.starter.jsonrpc.utils.JsonRpcUtils;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract JSON-RPC transfer object.
 *
 * @author kibberpunk
 */
@Getter
@Setter
public abstract class AbstractJsonRpc20Object {

    /**
     * A String specifying the version of the JSON-RPC protocol. MUST be exactly "2.0".
     */
    @JsonProperty("jsonrpc")
    @Pattern(regexp = "^2\\.0$", message = "MUST be exactly \"2.0")
    private String jsonRpc = JsonRpcUtils.VERSION;
}
