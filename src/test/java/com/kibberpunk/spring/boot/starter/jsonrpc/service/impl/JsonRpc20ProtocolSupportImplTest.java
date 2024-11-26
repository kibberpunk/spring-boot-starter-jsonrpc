package com.kibberpunk.spring.boot.starter.jsonrpc.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kibberpunk.spring.boot.starter.jsonrpc.context.TestJsonRpcAutoConfiguration;
import com.kibberpunk.spring.boot.starter.jsonrpc.controller.AbstractTyrantController;
import com.kibberpunk.spring.boot.starter.jsonrpc.controller.NemesisController;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Request;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Response;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpcEmptyRequestBodyException;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpcMethodNotFoundException;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpcRequestIdNotFoundException;
import com.kibberpunk.spring.boot.starter.jsonrpc.exception.JsonRpcRequestReceivingException;
import com.kibberpunk.spring.boot.starter.jsonrpc.service.JsonRpcProtocolSupport;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link JsonRpc20ProtocolSupportImpl} test
 *
 * @author kibberpunk
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestJsonRpcAutoConfiguration.class)
public class JsonRpc20ProtocolSupportImplTest {

    @Autowired
    private JsonRpcProtocolSupport<JsonRpc20Request, JsonRpc20Response, JsonRpc20RequestContext> support;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Get context")
    public void getContext() {
        final JsonRpc20RequestContext context = support.context();
        assertNotNull(context);
    }

    @Test
    @DisplayName("Get request from null body")
    public void getRequestFromNullBody() {
        assertThrows(JsonRpcEmptyRequestBodyException.class, () ->
                request(null));
    }

    @Test
    @DisplayName("Get request from empty string body")
    public void getRequestFromEmptyStringBody() {
        assertThrows(JsonRpcEmptyRequestBodyException.class, () ->
                request(""));
    }

    @Test
    @DisplayName("Get request from invalid json body")
    public void getRequestFromInvalidJsonBody() {
        assertThrows(JsonRpcRequestReceivingException.class, () ->
                request("lsjrhfskhfgsd"));
    }

    @Test
    @DisplayName("Get request from empty json body")
    public void getRequestFromEmptyJsonBody() {
        assertThrows(JsonRpcEmptyRequestBodyException.class, () ->
                request("{}"));
    }

    @Test
    @DisplayName("Get request with null parameters")
    public void getRequestWithNullStringParameters() {
        String body = """
                {
                  "id" : null,
                  "method" : null,
                  "params" : null,
                  "jsonrpc" : null
                }""";
        assertEquals(body, toJson(request(body).getRequest()));
    }

    @Test
    @DisplayName("Get request with empty string parameters")
    public void getRequestWithEmptyStringParameters() {
        String body = """
                {
                  "id" : " ",
                  "method" : "           ",
                  "params" : "    ",
                  "jsonrpc" : ""
                }""";
        assertEquals(body, toJson(request(body).getRequest()));
    }

    @Test
    @DisplayName("Get request with not empty parameters")
    public void getRequestWithNotEmptyParameters() {
        String body = """
                {
                  "id" : "123",
                  "method" : "method",
                  "params" : {
                    "p1" : "string",
                    "p2" : 2,
                    "p3" : true
                  },
                  "jsonrpc" : "2.0"
                }""";
        assertEquals(body, toJson(request(body).getRequest()));
    }

    @Test
    @DisplayName("Get method if input method parameter is null")
    public void getMethodIfInputMethodParameterIsNull() {
        String body = """
                {
                  "id" : null,
                  "method" : null,
                  "params" : null,
                  "jsonrpc" : null
                }""";
        JsonRpc20RequestContext context = request(body);
        assertThrows(JsonRpcMethodNotFoundException.class, () ->
                support.methodName(context));
    }

    @Test
    @DisplayName("Get method if input method parameter is empty")
    public void getMethodIfInputMethodParameterIsEmpty() {
        String body = """
                {
                  "id" : null,
                  "method" : "      ",
                  "params" : null,
                  "jsonrpc" : null
                }""";
        JsonRpc20RequestContext context = request(body);
        assertThrows(JsonRpcMethodNotFoundException.class, () ->
                support.methodName(context));
    }

    @Test
    @DisplayName("Get method")
    public void getMethod() {
        String body = """
                {
                  "id" : null,
                  "method" : "method123",
                  "params" : null,
                  "jsonrpc" : null
                }""";
        JsonRpc20RequestContext context = request(body);
        assertEquals("method123", support.methodName(context));
    }

    @Test
    @DisplayName("Get method and trim method name")
    public void getMethodAndTrimMethodName() {
        String body = """
                {
                  "id" : null,
                  "method" : "    method123                    ",
                  "params" : null,
                  "jsonrpc" : null
                }""";
        JsonRpc20RequestContext context = request(body);
        assertEquals("method123", support.methodName(context));
    }

