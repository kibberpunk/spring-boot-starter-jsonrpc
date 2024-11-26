package com.kibberpunk.spring.boot.starter.jsonrpc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * Provided JSON-RPC request id to target {@link Method}.
 *
 * @author kibberpunk
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonRpcRequestId {

    /**
     * If true are set, then if there is no identifier in the request, there will be an exception.
     *
     * @return By default returns false
     */
    boolean required() default false;
}
