package pm.mbo.license.test;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public final class Reflection {

    public static <T> Constructor<T> getExistingConstructor(final Class<T> clazz, final Class<?>... params) {
        try {
            return clazz.getDeclaredConstructor(params);
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
            throw new IllegalStateException(e);
        }
    }

    public static List<Field> getDeclaredFieldsOfHierarchy(final Class<?> clazz, final List<Field> result) {
        assertNotNull(clazz);
        assertNotNull(result);
        if (clazz == Object.class) {
            return result;
        }
        result.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return getDeclaredFieldsOfHierarchy(clazz.getSuperclass(), result);
    }

    public static List<Method> getDeclaredMethodsOfHierarchy(final Class<?> clazz, final List<Method> result) {
        assertNotNull(clazz);
        assertNotNull(result);
        if (clazz == Object.class) {
            return result;
        }
        result.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        return getDeclaredMethodsOfHierarchy(clazz.getSuperclass(), result);
    }

    public static Field findFieldByName(final String name, final List<Field> fields) {
        assertNotNull(name);
        assertNotNull(fields);
        for (final Field field : fields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    public static Method findMethodByNameAndParams(final String name, final List<Method> methods, final Class<?>... params) {
        assertNotNull(name);
        assertNotNull(methods);
        assertNotNull(params);
        for (final Method method : methods) {
            if (method.getName().equals(name)
                    && method.getParameterCount() == params.length) {
                if (!compareParams(method, params)) {
                    continue;
                }
                return method;
            }
        }
        return null;
    }

    public static Method findExistingMethodByNameAndParams(final String name, final List<Method> methods, final Class<?>... params) {
        final Method method = findMethodByNameAndParams(name, methods, params);
        assertNotNull(method);
        return method;
    }

    public static boolean compareParams(final Method method, final Class<?>... params) {
        assertNotNull(method);
        assertNotNull(params);
        if (method.getParameterCount() != params.length) {
            return false;
        }
        for (int i = 0; i < method.getParameterCount(); i++) {
            if (method.getParameterTypes()[i] != params[i]) {
                return false;
            }
        }
        return true;
    }

    public static String getSetterNameFor(final Field field) {
        assertNotNull(field);
        return String.format("set%s", StringUtils.capitalize(field.getName()));
    }

    public static String getGetterNameFor(final Field field) {
        assertNotNull(field);
        return String.format("get%s", StringUtils.capitalize(field.getName()));
    }

    private Reflection() {
        throw new IllegalAccessError();
    }
}
