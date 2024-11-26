package com.kibberpunk.spring.boot.starter.jsonrpc.controller;

import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcController;
import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcMethod;
import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcRequestId;

/**
 * 'T001' (from Resident Evil) JSON-RPC controller
 *
 * @author kibberpunk
 */
@JsonRpcController
public class T001Controller extends AbstractTyrantController {

    /**
     * 'Attack' JSON-RPC method 1
     */
    @JsonRpcMethod
    public Request.Response attack(
            final Request request,
            final @JsonRpcRequestId String id) {
        return new Request.Response();
    }

    /**
     * 'Mutate' JSON-RPC method
     */
    @JsonRpcMethod
    public String mutate() {
        return null;
    }

    /**
     * 'Sleep' NONE JSON-RPC method (because tyrant never sleep)
     */
    public String sleep() {
        return null;
    }

}
