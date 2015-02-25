package org.parisoft.resty.utils;

import static org.parisoft.resty.utils.ReflectionUtils.containsConstructor;
import static org.parisoft.resty.utils.ReflectionUtils.containsMethod;
import static org.parisoft.resty.utils.ReflectionUtils.getConstructor;
import static org.parisoft.resty.utils.ReflectionUtils.getMethod;

@SuppressWarnings("unchecked")
public class ObjectUtils {

    private static final String METHOD_VALUE_OF_STRING = "valueOf";

    public static boolean isInstanciableFromString(Class<?> someClass) {
        return containsConstructor(someClass, String.class)
                || containsMethod(someClass, METHOD_VALUE_OF_STRING, String.class);
    }

    public static <T> T newInstanceFromString(Class<T> someClass, String string) {
        if (containsConstructor(someClass, String.class)) {
            try {
                return getConstructor(someClass, String.class).newInstance(string);
            } catch (Exception ignored) {
            }
        }

        if (containsMethod(someClass, METHOD_VALUE_OF_STRING, String.class)) {
            try {
                return (T) getMethod(someClass, METHOD_VALUE_OF_STRING, String.class).invoke(null, string);
            } catch (Exception ignored) {
            }
        }

        return null;
    }

    public static boolean isPrimitive(Object object) {
        return object.getClass().isPrimitive();
    }
}
