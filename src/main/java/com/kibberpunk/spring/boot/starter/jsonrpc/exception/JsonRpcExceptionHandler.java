package com.kibberpunk.spring.boot.starter.jsonrpc.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Request;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Response;
import com.kibberpunk.spring.boot.starter.jsonrpc.service.impl.RequestContext;
import lombok.NonNull;
import org.springframework.core.convert.ConversionService;

/**
 * JSON-RPC exception handler.
 *
 * @param <Request>  JSON-RPC request type. F.e. see {@link JsonRpc20Request}
 * @param <Response> JSON-RPC response type. F.e. see {@link JsonRpc20Response}
 * @param <C>        Request context type. See {@link RequestContext}
 * @author kibberpunk
 */
public interface JsonRpcExceptionHandler<Request, Response, C extends RequestContext<Request>> {

    /**
     * Handle exception.
     *
     * @param throwable         See {@link Throwable}
     * @param id                Request id
     * @param context           See {@link RequestContext}
     * @param objectMapper      See {@link ObjectMapper}
     * @param conversionService See {@link ConversionService}
     * @return Protocol response
     */
    @NonNull
    Response handle(
            @NonNull Throwable throwable,
            JsonNode id,
            @NonNull C context,
            @NonNull ObjectMapper objectMapper,
            @NonNull ConversionService conversionService);
}
