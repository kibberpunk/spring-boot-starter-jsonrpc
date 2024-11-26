package com.kibberpunk.spring.boot.starter.jsonrpc.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kibberpunk.spring.boot.starter.jsonrpc.context.TestJsonRpcAutoConfiguration;
import com.kibberpunk.spring.boot.starter.jsonrpc.controller.AbstractTyrantController;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Response;
import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpcError;
import com.kibberpunk.spring.boot.starter.jsonrpc.service.JsonRpcService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link JsonRpc20ProtocolService} test
 *
 * @author kibberpunk
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestJsonRpcAutoConfiguration.class)
public class JsonRpc20ProtocolServiceTest {

    @Autowired
    private JsonRpcService processor;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Call with invalid json")
    public void callWithInvalidJson() {
        String body = """
                {xfdgdg
                  "id" : "123",
                  "method" : "method123",
                  "params" : {
                    "message" : "xflvhdgdddd",
                    "i" : 2345,
                    "d" : 6345.4535,
                    "l" : 23453454,
                    "f" : 723453454,
                    "uuid" : "6d0d420b-9530-4122-8c21-8bac5d4f7cbc",
                    "parameter" : {
                      "d" : 274.85
                    }
                  },
                  "jsonrpc" : "2.0"
                }""";
        JsonRpc20Response response = (JsonRpc20Response) processor.process(body, new MockMultipartHttpServletRequest());
        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals(JsonRpcError.Code.PARSE_ERROR.getCode(), response.getError().getCode());
        assertEquals(JsonRpcError.Code.PARSE_ERROR.getMessage(), response.getError().getMessage());
    }

    @Test
    @DisplayName("Call with empty json")
    public void callWithEmptyJson() {
        String body = """
                {}""";
        JsonRpc20Response response = (JsonRpc20Response) processor.process(body, new MockMultipartHttpServletRequest());
        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals(JsonRpcError.Code.INVALID_REQUEST.getCode(), response.getError().getCode());
        assertEquals(JsonRpcError.Code.INVALID_REQUEST.getMessage(), response.getError().getMessage());
    }

    @Test
    @DisplayName("Call with empty request id")
    public void callWithEmptyRequestId() {
        String body = """
                {
                  "method" : "nemesisController.battleExit",
                  "params" : {
                    "message" : "xflvhdgdddd",
                    "i" : 2345,
                    "d" : 6345.4535,
                    "l" : 23453454,
                    "f" : 723453454,
                    "uuid" : "6d0d420b-9530-4122-8c21-8bac5d4f7cbc",
                    "parameter" : {
                      "d" : 274.85
                    }
                  },
                  "jsonrpc" : "2.0"
                }""";
        JsonRpc20Response response = (JsonRpc20Response) processor.process(body, new MockMultipartHttpServletRequest());
        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals(JsonRpcError.Code.REQUEST_ID_IS_EMPTY.getCode(), response.getError().getCode());
        assertEquals(JsonRpcError.Code.REQUEST_ID_IS_EMPTY.getMessage(), response.getError().getMessage());
    }

    @Test
    @DisplayName("Call with not found method")
    public void callWithNotFoundMethod() {
        String body = """
                {
                  "id" : "123",
                  "method" : "method123",
                  "params" : {
                    "message" : "xflvhdgdddd",
                    "i" : 2345,
                    "d" : 6345.4535,
                    "l" : 23453454,
                    "f" : 723453454,
                    "uuid" : "6d0d420b-9530-4122-8c21-8bac5d4f7cbc",
                    "parameter" : {
                      "d" : 274.85
                    }
                  },
                  "jsonrpc" : "2.0"
                }""";
        JsonRpc20Response response = (JsonRpc20Response) processor.process(body, new MockMultipartHttpServletRequest());
        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals(JsonRpcError.Code.METHOD_NOT_FOUND.getCode(), response.getError().getCode());
        assertEquals(JsonRpcError.Code.METHOD_NOT_FOUND.getMessage(), response.getError().getMessage());
    }

    @Test
    @DisplayName("Call with non matched by type parameter")
    public void callWithInvalidParameter() {
        String body = """
                {
                  "id" : "123",
                  "method" : "nemesisController.mutate",
                  "params" : {
                    "message" : "xflvhdgdddd",
                    "i" : "nhhhhhhhh",
                    "d" : "333333333",
                    "l" : "bchhhh",
                    "f" : "fggg",
                    "uuid" : "68998eeeeeeeeeeaf-dee3-4652-90fe-776a397ed1ab",
                    "parameter" : {
                      "d" : 274.85
                    }
                  },
                  "jsonrpc" : "2.0"
                }""";
        JsonRpc20Response response = (JsonRpc20Response) processor.process(body, new MockMultipartHttpServletRequest());
        assertNotNull(response);
        assertNull(response.getError());
    }

