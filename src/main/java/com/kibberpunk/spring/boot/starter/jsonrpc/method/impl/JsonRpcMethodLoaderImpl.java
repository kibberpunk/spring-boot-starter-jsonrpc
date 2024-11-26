package com.kibberpunk.spring.boot.starter.jsonrpc.method.impl;

import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcController;
import com.kibberpunk.spring.boot.starter.jsonrpc.annotation.JsonRpcMethod;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodLoader;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodNameResolver;
import com.kibberpunk.spring.boot.starter.jsonrpc.utils.FormatUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

/**
 * JSON-RPC {@link JsonRpcMethodLoader} implementation.
 *
 * @author kibberpunk
 */
@Slf4j
@Getter(AccessLevel.PROTECTED)
public class JsonRpcMethodLoaderImpl implements JsonRpcMethodLoader {

    /**
     * Found methods are annotated {@link JsonRpcMethod} with parameters in {@link JsonRpcController}'s.
     */
    private final Map<String, Triple<Method, Object, List<MethodParameter>>> index;

    /**
     * Constructor.
     *
     * @param nameResolver {@link JsonRpcMethodNameResolver}
     * @param beanFactory  {@link ListableBeanFactory}
     */
    public JsonRpcMethodLoaderImpl(final @NonNull JsonRpcMethodNameResolver nameResolver,
                                   final @NonNull ListableBeanFactory beanFactory) {
        this.index = load(nameResolver, beanFactory.getBeansWithAnnotation(JsonRpcController.class));
    }

    /**
     * Load method by specific name. See {@link JsonRpcMethodNameResolver}.
     *
     * @param name Specific method name. See {@link JsonRpcMethodNameResolver}
     * @return {@link Method}, controller object and {@link Method} parameters {@link Triple}
     */
    @Override
    public Triple<Method, Object, List<MethodParameter>> get(final @NonNull String name) {
        return index.get(name);
    }

    /**
     * Load methods.
     *
     * @param methodNameResolver See {@link JsonRpcMethodNameResolver}
     * @param controllers        Controllers map
     * @return Found methods are annotated {@link JsonRpcMethod} in {@link JsonRpcController}'s.
     */
    @NonNull
    protected Map<String, Triple<Method, Object, List<MethodParameter>>> load(
            final @NonNull JsonRpcMethodNameResolver methodNameResolver,
            final @NonNull Map<String, Object> controllers) {
        return Optional.of(new HashMap<String, Triple<Method, Object, List<MethodParameter>>>()).map(map -> {
            controllers.forEach((controllerName, controller) ->
                    Arrays.stream(AopUtils.getTargetClass(controller).getMethods()).forEach(method -> {
                        if (isCandidate(method)) {
                            if (isEmpty(map.put(methodNameResolver.resolve(
                                    new String[]{controllerName},
                                    method.getName(),
                                    AopUtils.getTargetClass(controller),
                                    controller,
                                    method), box(method, controller)))) {
                                log.debug("Method {} from controller {} was added to JSON-RPC index",
                                        method.getName(), controller.getClass());
                            } else {
                                throw nonUniqueMethodNameException(method, controller);
                            }
                        }
                    }));
            return map;
        }).orElseThrow();
    }

    /**
     * Checks whether the method meets the required parameters.
     *
     * @param method {@link Method} to check that it is suitable
     * @return true if method meets the required parameters
     */
    protected boolean isCandidate(final @NonNull Method method) {
        return AnnotationUtils.findAnnotation(method, JsonRpcMethod.class) != null
                && Modifier.isPublic(method.getModifiers())
                && !Modifier.isStatic(method.getModifiers())
                && method.getDeclaringClass() != Object.class;
    }

    /**
     * Box {@link Method}, controller object and {@link Method} parameters to index {@link Triple}.
     *
     * @param method     Target {@link Method}
     * @param controller Target {@link Method} controller object
     * @return {@link Triple} with {@link Method}, controller object and {@link Method} parameters
     */
    @NonNull
    protected Triple<Method, Object, List<MethodParameter>> box(
            final @NonNull Method method, final @NonNull Object controller) {
        return ImmutableTriple.of(method, controller,
                List.of(Optional.of(new MethodParameter[method.getParameterCount()])
                        .map(parameters -> {
                            for (int i = 0; i < parameters.length; i++) {
                                parameters[i] = new OptimizedMethodParameter(method, i);
                            }
                            return parameters;
                        })
                        .orElseThrow()));
    }

    /**
     * Throws an exception if two methods with the same name are found in a controller.
     *
     * @param method     Target {@link Method}
     * @param controller Target {@link Method} controller object
     * @return {@link IllegalArgumentException}
     */
    protected IllegalArgumentException nonUniqueMethodNameException(
            final @NonNull Method method, final @NonNull Object controller) {
        throw new IllegalArgumentException(FormatUtils.format(
                "Found > 1 methods with name {} in controller {}. "
                        + "Currently only methods with different names within a single controller are supported",
                method.getName(), controller.getClass()));
    }

    /**
     * Optimized {@link MethodParameter}.
     * Optimized {@link MethodParameter#hasParameterAnnotation(Class)} function.
     * The {@link MethodParameter#hasParameterAnnotation(Class)} implementation is built on receiving an object
     * and casting its type, which is not needed for verification.
     * <p>
     * Old implementation:
     * <code>
     * return (getParameterAnnotation(annotationType) != null);
     * </code>
     * <p>
     * <code>
     * Annotation[] anns = getParameterAnnotations();
     * for (Annotation ann : anns) {
     * if (annotationType.isInstance(ann)) {
     * return (A) ann;
     * </code>
     * return null;
     */
    public static class OptimizedMethodParameter extends MethodParameter {

        /**
         * Create a new {@code MethodParameter} for the given method, with nesting level 1.
         *
         * @param method         the Method to specify a parameter for
         * @param parameterIndex the index of the parameter: -1 for the method
         *                       return type; 0 for the first method parameter; 1 for the second method
         *                       parameter, etc.
         */
        public OptimizedMethodParameter(final @NonNull Method method, final int parameterIndex) {
            super(method, parameterIndex);
        }

        /**
         * Return whether the parameter is declared with the given annotation type.
         *
         * @param annotationType the annotation type to look for
         * @see #getParameterAnnotation(Class)
         */
        @Override
        public <A extends Annotation> boolean hasParameterAnnotation(final @NonNull Class<A> annotationType) {
            return Arrays.stream(getParameterAnnotations()).anyMatch(annotationType::isInstance);
        }
    }
}
