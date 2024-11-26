package com.kibberpunk.spring.boot.starter.jsonrpc.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Abstract 'Tyrant' (from Resident Evil) test JSON-RPC controller
 *
 * @author kibberpunk
 */
public class AbstractTyrantController {

    /**
     * Test request
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private UUID uuid;
        private String message;
        private double d;
        private Long l;
        private int i;
        private float f;
        private Parameter parameter;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Parameter {
            private Double d;
        }

        /**
         * Test response
         */
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Response {

            /*0*/ private UUID requestIdInUUIDFormat;
            /*1*/ private String requestIdInStringFormat;
            /*2*/ private JsonNode requestIdInJsonNodeFormat;

            /*3*/private Request requestObject;
            /*4*/private String requestObjectInStringFormat;
            /*5*/private JsonNode requestObjectInJsonNodeFormat;

            /*6*/private String messageRequestObjectParameter;
            /*7*/private int iRequestObjectParameter;
            /*8*/private double dRequestObjectParameter;
            /*9*/private Long LRequestObjectParameter;
            /*10*/private float fRequestObjectParameter;
            /*11*/private UUID uuidRequestObjectParameter;
            /*12*/private Request.Parameter parameterRequestObjectParameter;

            /*13*/private String messageRequestObjectParameterIgnored;
            /*14*/private int iiRequestObjectParameterIgnored;
            /*15*/private double dRequestObjectParameterIgnored;
            /*16*/private long lRequestObjectParameterIgnored;
            /*17*/private float fRequestObjectParameterIgnored;
            /*18*/private UUID uuidRequestObjectParameterIgnored;
            /*19*/private Request.Parameter parameterRequestObjectParameterIgnored;
            /*20*/private Integer IRequestObjectParameterIgnored;
        }
    }
}
