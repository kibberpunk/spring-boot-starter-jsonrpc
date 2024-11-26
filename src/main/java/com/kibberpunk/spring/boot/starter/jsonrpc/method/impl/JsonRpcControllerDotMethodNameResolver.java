package com.kibberpunk.spring.boot.starter.jsonrpc.method.impl;

import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodNameResolver;
import lombok.NonNull;
import org.springdoc.core.utils.Constants;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Method;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

/**
 * 'Controller dot method'. First controller bean name + dot + method name.
 * Example: <b>controllerBeanName.methodName</b>
 *
 * @author kibberpunk
 */
public class JsonRpcControllerDotMethodNameResolver implements JsonRpcMethodNameResolver {

    /**
     * Resolve method name.
     *
     * @param controllerBeanNames Controller bean names
     * @param methodName          Method name
     * @param controllerType      Controller {@link ParameterizedTypeReference}
     * @param controller          Controller object
     * @param method              Controller method
     * @return Method name generated based on the received data
     */
    @Override
    @NonNull
    public <T> String resolve(
            final @NonNull String[] controllerBeanNames,
            final @NonNull String methodName,
            final @NonNull Class<T> controllerType,
            final @NonNull Object controller,
            final @NonNull Method method) {
        if (isEmpty(controllerBeanNames) || isEmpty(methodName)) {
            throw new IllegalArgumentException();
        }
        return controllerBeanNames[0] + Constants.DOT + methodName;
    }
}
