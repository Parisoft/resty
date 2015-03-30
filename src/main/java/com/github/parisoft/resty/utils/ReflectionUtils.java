/*
 *    Copyright 2013-2014 Parisoft Team
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.github.parisoft.resty.utils;

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
