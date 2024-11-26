package com.kibberpunk.spring.boot.starter.jsonrpc.service;

import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Request;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Response;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodNameResolver;
import com.kibberpunk.spring.boot.starter.jsonrpc.service.impl.RequestContext;
import lombok.NonNull;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;

/**
 * JSON-RPC protocol support.
 *
 * @param <Request>  JSON-RPC request type. F.e. see {@link JsonRpc20Request}
 * @param <Response> JSON-RPC response type. F.e. see {@link JsonRpc20Response}
 * @param <C>        Request context type. See {@link RequestContext}
 * @author kibberpunk
 */
public interface JsonRpcProtocolSupport<Request, Response, C extends RequestContext<Request>> {

    /**
     * Create specific protocol {@link RequestContext}.
     *
     * @return Specific protocol {@link RequestContext}
     */
    @NonNull
    C context();

    /**
     * Converting a request object into the desired object.
     *
     * @param context See {@link RequestContext}
     * @return Converted request object into the desired object
     */
    @NonNull
    Request request(@NonNull C context);

    /**
     * Getting the name of the method to call from a request.
     *
     * @param context See {@link RequestContext}
     * @return {@link Method} name in configured style. See {@link JsonRpcMethodNameResolver}
     */
    @NonNull
    String methodName(@NonNull C context);

    /**
     * Receiving a method parameter from a request.
     *
     * @param parameter {@link Method} parameter
     * @param context   See {@link RequestContext}
     * @return Method parameter from a request
     */
    Object parameter(@NonNull MethodParameter parameter, @NonNull C context);

    /**
     * Converting a result object into the response object.
     *
     * @param result  Result of execution of the target method
     * @param context {@link RequestContext}
     * @return Converted request object into the desired object
     */
    @NonNull
    Response response(Object result, @NonNull C context);

    /**
     * Convert error result to response.
     *
     * @param throwable An error occurred while processing the request
     * @param context   See {@link RequestContext}
     * @return Response based on the error provided
     */
    @NonNull
    Response error(@NonNull Throwable throwable, @NonNull C context);
}
