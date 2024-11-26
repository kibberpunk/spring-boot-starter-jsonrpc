package com.kibberpunk.spring.boot.starter.jsonrpc.service.impl;

import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Request;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * JSON-RPC request context. This context is filled with class {@link AbstractJsonRpcService}.
 * You can create your own to store protocol-specific request data
 *
 * @param <Request> JSON-RPC request type. F.e. see {@link JsonRpc20Request}
 * @author kibberpunk
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class RequestContext<Request> {

    /**
     * Raw request body string.
     */
    private String body;

    /**
     * Request object received from body string.
     */
    private Request request;

    /**
     * See {@link HttpServletRequest}.
     */
    private HttpServletRequest httpServletRequest;
}
