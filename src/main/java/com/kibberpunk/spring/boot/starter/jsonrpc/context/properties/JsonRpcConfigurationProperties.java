package com.kibberpunk.spring.boot.starter.jsonrpc.context.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JSON-RPC configuration properties.
 *
 * @author kibberpunk
 */
@ConfigurationProperties("spring.json-rpc")
@Getter
@Setter
public class JsonRpcConfigurationProperties {

    /**
     * @see Consumer
     */
    private Consumer consumer;

    /**
     * JSON-RPC consumer properties.
     */
    @Getter
    @Setter
    public static class Consumer {

        /**
         * Consumer controller path.
         * The main endpoint for calling JSON-RPC methods will be available along this path
         */
        private String path = "/api";
    }
}
