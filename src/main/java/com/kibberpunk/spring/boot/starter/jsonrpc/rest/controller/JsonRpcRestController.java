package com.kibberpunk.spring.boot.starter.jsonrpc.rest.controller;

import com.kibberpunk.spring.boot.starter.jsonrpc.service.JsonRpcService;
import com.kibberpunk.spring.boot.starter.jsonrpc.utils.JsonRpcUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * JSON-RPC consumer controller.
 * Receives the request in plain text and passes it on to the processor
 *
 * @author kibberpunk
 */
@RequiredArgsConstructor
@RestController
public class JsonRpcRestController {

    /**
     * @see JsonRpcService
     */
    private final JsonRpcService processor;

    /**
     * Handle JSON-RPC REST request.
     *
     * @param request            Request in plain text
     * @param httpServletRequest See {@link HttpServletRequest}
     * @return JSON-RPC response object
     */
    @PostMapping("${spring.json-rpc.consumer.path}") /* See {@link JsonRpcConfigurationProperties.Consumer#path} */
    @Operation(
            summary = "JSON-RPC (JavaScript Object Notation-Remote Procedure Call) "
                    + "is a remote procedure call (RPC) protocol encoded in JSON",
            description = """
                    JSON-RPC is a stateless, light-weight remote procedure call (RPC) protocol.
                    Primarily this specification defines several data structures and the rules around their processing.
                    It is transport agnostic in that the concepts can be used within the same process, over sockets,
                     over http, or in many various message passing environments.
                    <b>https://www.jsonrpc.org/specification</b>
                    """,
            tags = {JsonRpcUtils.API_TAG}
    )
    public @ResponseBody Object handle(
            final @RequestBody String request,
            final @NonNull HttpServletRequest httpServletRequest) {
        return this.processor.process(request, httpServletRequest);
    }
}
