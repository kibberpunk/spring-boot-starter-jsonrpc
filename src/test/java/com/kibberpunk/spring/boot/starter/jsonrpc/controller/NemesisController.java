package com.kibberpunk.spring.boot.starter.jsonrpc.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcController;
import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcMethod;
import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcRequestId;
import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcRequestObject;
import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcRequestObjectParameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

import java.security.Principal;
import java.util.UUID;

/**
 * 'Nemesis' (from Resident Evil) JSON-RPC controller
 *
 * @author kibberpunk
 */
@JsonRpcController
public class NemesisController extends AbstractTyrantController {

    /**
     * 'Attack' JSON-RPC method
     */
    @JsonRpcMethod
    public Request.Response attack(
            /*0*/ final @JsonRpcRequestId(required = true) UUID requiredUUIDRequestId,
            /*1*/ final @JsonRpcRequestId String StringRequestId,
            /*2*/ final @JsonRpcRequestId Integer integerRequestId,
            /*3*/ final @JsonRpcRequestId int intRequestId,
            /*4*/ final @JsonRpcRequestId Double DoubleRequestId,
            /*5*/ final @JsonRpcRequestId double doubleRequestId,
            /*6*/ final @JsonRpcRequestId Long LongRequestId,
            /*7*/ final @JsonRpcRequestId long longRequestId,
            /*8*/ final @JsonRpcRequestId Float FloatRequestId,
            /*9*/ final @JsonRpcRequestId float floatRequestId,
            /*10*/final @JsonRpcRequestId UUID uuidRequestId,
            /*11*/final @JsonRpcRequestId ComplexRequestId complexRequestId,

            /*12*/final String ignoredParameter1,

            /*13*/final @JsonRpcRequestObject String StringFullObject,
            /*14*/final @JsonRpcRequestObject JsonNode JsonNodeFullObject,
            /*15*/final @JsonRpcRequestObject Request RequestFullObject,

            /*16*/final Integer ignoredParameter2,
            /*17*/final Double indexedDouble,

            /*18*/final @JsonRpcRequestObjectParameter("uuid") UUID uuid,
            /*19*/final @JsonRpcRequestObjectParameter("message") String message,
            /*20*/final @JsonRpcRequestObjectParameter("i") Integer I,
            /*21*/final @JsonRpcRequestObjectParameter("i") int i,
            /*22*/final @JsonRpcRequestObjectParameter("d") Double D,
            /*23*/final @JsonRpcRequestObjectParameter("d") double d,
            /*24*/final @JsonRpcRequestObjectParameter("l") Long L,
            /*25*/final @JsonRpcRequestObjectParameter("l") long l,
            /*26*/final @JsonRpcRequestObjectParameter("parameter") Request.Parameter parameter,

            /*27*/final Long ignoredParameter3,
            /*28*/final Float ignoredParameter4,
            /*29*/final UUID ignoredParameter5,

            /*30*/final Principal principal,
            /*31*/final HttpServletRequest httpServletRequest,

            /*32*/final Request.Parameter ignoredParameter6,
            /*33*/final Long ignoredParameter7
    ) {
        return null;
    }

    /**
     * 'Battle exit' JSON-RPC method
     */
    @JsonRpcMethod
    public Request.Response battleExit(
            /*0*/ final @JsonRpcRequestId(required = true) UUID requestIdInUUIDFormat,
            /*1*/ final @JsonRpcRequestId String requestIdInStringFormat,
            /*2*/ final @JsonRpcRequestId JsonNode requestIdInJsonNodeFormat,

            /*3*/final @JsonRpcRequestObject Request requestObject,
            /*4*/final @JsonRpcRequestObject String requestObjectInStringFormat,
            /*5*/final @JsonRpcRequestObject JsonNode requestObjectInJsonNodeFormat,

            /*6*/final @JsonRpcRequestObjectParameter("message") String messageRequestObjectParameter,
            /*7*/final @JsonRpcRequestObjectParameter("i") int iRequestObjectParameter,
            /*8*/final @JsonRpcRequestObjectParameter("d") double dRequestObjectParameter,
            /*9*/final @JsonRpcRequestObjectParameter("l") Long LRequestObjectParameter,
            /*10*/final @JsonRpcRequestObjectParameter("f") float fRequestObjectParameter,
            /*11*/final @JsonRpcRequestObjectParameter("uuid") UUID uuidRequestObjectParameter,
            /*12*/final @JsonRpcRequestObjectParameter("parameter") Request.Parameter parameterRequestObjectParameter,

            /*13*/final String messageRequestObjectParameterIgnored,
            /*14*/final int iRequestObjectParameterIgnored,
            /*15*/final double dRequestObjectParameterIgnored,
            /*16*/final long lRequestObjectParameterIgnored,
            /*17*/final float fRequestObjectParameterIgnored,
            /*18*/final UUID uuidRequestObjectParameterIgnored,
            /*19*/final Request.Parameter parameterRequestObjectParameterIgnored,
            /*20*/final Integer IRequestObjectParameterIgnored
    ) {
        return new Request.Response(
                requestIdInUUIDFormat,
                requestIdInStringFormat,
                requestIdInJsonNodeFormat,

                requestObject,
                requestObjectInStringFormat,
                requestObjectInJsonNodeFormat,

                messageRequestObjectParameter,
                iRequestObjectParameter,
                dRequestObjectParameter,
                LRequestObjectParameter,
                fRequestObjectParameter,
                uuidRequestObjectParameter,
                parameterRequestObjectParameter,

                messageRequestObjectParameterIgnored,
                iRequestObjectParameterIgnored,
                dRequestObjectParameterIgnored,
                lRequestObjectParameterIgnored,
                fRequestObjectParameterIgnored,
                uuidRequestObjectParameterIgnored,
                parameterRequestObjectParameterIgnored,
                IRequestObjectParameterIgnored
        );
    }

    /**
     * 'Mutate' JSON-RPC method
     */
    @JsonRpcMethod
    public void mutate() {
    }

    /**
     * 'Sleep' NONE JSON-RPC method (because tyrant never sleep)
     */
    public String sleep() {
        return null;
    }

    /**
     * Complex request id
     */
    @Getter
    @Setter
    public static class ComplexRequestId {

        private String StringId;
        private Integer integerId;
        private int intId;
        private Double DoubleId;
        private double doubleId;
        private Long LongId;
        private long longId;
        private Float FloatId;
        private float floatId;
        private UUID uuid;
    }
}
