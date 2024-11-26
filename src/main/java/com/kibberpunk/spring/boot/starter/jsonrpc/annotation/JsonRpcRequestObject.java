package com.kibberpunk.spring.boot.starter.jsonrpc.annotation;

import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Request;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Converts params section into a whole object.
 * See {@link JsonRpc20Request#getParams()}
 * <p>
 * F.e.: If we have
 * ---"params" : {
 * -----"message" : "message123"
 * ---}
 * this string will be converted to object with {@link JsonRpcRequestObject} annotation
 *
 * @author kibberpunk
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonRpcRequestObject {
}
