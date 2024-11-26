package com.kibberpunk.spring.boot.starter.jsonrpc.service.impl;

import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Request;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Response;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodProvider;
import com.kibberpunk.spring.boot.starter.jsonrpc.service.JsonRpcProtocolSupport;
import lombok.NonNull;

/**
 * JSON-RPC processor.
 *
 * @author kibberpunk
 */
public class JsonRpc20ProtocolService
        extends AbstractJsonRpcService<JsonRpc20Request, JsonRpc20Response, JsonRpc20RequestContext> {

    /**
     * Constructor.
     *
     * @param support        See {@link JsonRpc20ProtocolSupportImpl}
     * @param methodProvider See {@link JsonRpcMethodProvider}
     */
    public JsonRpc20ProtocolService(
            final @NonNull JsonRpcProtocolSupport
                    <JsonRpc20Request, JsonRpc20Response, JsonRpc20RequestContext> support,
            final @NonNull JsonRpcMethodProvider methodProvider) {
        super(support, methodProvider);
    }
}
