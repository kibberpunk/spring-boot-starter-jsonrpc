package com.kibberpunk.spring.boot.starter.jsonrpc.service.impl;

import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Request;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Response;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpcMethodNotFoundException;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpcParseMethodParameterException;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodProvider;
import com.kibberpunk.spring.boot.starter.jsonrpc.service.JsonRpcProtocolSupport;
import com.kibberpunk.spring.boot.starter.jsonrpc.service.JsonRpcService;
import com.kibberpunk.spring.boot.starter.jsonrpc.utils.FormatUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Abstract {@link JsonRpcService} implementation.
 *
 * @param <Request>  JSON-RPC request type. F.e. see {@link JsonRpc20Request}
 * @param <Response> JSON-RPC response type. F.e. see {@link JsonRpc20Response}
 * @param <C>        Request context type. See {@link RequestContext}
 * @author kibberpunk
 */
@Slf4j
@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class AbstractJsonRpcService<Request, Response, C
        extends RequestContext<Request>>
        implements JsonRpcService {

    /**
     * Available {@link JsonRpcProtocolSupport}.
     */
    private final JsonRpcProtocolSupport<Request, Response, C> support;

    /**
     * See {@link JsonRpcMethodProvider}.
     */
    private final JsonRpcMethodProvider methodProvider;

    /**
     * Process request.
     *
     * @param body               Raw request body string
     * @param httpServletRequest See {@link HttpServletRequest}
     * @return Response after request processing
     */
    @Override
    public Object process(final String body, final @NonNull HttpServletRequest httpServletRequest) {
        return Optional.of(support.context())
                .map(context -> {
                    context.setBody(body);
                    context.setHttpServletRequest(httpServletRequest);
                    return context;
                })
                .map((Function<C, Object>) context -> {
                    try {
                        context.setRequest(support.request(context));
                        return support.response(call(context), context);
                    } catch (final Throwable throwable) {
                        log.error(FormatUtils.format(
                                "An exception occurred while processing the request: {}", body), throwable);
                        return support.error(throwable, context);
                    }
                }).orElseThrow();
    }

    /**
     * Call {@link Method}.
     *
     * @param context See {@link RequestContext}
     * @return {@link Method} result
     */
    protected Object call(final @NonNull C context) {
        return Optional.of(method(context))
                .map(box ->
                        ReflectionUtils.invokeMethod(
                                box.getLeft(),
                                box.getMiddle(),
                                parameters(box.getRight(), context)))
                .orElse(null);
    }

    /**
     * Build parameters to controller {@link Method}.
     *
     * @param methodParameterList {@link MethodParameter} list
     * @param context             See {@link RequestContext}
     * @return Parameters array to controller {@link Method}
     */
    protected Object[] parameters(final @NonNull List<MethodParameter> methodParameterList, final @NonNull C context) {
        return Optional.of(new Object[methodParameterList.size()])
                .map(parameters -> {
                    methodParameterList.forEach(parameter ->
                            parameters[parameter.getParameterIndex()] = isSystemParameter(parameter)
                                    ? systemParameter(parameter, context)
                                    : support.parameter(parameter, context));
                    return parameters;
                }).orElseThrow();
    }

    /**
     * Get target {@link Method} and controller object.
     *
     * @param context See {@link RequestContext}
     * @return Target {@link Method} and controller object
     */
    protected Triple<Method, Object, List<MethodParameter>> method(final @NonNull C context) {
        return Optional.of(support.methodName(context))
                .map(methodProvider::get)
                .orElseThrow(() -> new JsonRpcMethodNotFoundException(support.methodName(context)));
    }

    /**
     * Check is system parameter.
     * {@link java.security.Principal}
     * {@link org.springframework.web.bind.annotation.CookieValue}
     * And others ...
     *
     * @param parameter See {@link MethodParameter}
     * @return True if parameter:
     * instanceof {@link Principal} or {@link HttpServletRequest}
     */
    protected boolean isSystemParameter(final @NonNull MethodParameter parameter) {
        return parameter.getParameterType() == Principal.class
                || parameter.getParameterType() == HttpServletRequest.class;
    }

    /**
     * Process system parameter.
     * {@link java.security.Principal}
     * {@link HttpServletRequest}
     * And others ...
     *
     * @param parameter See {@link MethodParameter}
     * @param context   See {@link RequestContext}
     * @return Processed system parameter
     */
    protected Object systemParameter(
            final @NonNull MethodParameter parameter,
            final @NonNull C context) {
        if (parameter.getParameterType() == Principal.class) {
            return context.getHttpServletRequest().getUserPrincipal();
        } else if (parameter.getParameterType() == HttpServletRequest.class) {
            return context.getHttpServletRequest();
        }
        throw new JsonRpcParseMethodParameterException(
                FormatUtils.format("Could not identify the type of system parameter {}", parameter));
    }
}
