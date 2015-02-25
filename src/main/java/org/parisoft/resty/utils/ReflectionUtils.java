package org.parisoft.resty.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static boolean containsMethod(Class<?> someClass, String methodName, Class<?>... parameterTypes) {
        return getMethod(someClass, methodName, parameterTypes) != null;
    }

    public static boolean containsConstructor(Class<?> someClass, Class<?> parameterTypes) {
        return getConstructor(someClass, parameterTypes) != null;
    }

    public static Method getMethod(Class<?> someClass, String methodName, Class<?>... parameterTypes) {
        try {
            return someClass.getDeclaredMethod(methodName, parameterTypes);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> Constructor<T> getConstructor(Class<T> someClass, Class<?> parameterTypes) {
        try {
            return someClass.getDeclaredConstructor(parameterTypes);
        } catch (Exception e) {
            return null;
        }
    }
}
