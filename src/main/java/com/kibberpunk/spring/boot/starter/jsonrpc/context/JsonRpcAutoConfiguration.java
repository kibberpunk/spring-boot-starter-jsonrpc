package com.kibberpunk.spring.boot.starter.jsonrpc.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kibberpunk.spring.boot.starter.jsonrpc.context.properties.JsonRpcConfigurationProperties;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Request;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Response;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpc20DefaultExceptionHandler;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpcExceptionHandler;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodLoader;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodNameResolver;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodProvider;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.impl.JsonRpcControllerDotMethodNameResolver;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.impl.JsonRpcMethodLoaderImpl;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.impl.JsonRpcMethodProviderImpl;
import com.kibberpunk.spring.boot.starter.jsonrpc.rest.controller.JsonRpcRestController;
import com.kibberpunk.spring.boot.starter.jsonrpc.service.JsonRpcProtocolSupport;
import com.kibberpunk.spring.boot.starter.jsonrpc.service.JsonRpcService;
import com.kibberpunk.spring.boot.starter.jsonrpc.service.impl.JsonRpc20ProtocolService;
import com.kibberpunk.spring.boot.starter.jsonrpc.service.impl.JsonRpc20ProtocolSupportImpl;
import com.kibberpunk.spring.boot.starter.jsonrpc.service.impl.JsonRpc20RequestContext;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;

/**
 * JSON-RPC auto configuration.
 *
 * @author kibberpunk
 */
@Configuration
@EnableConfigurationProperties({JsonRpcConfigurationProperties.class})
@Import(JsonRpcRestController.class)
@PropertySource("classpath:spring-boot-starter-json-rpc.properties")
public class JsonRpcAutoConfiguration {

    /**
     * Create {@link JsonRpcService}.
     *
     * @param protocolSupport See {@link JsonRpc20ProtocolSupportImpl}
     * @param methodProvider  See {@link JsonRpcMethodProvider}
     * @return {@link JsonRpc20ProtocolService}
     */
    @Bean
    @ConditionalOnMissingBean
    public JsonRpcService jsonRpcProcessor(
            final JsonRpcProtocolSupport
                    <JsonRpc20Request, JsonRpc20Response, JsonRpc20RequestContext> protocolSupport,
            final JsonRpcMethodProvider methodProvider) {
        return new JsonRpc20ProtocolService(protocolSupport, methodProvider);
    }

    /**
     * Create JSON-RPC {@link JsonRpcProtocolSupport}.
     *
     * @param objectMapper             See {@link ObjectMapper}
     *                                 In real use, the {@link ObjectMapper} will already be created
     *                                 or will still have to be created at the stage of context assembly
     * @param jsonRpcConversionService See {@link ConversionService}
     * @param jsonRpcExceptionHandler  See {@link JsonRpcExceptionHandler}
     * @return {@link JsonRpc20ProtocolSupportImpl}
     */
    @Bean
    @ConditionalOnMissingBean
    public JsonRpcProtocolSupport<JsonRpc20Request, JsonRpc20Response, JsonRpc20RequestContext>
    jsonRpcProtocolSupport(
            final ObjectMapper objectMapper,
            final @Qualifier("jsonRpcConversionService") ConversionService jsonRpcConversionService,
            final JsonRpcExceptionHandler
                    <JsonRpc20Request, JsonRpc20Response, JsonRpc20RequestContext> jsonRpcExceptionHandler) {
        return new JsonRpc20ProtocolSupportImpl(objectMapper, jsonRpcConversionService, jsonRpcExceptionHandler);
    }

    /**
     * Create {@link JsonRpcMethodProvider}.
     *
     * @param methodLoader {@link JsonRpcMethodLoader}
     * @return {@link JsonRpcMethodProviderImpl}
     */
    @Bean
    @ConditionalOnMissingBean
    public JsonRpcMethodProvider jsonRpcMethodProvider(final JsonRpcMethodLoader methodLoader) {
        return new JsonRpcMethodProviderImpl(methodLoader);
    }

    /**
     * Create {@link JsonRpcMethodLoader}.
     *
     * @param methodNameResolver {@link JsonRpcMethodNameResolver}
     * @param beanFactory        {@link ListableBeanFactory}
     * @return {@link JsonRpcMethodLoaderImpl}
     */
    @Bean
    @ConditionalOnMissingBean
    public JsonRpcMethodLoader jsonRpcMethodLoader(
            final JsonRpcMethodNameResolver methodNameResolver,
            final ListableBeanFactory beanFactory) {
        return new JsonRpcMethodLoaderImpl(methodNameResolver, beanFactory);
    }

    /**
     * Create {@link JsonRpcMethodNameResolver}.
     *
     * @return {@link JsonRpcControllerDotMethodNameResolver}
     */
    @Bean
    @ConditionalOnMissingBean
    public JsonRpcMethodNameResolver jsonRpcMethodNameResolver() {
        return new JsonRpcControllerDotMethodNameResolver();
    }

    /**
     * Create {@link ConversionService}.
     *
     * @return {@link ConversionService} from {@link ConversionServiceFactoryBean}
     */
    @Bean
    @ConditionalOnMissingBean(name = "jsonRpcConversionService")
    public ConversionService jsonRpcConversionService() {
        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        //bean.setConverters(...); //add converters
        bean.afterPropertiesSet();
        return bean.getObject();
    }

    /**
     * Create {@link JsonRpcExceptionHandler}.
     *
     * @return See {@link JsonRpc20DefaultExceptionHandler}
     */
    @Bean
    @ConditionalOnMissingBean
    public JsonRpcExceptionHandler
            <JsonRpc20Request, JsonRpc20Response, JsonRpc20RequestContext> jsonRpcExceptionHandler() {
        return new JsonRpc20DefaultExceptionHandler();
    }
}
