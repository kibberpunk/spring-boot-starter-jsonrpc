package com.kibberpunk.spring.boot.starter.jsonrpc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JSON-RPC method marker.
 * Without this marker the method will not be recognized by the framework.
 *
 * @author kibberpunk
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonRpcMethod {
}
