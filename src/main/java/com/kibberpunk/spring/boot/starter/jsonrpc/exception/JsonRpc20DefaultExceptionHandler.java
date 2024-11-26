package com.kibberpunk.spring.boot.starter.jsonrpc.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Request;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Response;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpcError;
import com.kibberpunk.spring.boot.starter.jsonrpc.service.impl.JsonRpc20RequestContext;
import lombok.NonNull;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.convert.ConversionService;

/**
 * Default {@link JsonRpcExceptionHandler}.
 *
 * @author kibberpunk
 */
public class JsonRpc20DefaultExceptionHandler implements
        JsonRpcExceptionHandler<JsonRpc20Request, JsonRpc20Response, JsonRpc20RequestContext> {

    /**
     * Handle exception.
     *
     * @param throwable         See {@link Throwable}
     * @param id                Request id
     * @param context           See {@link JsonRpc20RequestContext}
     * @param objectMapper      See {@link ObjectMapper}
     * @param conversionService See {@link ConversionService}
     * @return Protocol response
     */
    @Override
    public @NonNull JsonRpc20Response handle(
            final @NonNull Throwable throwable,
            final JsonNode id,
            final @NonNull JsonRpc20RequestContext context,
            final @NonNull ObjectMapper objectMapper,
            final @NonNull ConversionService conversionService) {
        if (throwable instanceof JsonRpcRequestReceivingException) {
            return JsonRpc20Response.error(id, JsonRpcError.Code.PARSE_ERROR,
                    ExceptionUtils.getStackTrace(throwable));
        } else if (throwable instanceof JsonRpcEmptyRequestBodyException) {
            return JsonRpc20Response.error(id, JsonRpcError.Code.INVALID_REQUEST);
        } else if (throwable instanceof JsonRpcMethodNotFoundException) {
            return JsonRpc20Response.error(id, JsonRpcError.Code.METHOD_NOT_FOUND);
        } else if (throwable instanceof JsonRpcParseMethodParameterException) {
            return JsonRpc20Response.error(id, JsonRpcError.Code.INVALID_PARAMS,
                    ExceptionUtils.getStackTrace(throwable));
        } else if (throwable instanceof JsonRpcRequestIdNotFoundException) {
            return JsonRpc20Response.error(id, JsonRpcError.Code.REQUEST_ID_IS_EMPTY);
        }
        return JsonRpc20Response.error(id, JsonRpcError.Code.INTERNAL_ERROR,
                ExceptionUtils.getStackTrace(throwable));
    }
}
