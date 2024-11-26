package com.kibberpunk.spring.boot.starter.jsonrpc.context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Test {@link JsonRpcAutoConfiguration}
 *
 * @author kibberpunk
 */
@Configuration
@ComponentScan("com.kibberpunk.spring.boot.starter.jsonrpc.controller")
public class TestJsonRpcAutoConfiguration extends JsonRpcAutoConfiguration {

    /**
     * Create temporally {@link ObjectMapper}
     * In real use, the {@link ObjectMapper} will already be created or will still have to be created at the stage of context assembly
     *
     * @return Temporally {@link ObjectMapper}
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().
                registerModule(new JavaTimeModule()).
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).
                enable(SerializationFeature.INDENT_OUTPUT);
    }
}

