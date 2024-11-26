package com.kibberpunk.spring.boot.starter.jsonrpc.service.impl;

import com.kibberpunk.spring.boot.starter.jsonrpc.dto.JsonRpc20Request;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * {@link JsonRpc20ProtocolSupportImpl} context.
 *
 * @author kibberpunk
 */
@Getter
@Setter(AccessLevel.PROTECTED)
@Accessors(chain = true)
public class JsonRpc20RequestContext extends RequestContext<JsonRpc20Request> {
}
