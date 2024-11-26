package com.kibberpunk.spring.boot.starter.jsonrpc.utils;

import org.slf4j.helpers.MessageFormatter;

/**
 * Messages utils.
 *
 * @author kibberpunk
 */
public final class FormatUtils {

    /**
     * Private constructor to utils class.
     */
    private FormatUtils() {
    }

    /**
     * Get formatted message. Based on SLF4J easy {@link MessageFormatter}.
     *
     * @param message Message to format
     * @param args    Message arguments
     * @return Formatted message
     */
    public static String format(final String message, final Object... args) {
        return MessageFormatter.arrayFormat(message, args).getMessage();
    }
}
