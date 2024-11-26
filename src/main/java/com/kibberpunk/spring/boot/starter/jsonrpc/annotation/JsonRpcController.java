package com.kibberpunk.spring.boot.starter.jsonrpc.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JSON-RPC controller marker.
 * Without this marker the RPC controller will not be recognized by the framework.
 *
 * @author kibberpunk
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface JsonRpcController {

    /**
     * See {@link Service#value()}.
     *
     * @return The suggested component name, if any (or empty String otherwise)
     */
    @AliasFor(annotation = Service.class)
    String value() default "";
}
