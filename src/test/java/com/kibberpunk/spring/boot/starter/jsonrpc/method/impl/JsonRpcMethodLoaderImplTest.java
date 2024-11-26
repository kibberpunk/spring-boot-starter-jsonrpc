package com.kibberpunk.spring.boot.starter.jsonrpc.method.impl;

import com.kibberpunk.spring.boot.starter.jsonrpc.context.TestJsonRpcAutoConfiguration;
import com.kibberpunk.spring.boot.starter.jsonrpc.controller.NemesisController;
import com.kibberpunk.spring.boot.starter.jsonrpc.controller.T001Controller;
import com.kibberpunk.spring.boot.starter.jsonrpc.method.JsonRpcMethodLoader;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.utils.Utils;
import org.springdoc.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link JsonRpcMethodLoaderImpl} test
 *
 * @author kibberpunk
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestJsonRpcAutoConfiguration.class)
public class JsonRpcMethodLoaderImplTest {

    @Autowired
    private JsonRpcMethodLoader loader;
    @Autowired
    private NemesisController nemesisController;
    @Autowired
    private T001Controller t001Controller;

    @Test
    @DisplayName("Load methods")
    public void loadMethods() {
        assertEquals(5, ((JsonRpcMethodLoaderImpl) loader).getIndex().size());
        check("nemesisController.attack", nemesisController);
        check("nemesisController.mutate", nemesisController);
        check("nemesisController.battleExit", nemesisController);
        assertNull(check("t001Controller.sleep", t001Controller));
        check("t001Controller.attack", t001Controller);
        check("t001Controller.mutate", t001Controller);
        assertNull(check("t001Controller.sleep", t001Controller));
    }

    private Triple<Method, Object, List<MethodParameter>> check(
            final String name,
            final @NonNull Object controller) {
        Triple<Method, Object, List<MethodParameter>> box = loader.get(name);
        if (isEmpty(box)) {
            return null;
        }
        assertEquals(
                Utils.method(StringUtils.splitPreserveAllTokens(name, Constants.DOT)[1], controller.getClass()),
                box.getLeft());
        assertEquals(controller, box.getMiddle());
        return box;
    }
}
