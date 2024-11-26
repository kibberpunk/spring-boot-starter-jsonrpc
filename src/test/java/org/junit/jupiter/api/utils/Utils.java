package org.junit.jupiter.api.utils;

import lombok.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author kibberpunk
 */
public class Utils {

    public static Method method(final @NonNull String name, final @NonNull Class<?> type) {
        return Arrays.stream(ReflectionUtils.getDeclaredMethods(type)).
                filter(method -> method.getName().equals(name)).findFirst()
                .orElseThrow();
    }

}
