package pm.mbo.license.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public final class EntityTestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(EntityTestUtil.class);

    private static final List<Class<? extends Annotation>> DEFAULT_REQUIRED_ANNOTATIONS = Arrays.asList(Entity.class, Table.class);

    private static final List<String> PRIVATE_METHODS = Arrays.asList();
    private static final List<String> PROTECTED_METHODS = Arrays.asList("canEqual");

    public static <T> void checkEntity(final Class<T> entity) {
        checkEntity(entity, DEFAULT_REQUIRED_ANNOTATIONS);
    }

    public static <T> void checkEntity(final Class<T> entity, final List<Class<? extends Annotation>> requiredAnnoations) {
        assertNotNull(entity);
        assertNotNull(requiredAnnoations);
        try {
            checkAnnoations(entity, requiredAnnoations);

            final T instance = createInstance(entity);

            final List<Field> fields = Reflection.getDeclaredFieldsOfHierarchy(entity, new ArrayList<>());
            checkFields(entity, fields);

            final List<Method> methods = Reflection.getDeclaredMethodsOfHierarchy(entity, new ArrayList<>());
            checkMethods(entity, methods);

            checkGetterSetter(instance, fields, methods);

            // check some specific methods
            assertEquals(callMethod("toString", methods, instance), callMethod("toString", methods, instance));
            assertEquals(callMethod("hashCode", methods, instance), callMethod("hashCode", methods, instance));
            testEquals(methods, instance);

        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail(e.getMessage());
        }
    }

    private static <T> T createInstance(final Class<T> entity) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        LOG.trace("create instance of {} by default constructor", entity.getName());
        return Reflection.getExistingConstructor(entity).newInstance();
    }

    private static <T> void checkMethods(final Class<T> entity, final List<Method> methods) {
        assertNotNull(entity);
        assertNotNull(methods);
        assertThat(methods.size(), greaterThan(0));

        for (final Method method : methods) {
            final String name = String.format("method %s.%s", entity.getSimpleName(), method.getName());

            if (null != method.getAnnotation(PrePersist.class)
                    || null != method.getAnnotation(PostPersist.class)
                    || null != method.getAnnotation(PreUpdate.class)
                    || null != method.getAnnotation(PostUpdate.class)
                    || null != method.getAnnotation(PreRemove.class)
                    || null != method.getAnnotation(PostRemove.class)) {
                LOG.trace("{} is a lifecycle method and has to be private", name);
                checkPrivate(name, method.getModifiers());
                continue;
            }

            if (PRIVATE_METHODS.contains(method.getName())) {
                LOG.trace("{} has to be private", name);
                checkPrivate(name, method.getModifiers());
                continue;
            }
            if (PROTECTED_METHODS.contains(method.getName())) {
                LOG.trace("{} has to be protected", name);
                checkProtected(name, method.getModifiers());
                continue;
            }
            LOG.trace("{} has to be public", name);
            checkPublic(name, method.getModifiers());
        }

    }

    private static <T> void checkFields(final Class<T> entity, final List<Field> fields) {
        assertNotNull(entity);
        assertNotNull(fields);
        assertThat(fields.size(), greaterThan(0));

        for (final Field field : fields) {
            final Class<?> type = field.getType();
            LOG.trace("checking field access {}.{}", entity.getSimpleName(), field.getName());
            checkPrivate(String.format("field %s.%s", entity.getSimpleName(), field.getName()), field.getModifiers());
        }
    }

    private static void checkPrivate(final String name, final int modifiers) {
        assertNotNull(name);
        if (!Modifier.isPrivate(modifiers)) {
            fail(name + " must be private");
        }
    }

    private static void checkPublic(final String name, final int modifiers) {
        assertNotNull(name);
        if (!Modifier.isPublic(modifiers)) {
            fail(name + " must be public");
        }
    }

    private static void checkProtected(final String name, final int modifiers) {
        assertNotNull(name);
        if (!Modifier.isProtected(modifiers)) {
            fail(name + " must be protected");
        }
    }

    private static <T> void testEquals(final List<Method> methods, final T instance) throws InvocationTargetException, IllegalAccessException {
        assertNotNull(methods);
        assertNotNull(instance);
        LOG.trace("testing {}.equals", instance.getClass().getSimpleName());
        final Method equals = Reflection.findExistingMethodByNameAndParams("equals", methods, Object.class);
        assertTrue((Boolean) equals.invoke(instance, instance));
        assertFalse((Boolean) equals.invoke(instance, new Object[]{null}));
        assertFalse((Boolean) equals.invoke(instance, new Object[]{new String()}));
    }

    private static <T> Object callMethod(final String name, final List<Method> methods, final T instance) throws InvocationTargetException, IllegalAccessException {
        assertNotNull(name);
        assertNotNull(methods);
        assertNotNull(instance);
        LOG.trace("testing {}.{}", instance.getClass().getSimpleName(), name);
        final Method toString = Reflection.findExistingMethodByNameAndParams(name, methods);
        final Object result = toString.invoke(instance);
        assertNotNull(result);
        return result;
    }

    private static <T> void checkGetterSetter(final T instance, final List<Field> fields, final List<Method> methods) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        assertNotNull(instance);
        assertNotNull(fields);
        assertNotNull(methods);
        for (final Field field : fields) {
            final String name = instance.getClass().getSimpleName();

            final String getterName = Reflection.getGetterNameFor(field);
            final String getterSign = String.format("%s %s.%s()", field.getType().getSimpleName(), name, getterName);
            final String setterName = Reflection.getSetterNameFor(field);
            final String setterSign = String.format("void %s.%s(%s)", name, setterName, field.getType().getSimpleName());

            LOG.trace("check existence of getter '{}'", getterSign);
            final Method getterMethod = Reflection.findExistingMethodByNameAndParams(getterName, methods);
            assertEquals(field.getType(), getterMethod.getReturnType());

            LOG.trace("check existence of setter '{}'", setterSign);
            final Method setterMethod = Reflection.findExistingMethodByNameAndParams(setterName, methods, field.getType());

            LOG.trace("calling '{}' and '{}'", getterSign, setterSign);
            assertThat(getterMethod.invoke(instance), nullValue());
            Object data = DataGenerator.createInstanceOf(field.getType());
            assertThat(setterMethod.invoke(instance, data), nullValue());
            assertThat(getterMethod.invoke(instance), equalTo(data));
            assertThat(setterMethod.invoke(instance, new Object[]{null}), nullValue());
            assertThat(getterMethod.invoke(instance), nullValue());
        }
    }

    private static void checkAnnoations(final Class<?> entity, final List<Class<? extends Annotation>> requiredAnnoations) {
        assertNotNull(entity);
        if (null != requiredAnnoations) {
            for (Class<? extends Annotation> requiredAnnoation : requiredAnnoations) {
                LOG.trace("check {} for {}", entity.getName(), requiredAnnoation.getName());
                assertNotNull(String.format("Entity has to declare @%s", requiredAnnoation.getSimpleName()), entity.getAnnotation(requiredAnnoation));
            }
        }
    }

    private EntityTestUtil() {
        throw new IllegalAccessError();
    }
}