    @Test
    @DisplayName("Call void method")
    public void callWithVoidMethod() {
        String body = """
                {
                  "id" : "123",
                  "method" : "nemesisController.mutate",
                  "params" : {
                    "message" : "xflvhdgdddd",
                    "i" : 2345,
                    "d" : 6345.4535,
                    "l" : 23453454,
                    "f" : 723453454,
                    "uuid" : "6d0d420b-9530-4122-8c21-8bac5d4f7cbc",
                    "parameter" : {
                      "d" : 274.85
                    }
                  },
                  "jsonrpc" : "2.0"
                }""";
        JsonRpc20Response response = (JsonRpc20Response) processor.process(body, new MockMultipartHttpServletRequest());
        assertNotNull(response);
        assertNull(response.getResult());
        assertNull(response.getError());
    }

    @Test
    @DisplayName("Call method")
    @SuppressWarnings("all")
    public void callMethod() throws JsonProcessingException {
        String body = """
                {
                  "id" : "68998eaf-dee3-4652-90fe-776a397ed1ab",
                  "method" : "nemesisController.battleExit",
                  "params" : {
                    "message" : "xflvhdgdddd",
                    "i" : 2345,
                    "d" : 6345.4535,
                    "l" : 23453454,
                    "f" : 723453454,
                    "uuid" : "6d0d420b-9530-4122-8c21-8bac5d4f7cbc",
                    "parameter" : {
                      "d" : 274.85
                    }
                  },
                  "jsonrpc" : "2.0"
                }""";
        JsonRpc20Response jsonRpc20Response = (JsonRpc20Response) processor.process(body, new MockMultipartHttpServletRequest());
        assertNotNull(jsonRpc20Response);
        assertNull(jsonRpc20Response.getError());
        assertNotNull(jsonRpc20Response.getResult());
        AbstractTyrantController.Request.Response response = (AbstractTyrantController.Request.Response) jsonRpc20Response.getResult();

        assertEquals(UUID.fromString("68998eaf-dee3-4652-90fe-776a397ed1ab"), response.getRequestIdInUUIDFormat());
        assertEquals("68998eaf-dee3-4652-90fe-776a397ed1ab", response.getRequestIdInStringFormat());
        //assertEquals(, response.getRequestIdInJsonNodeFormat());

        String requestObjectString = """
                {
                  "uuid" : "6d0d420b-9530-4122-8c21-8bac5d4f7cbc",
                  "message" : "xflvhdgdddd",
                  "d" : 6345.4535,
                  "l" : 23453454,
                  "i" : 2345,
                  "f" : 7.2345344E8,
                  "parameter" : {
                    "d" : 274.85
                  }
                }""";
        assertEquals(requestObjectString, objectMapper.writeValueAsString(response.getRequestObject()));
        assertEquals("{\"message\":\"xflvhdgdddd\",\"i\":2345,\"d\":6345.4535,\"l\":23453454,\"f\":723453454,\"uuid\":\"6d0d420b-9530-4122-8c21-8bac5d4f7cbc\",\"parameter\":{\"d\":274.85}}", response.getRequestObjectInStringFormat());
        assertEquals(objectMapper.readTree("{\"message\":\"xflvhdgdddd\",\"i\":2345,\"d\":6345.4535,\"l\":23453454,\"f\":723453454,\"uuid\":\"6d0d420b-9530-4122-8c21-8bac5d4f7cbc\",\"parameter\":{\"d\":274.85}}"), response.getRequestObjectInJsonNodeFormat());

        assertEquals("xflvhdgdddd", response.getMessageRequestObjectParameter());
        assertEquals(2345, response.getIRequestObjectParameter());
        assertEquals(6345.4535, response.getDRequestObjectParameter());
        assertEquals(23453454L, response.getLRequestObjectParameter());
        assertEquals(723453454f, response.getFRequestObjectParameter());
        assertEquals(UUID.fromString("6d0d420b-9530-4122-8c21-8bac5d4f7cbc"), response.getUuidRequestObjectParameter());
        assertEquals("""
                {
                  "d" : 274.85
                }""", objectMapper.writeValueAsString(response.getParameterRequestObjectParameter()));
        assertNull(response.getMessageRequestObjectParameterIgnored());
        assertEquals(0, response.getIiRequestObjectParameterIgnored());
        assertEquals(0.0, response.getDRequestObjectParameterIgnored());
        assertEquals(0L, response.getLRequestObjectParameterIgnored());
        assertEquals(0f, response.getFRequestObjectParameterIgnored());
        assertNull(response.getUuidRequestObjectParameterIgnored());
        assertNull(response.getUuidRequestObjectParameterIgnored());
        assertNull(response.getIRequestObjectParameterIgnored());
    }
}
