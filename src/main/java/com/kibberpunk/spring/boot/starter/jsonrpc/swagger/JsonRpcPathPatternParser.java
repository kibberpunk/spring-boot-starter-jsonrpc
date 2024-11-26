package com.kibberpunk.spring.boot.starter.jsonrpc.swagger;

import lombok.NonNull;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * JSON-RPC {@link PathPatternParser}.
 *
 * @author kibberpunk
 */
public class JsonRpcPathPatternParser extends PathPatternParser {

    /**
     * Constructor.
     */
    public JsonRpcPathPatternParser() {
        setPathOptions(PathContainer.Options.MESSAGE_ROUTE);
    }

    /**
     * Prepare the given pattern for use in matching to full URL paths.
     * <p>By default, prepend a leading slash if needed for non-empty patterns.
     *
     * @param pattern the pattern to initialize
     * @return the updated pattern
     * @since 5.2.25
     */
    @Override
    public @NonNull String initFullPathPattern(final @NonNull String pattern) {
        return pattern;
    }
}
