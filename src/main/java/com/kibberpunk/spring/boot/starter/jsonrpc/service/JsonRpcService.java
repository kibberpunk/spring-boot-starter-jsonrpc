package com.kibberpunk.spring.boot.starter.jsonrpc.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;

/**
 * JSON-RPC consumer service.
 *
 * @author kibberpunk
 */
public interface JsonRpcService {

    /**
     * Process request.
     *
     * @param body               Raw request body string
     * @param httpServletRequest {@link HttpServletRequest}
     * @return Response after request processing
     */
    Object process(String body, @NonNull HttpServletRequest httpServletRequest);
}
