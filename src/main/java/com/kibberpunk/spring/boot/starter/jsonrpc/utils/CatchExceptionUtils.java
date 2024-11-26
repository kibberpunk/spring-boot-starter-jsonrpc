package com.kibberpunk.spring.boot.starter.jsonrpc.utils;

import lombok.NonNull;

import java.util.Optional;
import java.util.function.Function;

/**
 * Exception catch utils.
 *
 * @author kibberpunk
 */
public final class CatchExceptionUtils {

    /**
     * Private constructor to utils class.
     */
    private CatchExceptionUtils() {
    }

    /**
     * If an exception occurs, this method calls the wrapper resolver for the exception.
     *
     * @param supplier          Target method to process
     * @param exceptionResolver Exception resolver
     * @param <R>               Return type
     * @return Return {@link Supplier} result if exception not present
     */
    public static <R> R catchException(
            final @NonNull Supplier<R> supplier,
            final @NonNull Function<Throwable, RuntimeException> exceptionResolver) throws RuntimeException {
        try {
            return supplier.get();
        } catch (final Throwable throwable) {
            throw Optional.ofNullable(exceptionResolver.apply(throwable))
                    .orElse(new RuntimeException(throwable));
        }
    }

    /**
     * Represents a supplier of results.
     *
     * <p>There is no requirement that a new or distinct result be returned each
     * time the supplier is invoked.
     *
     * <p>This is a <a href="package-summary.html">functional interface</a>
     * whose functional method is {@link #get()}.
     *
     * @param <T> the type of results supplied by this supplier
     * @since 1.8
     */
    @FunctionalInterface
    public interface Supplier<T> {

        /**
         * Gets a result.
         *
         * @return a result
         */
        T get() throws Throwable;
    }

}
