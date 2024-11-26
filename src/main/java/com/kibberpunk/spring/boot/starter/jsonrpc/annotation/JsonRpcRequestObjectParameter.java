package com.kibberpunk.spring.boot.starter.jsonrpc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * First level path of the parameter at the 'params:' ({@link JsonRpcRequestObject}) object.
 *
 * @author kibberpunk
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonRpcRequestObjectParameter {

    /**
     * Path of the parameter.
     *
     * @return Path of the parameter. None default value
     */
    String value();
}
