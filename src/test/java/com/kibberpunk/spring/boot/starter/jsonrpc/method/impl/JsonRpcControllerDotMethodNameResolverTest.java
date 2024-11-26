package com.kibberpunk.spring.boot.starter.jsonrpc.method.impl;

import com.kibberpunk.spring.boot.starter.jsonrpc.context.TestJsonRpcAutoConfiguration;
import com.kibberpunk.spring.boot.starter.jsonrpc.controller.NemesisController;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodNameResolver;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link JsonRpcControllerDotMethodNameResolver} test
 *
 * @author kibberpunk
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestJsonRpcAutoConfiguration.class)
public class JsonRpcControllerDotMethodNameResolverTest {

    @Autowired
    private JsonRpcMethodNameResolver methodNameResolver;
    @Autowired
    private NemesisController nemesisController;
    @Autowired
    private ApplicationContext context;

    /**
     * Test {@link JsonRpcControllerDotMethodNameResolver#resolve(String[], String, Class, Object, Method)}
     */
    @DisplayName("Resolve method name")
    @Test
    public void resolve() {
        assertThrows(IllegalArgumentException.class, () -> methodNameResolver.resolve(
                new String[]{},
                StringUtils.EMPTY,
                nemesisController.getClass(),
                nemesisController,
                ReflectionUtils.getDeclaredMethods(NemesisController.class)[0]));
        assertThrows(IllegalArgumentException.class, () -> methodNameResolver.resolve(
                context.getBeanNamesForType(NemesisController.class),
                StringUtils.EMPTY,
                nemesisController.getClass(),
                nemesisController,
                ReflectionUtils.getDeclaredMethods(NemesisController.class)[0]));
        assertThrows(IllegalArgumentException.class, () -> methodNameResolver.resolve(
                new String[]{},
                "method",
                nemesisController.getClass(),
                nemesisController,
                ReflectionUtils.getDeclaredMethods(NemesisController.class)[0]));
        assertEquals("nemesisController.attack", methodNameResolver.resolve(
                context.getBeanNamesForType(NemesisController.class),
                "attack",
                nemesisController.getClass(),
                nemesisController,
                ReflectionUtils.getDeclaredMethods(NemesisController.class)[0]));
    }
}
