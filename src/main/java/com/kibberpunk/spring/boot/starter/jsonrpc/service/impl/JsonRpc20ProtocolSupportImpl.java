package com.kibberpunk.spring.boot.starter.jsonrpc.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.google.common.base.Defaults;
import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcRequestId;
import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcRequestObject;
import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcRequestObjectParameter;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Request;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Response;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpcEmptyRequestBodyException;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpcException;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpcExceptionHandler;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpcMethodNotFoundException;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpcParseMethodParameterException;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpcRequestIdNotFoundException;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpcRequestReceivingException;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodNameResolver;
import com.kibberpunk.spring.boot.starter.jsonrpc.service.JsonRpcProtocolSupport;
import com.kibberpunk.spring.boot.starter.jsonrpc.utils.CatchExceptionUtils;
import com.kibberpunk.spring.boot.starter.jsonrpc.utils.FormatUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

/**
 * JSON-RPC protocol support.
 *
 * @author kibberpunk
 */
@Slf4j
@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class JsonRpc20ProtocolSupportImpl implements
        JsonRpcProtocolSupport<JsonRpc20Request, JsonRpc20Response, JsonRpc20RequestContext> {

    /**
     * See {@link ObjectMapper}.
     */
    private final ObjectMapper objectMapper;

    /**
     * See {@link ConversionService}.
     */
    private final ConversionService conversionService;

    /**
     * Exception handler.
     */
    private final JsonRpcExceptionHandler
            <JsonRpc20Request, JsonRpc20Response, JsonRpc20RequestContext> exceptionHandler;

    /**
     * Create specific protocol {@link JsonRpc20RequestContext}.
     *
     * @return See {@link JsonRpc20RequestContext}
     */
    @Override
    @NonNull
    public JsonRpc20RequestContext context() {
        return new JsonRpc20RequestContext();
    }

    /**
     * Converting a request object into {@link JsonRpc20Request}.
     *
     * @param context See {@link JsonRpc20RequestContext}
     * @return See {@link JsonRpc20Request}
     */
    @Override
    @NonNull
    public JsonRpc20Request request(final @NonNull JsonRpc20RequestContext context) {
        return Optional.ofNullable(context.getBody())
                .map(body -> CatchExceptionUtils.catchException(() ->
                                objectMapper.readTree(body),
                        JsonRpcRequestReceivingException::new))
                .filter(this::isNotEmptyJsonNode)
                .map(jsonNode -> CatchExceptionUtils.catchException(() ->
                                objectMapper.treeToValue(jsonNode, JsonRpc20Request.class),
                        JsonRpcRequestReceivingException::new))
                .orElseThrow(JsonRpcEmptyRequestBodyException::new);
    }

    /**
     * Getting the name of the method to call from a request.
     *
     * @param context See {@link JsonRpc20RequestContext}
     * @return {@link Method} name in configured style. See {@link JsonRpcMethodNameResolver}
     */
    @Override
    @NonNull
    public String methodName(final @NonNull JsonRpc20RequestContext context) {
        return Optional.ofNullable(context.getRequest().getMethod())
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank)
                .orElseThrow(JsonRpcMethodNotFoundException::new);
    }

    /**
     * Receiving a method parameter from a request.
     *
     * @param parameter {@link Method} parameter
     * @param context   See {@link JsonRpc20RequestContext}
     * @return Method parameter from a request with additional system parameters support.
     */
    @Override
    public Object parameter(
            final @NonNull MethodParameter parameter,
            final @NonNull JsonRpc20RequestContext context) {
        try {
            if (parameter.hasParameterAnnotation(JsonRpcRequestId.class)) {
                return jsonRpcRequestId(Objects.requireNonNull(
                        parameter.getParameterAnnotation(JsonRpcRequestId.class)), parameter, context);
            } else if (parameter.hasParameterAnnotation(JsonRpcRequestObject.class)) {
                return jsonRpcRequestObject(parameter, context);
            } else if (parameter.hasParameterAnnotation(JsonRpcRequestObjectParameter.class)) {
                return jsonRpcRequestObjectParameter(Objects.requireNonNull(
                        parameter.getParameterAnnotation(JsonRpcRequestObjectParameter.class)), parameter, context);
            }
            return convert(NullNode.getInstance(), parameter);
        } catch (final Throwable throwable) {
            if (throwable instanceof JsonRpcException) {
                throw ExceptionUtils.asRuntimeException(throwable);
            }
            throw new JsonRpcParseMethodParameterException(throwable);
        }
    }

    /**
     * Converting a response object into the {@link JsonRpc20Response} object.
     *
     * @param result  Result of execution of the target method
     * @param context {@link JsonRpc20RequestContext}
     * @return See {@link JsonRpc20Response}
     */
    @Override
    @NonNull
    public JsonRpc20Response response(final Object result, final @NonNull JsonRpc20RequestContext context) {
        return JsonRpc20Response.builder()
                .id(id(context))
                .result(result).build();
    }

    /**
     * Convert error result to {@link JsonRpc20Response}.
     *
     * @param throwable An error occurred while processing the request
     * @param context   See {@link JsonRpc20RequestContext}
     * @return {@link JsonRpc20Response} based on the error provided
     */
    @Override
    @NonNull
    public JsonRpc20Response error(
            final @NonNull Throwable throwable,
            final @NonNull JsonRpc20RequestContext context) {
        log.error(FormatUtils.format("Request processing exception to request id {}", id(context)), throwable);
        return exceptionHandler.handle(throwable, id(context), context, objectMapper, conversionService);
    }

    /**
     * Get id from request.
     *
     * @param context Request context
     * @return id from request. Can return null if id is null value
     */
    @NonNull
    protected JsonNode id(final @NonNull JsonRpc20RequestContext context) {
        return Optional.ofNullable(context.getRequest()).map(JsonRpc20Request::getId).orElse(NullNode.getInstance());
    }

    /**
     * Get params {@link JsonNode} from request.
     *
     * @param context See {@link JsonRpc20RequestContext}
     * @return 'params' {@link JsonNode} from request.
     */
    @NonNull
    protected JsonNode params(final @NonNull JsonRpc20RequestContext context) {
        return Optional.ofNullable(context.getRequest().getParams())
                .orElse(NullNode.getInstance());
    }

    /**
     * Get request id by {@link JsonRpcRequestId} annotation.
     *
     * @param jsonRpcRequestId See {@link JsonRpcRequestId}
     * @param parameter        See {@link MethodParameter}
     * @param context          See {@link JsonRpc20RequestContext}
     * @return Request id by {@link JsonRpcRequestId} annotation
     */
    protected Object jsonRpcRequestId(
            final @NonNull JsonRpcRequestId jsonRpcRequestId,
            final @NonNull MethodParameter parameter,
            final @NonNull JsonRpc20RequestContext context) {
        return Optional.of(id(context))
                .filter(this::isNotNullJsonNode)
                .map(id -> convert(id, parameter))
                .orElseGet(() -> {
                    if (jsonRpcRequestId.required()) {
                        throw new JsonRpcRequestIdNotFoundException();
                    }
                    return convert(NullNode.getInstance(), parameter);
                });
    }

    /**
     * Get request params object by {@link JsonRpcRequestObject} annotation.
     *
     * @param parameter See {@link MethodParameter}
     * @param context   See {@link JsonRpc20RequestContext}
     * @return Request params object by {@link JsonRpcRequestObject} annotation
     */
    protected Object jsonRpcRequestObject(
            final @NonNull MethodParameter parameter,
            final @NonNull JsonRpc20RequestContext context) {
        return convert(params(context), parameter);
    }

    /**
     * Get parameter by name from {@link JsonRpcRequestObjectParameter} annotation.
     *
     * @param jsonRpcRequestObjectParameter See {@link JsonRpcRequestObjectParameter}
     * @param parameter                     See {@link MethodParameter}
     * @param context                       See {@link JsonRpc20RequestContext}
     * @return Parameter by name from {@link JsonRpcRequestObjectParameter} annotation
     */
    protected Object jsonRpcRequestObjectParameter(
            final @NonNull JsonRpcRequestObjectParameter jsonRpcRequestObjectParameter,
            final @NonNull MethodParameter parameter,
            final @NonNull JsonRpc20RequestContext context) {
        return convert(Optional.ofNullable(params(context).get(jsonRpcRequestObjectParameter.value()))
                .orElse(NullNode.getInstance()), parameter);
    }

    /**
     * Convert {@link JsonNode} to object.
     *
     * @param jsonNode  See {@link JsonNode}
     * @param parameter See {@link MethodParameter}
     * @return Convert from {@link JsonNode} object
     */
    protected Object convert(final @NonNull JsonNode jsonNode, final @NonNull MethodParameter parameter) {
        if (ClassUtils.isSimpleValueType(parameter.getParameterType())) {
            return convertSimpleType(jsonNode, parameter);
        }
        return convertComplexType(jsonNode, parameter);
    }

    /**
     * Get simple type object from {@link JsonNode}.
     *
     * @param jsonNode  See {@link JsonNode}
     * @param parameter See {@link MethodParameter}
     * @return Simple type object from {@link JsonNode}
     */
    protected Object convertSimpleType(final @NonNull JsonNode jsonNode, final @NonNull MethodParameter parameter) {
        return Optional.of(jsonNode)
                .filter(this::isNotNullJsonNode)
                .map((Function<JsonNode, Object>) jsonValue ->
                        jsonValue.isContainerNode() ? jsonValue.toString() : jsonValue.asText())
                .filter(value ->
                        conversionService.canConvert(value.getClass(), parameter.getParameterType()))
                .map((Function<Object, Object>) value ->
                        conversionService.convert(value, parameter.getParameterType()))
                .orElse(Defaults.defaultValue(parameter.getParameterType()));
    }

    /**
     * Get complex type object {@link JsonNode}.
     *
     * @param jsonNode  See {@link JsonNode}
     * @param parameter See {@link MethodParameter}
     * @return Complex object from {@link JsonNode}
     */
    protected Object convertComplexType(final @NonNull JsonNode jsonNode, final @NonNull MethodParameter parameter) {
        return CatchExceptionUtils.catchException((CatchExceptionUtils.Supplier<Object>) () ->
                objectMapper.treeToValue(jsonNode, parameter.getParameterType()), RuntimeException::new);
    }

    /**
     * Check {@link JsonNode} is empty. Call isNotNullJsonNode and jsonNode.isEmpty().
     *
     * @param jsonNode See {@link JsonNode}
     * @return true is isNotNullJsonNode return true and !jsonNode.isEmpty().
     */
    protected boolean isNotEmptyJsonNode(final JsonNode jsonNode) {
        return isNotNullJsonNode(jsonNode) && !jsonNode.isEmpty();
    }

    /**
     * Check {@link JsonNode} is null.
     *
     * @param jsonNode See {@link JsonNode}
     * @return true is {@link JsonNode} object is not null and !jsonNode.isNull()
     */
    protected boolean isNotNullJsonNode(final JsonNode jsonNode) {
        return isNotEmpty(jsonNode)
                && !jsonNode.isNull();
    }
}