    @Test
    @DisplayName("Get JsonRpcRequestId")
    public void getJsonRpcRequestId() {
        String body = """
                {
                  "id" : null
                }""";
        JsonRpc20RequestContext context = request(body);

        assertNull(parameter(1, "attack", NemesisController.class, context));
        assertNull(parameter(2, "attack", NemesisController.class, context));
        assertEquals(0, parameter(3, "attack", NemesisController.class, context));
        assertNull(parameter(4, "attack", NemesisController.class, context));
        assertEquals(0.0, parameter(5, "attack", NemesisController.class, context));
        assertNull(parameter(6, "attack", NemesisController.class, context));
        assertEquals(0L, parameter(7, "attack", NemesisController.class, context));
        assertNull(parameter(8, "attack", NemesisController.class, context));
        assertEquals(0f, parameter(9, "attack", NemesisController.class, context));
        assertNull(parameter(10, "attack", NemesisController.class, context));
        assertNull(parameter(11, "attack", NemesisController.class, context));

        body = """
                {
                  "id" : "StringRequestId"
                }""";
        assertThat(parameter(1, "attack", NemesisController.class, request(body)))
                .isInstanceOf(String.class)
                .isEqualTo("StringRequestId");

        body = """
                {
                  "id" : 2
                }""";
        assertThat(parameter(2, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Integer.class)
                .isEqualTo(2);

        body = """
                {
                  "id" : 3
                }""";
        assertThat(parameter(3, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Integer.class)
                .isEqualTo(3);

        body = """
                {
                  "id" : 4.0
                }""";
        assertThat(parameter(4, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Double.class)
                .isEqualTo(4.0);

        body = """
                {
                  "id" : 5.0
                }""";
        assertThat(parameter(5, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Double.class)
                .isEqualTo(5.0);

        body = """
                {
                  "id" : 6
                }""";
        assertThat(parameter(6, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Long.class)
                .isEqualTo(6L);

        body = """
                {
                  "id" : 7
                }""";
        assertThat(parameter(7, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Long.class)
                .isEqualTo(7L);

        body = """
                {
                  "id" : 8.88888888
                }""";
        assertThat(parameter(8, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Float.class)
                .isEqualTo(8.88888888f);

        body = """
                {
                  "id" : 9.99999999
                }""";
        assertThat(parameter(9, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Float.class)
                .isEqualTo(9.99999999f);

        body = """
                {
                  "id" : "68998eaf-dee3-4652-90fe-776a397ed1ab"
                }""";
        assertThat(parameter(10, "attack", NemesisController.class, request(body)))
                .isInstanceOf(UUID.class)
                .isEqualTo(UUID.fromString("68998eaf-dee3-4652-90fe-776a397ed1ab"));
    }

    @Test
    @DisplayName("Get JsonRpcRequestId is required")
    public void getJsonRpcRequestIdIsRequired() {
        String body = """
                {
                  "id" : "68998eaf-dee3-4652-90fe-776a397ed1ab"
                }""";
        assertThat(parameter(10, "attack", NemesisController.class, request(body)))
                .isInstanceOf(UUID.class)
                .isEqualTo(UUID.fromString("68998eaf-dee3-4652-90fe-776a397ed1ab"));

        assertThrows(JsonRpcRequestIdNotFoundException.class, () ->
                parameter(0, "attack", NemesisController.class, request("""
                        {
                          "id" : null
                        }""")));
    }

    @Test
    @DisplayName("Get JsonRpcFullObject parameter")
    public void getJsonRpcFullObject() {
        String body = """
                {
                  "id" : "123",
                  "method" : "method123",
                  "params" : {
                    "uuid" : "68998eaf-dee3-4652-90fe-776a397ed1ab",
                    "message" : "xflvhdgdddd",
                    "d" : 6345.4535,
                    "l" : 23453454,
                    "i" : 2345,
                    "parameter" : {
                      "d" : 274.85
                    }
                  },
                  "jsonrpc" : "2.0"
                }""";
        assertThat(parameter(13, "attack", NemesisController.class, request(body)))
                .isInstanceOf(String.class)
                .isEqualTo("""
                        {"uuid":"68998eaf-dee3-4652-90fe-776a397ed1ab","message":"xflvhdgdddd","d":6345.4535,"l":23453454,"i":2345,"parameter":{"d":274.85}}""");

        assertThat(parameter(14, "attack", NemesisController.class, request(body)))
                .isInstanceOf(TreeNode.class)
                .matches(request -> {
                    assertEquals("""
                            {
                              "uuid" : "68998eaf-dee3-4652-90fe-776a397ed1ab",
                              "message" : "xflvhdgdddd",
                              "d" : 6345.4535,
                              "l" : 23453454,
                              "i" : 2345,
                              "parameter" : {
                                "d" : 274.85
                              }
                            }""", toJson(request));
                    return true;
                });

        assertThat(parameter(15, "attack", NemesisController.class, request(body)))
                .isInstanceOf(AbstractTyrantController.Request.class)
                .matches(request -> {
                    assertEquals("""
                            {
                              "uuid" : "68998eaf-dee3-4652-90fe-776a397ed1ab",
                              "message" : "xflvhdgdddd",
                              "d" : 6345.4535,
                              "l" : 23453454,
                              "i" : 2345,
                              "f" : 0.0,
                              "parameter" : {
                                "d" : 274.85
                              }
                            }""", toJson(request));
                    return true;
                });

        body = """
                {
                  "id" : "123",
                  "method" : "method123",
                  "params" : {},
                  "jsonrpc" : "2.0"
                }""";
        assertThat(parameter(13, "attack", NemesisController.class, request(body)))
                .isInstanceOf(String.class)
                .isEqualTo("{}");
        assertThat(parameter(14, "attack", NemesisController.class, request(body)))
                .isInstanceOf(TreeNode.class)
                .matches(request -> {
                    assertEquals("{ }", toJson(request));
                    return true;
                });
        assertThat(parameter(15, "attack", NemesisController.class, request(body)))
                .isInstanceOf(AbstractTyrantController.Request.class)
                .matches(request -> {
                    assertEquals("""
                            {
                              "uuid" : null,
                              "message" : null,
                              "d" : 0.0,
                              "l" : null,
                              "i" : 0,
                              "f" : 0.0,
                              "parameter" : null
                            }""", toJson(request));
                    return true;
                });

    }

    @Test
    @DisplayName("Get parameter by JsonRpcRequestObjectParameterName")
    public void getParameterByJsonRpcRequestObjectParameterName() {
        String body = """
                {
                  "id" : "123",
                  "method" : "method123",
                  "params" : {
                    "uuid" : "68998eaf-dee3-4652-90fe-776a397ed1ab",
                    "message" : "xflvhdgdddd",
                    "d" : 6345.4535,
                    "l" : 23453454,
                    "i" : 2345,
                    "parameter" : {
                      "d" : 274.85
                    }
                  },
                  "jsonrpc" : "2.0"
                }""";

        assertThat(parameter(18, "attack", NemesisController.class, request(body)))
                .isInstanceOf(UUID.class)
                .isEqualTo(UUID.fromString("68998eaf-dee3-4652-90fe-776a397ed1ab"));
        assertThat(parameter(19, "attack", NemesisController.class, request(body)))
                .isInstanceOf(String.class)
                .isEqualTo("xflvhdgdddd");
        assertThat(parameter(20, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Integer.class)
                .isEqualTo(2345);
        assertThat(parameter(21, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Integer.class)
                .isEqualTo(2345);
        assertThat(parameter(22, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Double.class)
                .isEqualTo(6345.4535);
        assertThat(parameter(23, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Double.class)
                .isEqualTo(6345.4535);
        assertThat(parameter(24, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Long.class)
                .isEqualTo(23453454L);
        assertThat(parameter(25, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Long.class)
                .isEqualTo(23453454L);
        assertThat(parameter(26, "attack", NemesisController.class, request(body)))
                .isInstanceOf(AbstractTyrantController.Request.Parameter.class)
                .matches(request -> {
                    assertEquals("""
                            {
                              "d" : 274.85
                            }""", toJson(request));
                    return true;
                });

        body = """
                {
                  "id" : "123",
                  "method" : "method123",
                  "params" : {
                    "uuidddd" : "68998eaf-dee3-4652-90fe-776a397ed1ab",
                    "messageeeee" : "xflvhdgdddd",
                    "ddd" : 6345.4535,
                    "lll" : 23453454,
                    "iiii" : 2345,
                    "parameterttt" : {
                      "d" : 274.85
                    }
                  },
                  "jsonrpc" : "2.0"
                }""";
        assertThat(parameter(18, "attack", NemesisController.class, request(body)))
                .isNull();
        assertThat(parameter(19, "attack", NemesisController.class, request(body)))
                .isNull();
        assertThat(parameter(20, "attack", NemesisController.class, request(body)))
                .isNull();
        assertThat(parameter(21, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Integer.class)
                .isEqualTo(0);
        assertThat(parameter(22, "attack", NemesisController.class, request(body)))
                .isNull();
        assertThat(parameter(23, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Double.class)
                .isEqualTo(0.0);
        assertThat(parameter(24, "attack", NemesisController.class, request(body)))
                .isNull();
        assertThat(parameter(25, "attack", NemesisController.class, request(body)))
                .isInstanceOf(Long.class)
                .isEqualTo(0L);
        assertThat(parameter(26, "attack", NemesisController.class, request(body)))
                .isNull();
    }

    protected MethodParameter methodParameter(int index, String method, Class<?> controllerType) {
        return new MethodParameter(Utils.method(method, controllerType), index);
    }

    @SuppressWarnings("all")
    private Object parameter(int index, String method, Class<?> controllerType, JsonRpc20RequestContext context) {
        return support.parameter(methodParameter(index, method, controllerType), context);
    }

    private JsonRpc20RequestContext request(final String body) {
        JsonRpc20RequestContext context = context(support.context(), body, null);
        context.setRequest(support.request(context));
        return context;
    }

    @SuppressWarnings("all")
    private JsonRpc20RequestContext context(
            final @NonNull JsonRpc20RequestContext context,
            final String body,
            final HttpServletRequest httpServletRequest) {
        context.setBody(body);
        context.setHttpServletRequest(httpServletRequest);
        return context;
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
