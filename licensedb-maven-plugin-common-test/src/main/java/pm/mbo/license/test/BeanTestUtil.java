package pm.mbo.license.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class BeanTestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(BeanTestUtil.class);

    private static final List<String> DEFAULT_PROTECTED_METHODS = new ArrayList<>(1);

    static {
        DEFAULT_PROTECTED_METHODS.add("canEqual");
    }

    public <T> void checkBean(final Class<T> clazz) {
        checkBean(clazz, getRequiredAnnotations());
    }

    public <T> void checkBean(final Class<T> clazz, final List<Class<? extends Annotation>> requiredAnnoations) {
        assertNotNull(clazz);
        assertNotNull(requiredAnnoations);
        try {
            checkAnnoations(clazz, requiredAnnoations);

            final T instance = createInstance(clazz);

            final List<Field> fields = Reflection.getDeclaredFieldsOfHierarchy(clazz, new ArrayList<>());
            checkFields(clazz, fields);

            final List<Method> methods = Reflection.getDeclaredMethodsOfHierarchy(clazz, new ArrayList<>());
            checkMethods(clazz, methods);

            checkGetterSetter(instance, fields, methods);

            // check some specific methods

            final Object toStringResult = callMethod("toString", methods, instance);
            assertNotNull(toStringResult);
            assertEquals(toStringResult, callMethod("toString", methods, instance));

            final Object hashCodeResult = callMethod("hashCode", methods, instance);
            assertNotNull(hashCodeResult);
            assertEquals(hashCodeResult, callMethod("hashCode", methods, instance));

            testEquals(methods, instance);

        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail(e.getMessage());
        }
    }

    protected <T> T createInstance(final Class<T> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        LOG.trace("create instance of {} by default constructor", clazz.getName());
        return Reflection.getExistingConstructor(clazz).newInstance();
    }

    protected boolean methodHasOneOfAnnotations(final Method method, final List<Class<? extends Annotation>> annotations) {
        assertNotNull(method);
        assertNotNull(annotations);
        for (final Class<? extends Annotation> annotation : annotations) {
            if (null != method.getAnnotation(annotation)) {
                return true;
            }
        }
        return false;
    }

    protected <T> void checkMethods(final Class<T> clazz, final List<Method> methods) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        assertNotNull(clazz);
        assertNotNull(methods);
        assertThat(methods.size(), greaterThan(0));

        for (final Method method : methods) {
            final String name = String.format("method %s.%s", clazz.getSimpleName(), method.getName());

            if (methodHasOneOfAnnotations(method, getPrivateMethodAnnotations())) {
                LOG.trace("{} is a annotated method that has to be private", name);
                checkPrivate(name, method.getModifiers());
                callMethod(method, clazz.newInstance());
                continue;
            }
            if (getPrivateMethods().contains(method.getName())) {
                LOG.trace("{} has to be private", name);
                checkPrivate(name, method.getModifiers());
                continue;
            }
            if (getProtectedMethods().contains(method.getName())) {
                LOG.trace("{} has to be protected", name);
                checkProtected(name, method.getModifiers());
                continue;
            }

            if (method.getName().startsWith("set") ||
                    method.getName().startsWith("get") ||
                    method.getName().equals("equals") ||
                    method.getName().equals("toString") ||
                    method.getName().equals("hashCode")) {
                LOG.trace("{} has to be public", name);
                checkPublic(name, method.getModifiers());
            }
        }

    }

    protected <T> void checkFields(final Class<T> clazz, final List<Field> fields) {
        assertNotNull(clazz);
        assertNotNull(fields);
        assertThat(fields.size(), greaterThan(0));

        for (final Field field : fields) {
            final Class<?> type = field.getType();
            LOG.trace("checking field access {}.{}", clazz.getSimpleName(), field.getName());
            checkPrivate(String.format("field %s.%s", clazz.getSimpleName(), field.getName()), field.getModifiers());
        }
    }

    protected void checkPrivate(final String name, final int modifiers) {
        assertNotNull(name);
        if (!Modifier.isPrivate(modifiers)) {
            fail(name + " must be private");
        }
    }

    protected void checkPublic(final String name, final int modifiers) {
        assertNotNull(name);
        if (!Modifier.isPublic(modifiers)) {
            fail(name + " must be public");
        }
    }

    protected void checkProtected(final String name, final int modifiers) {
        assertNotNull(name);
        if (!Modifier.isProtected(modifiers)) {
            fail(name + " must be protected");
        }
    }

    protected <T> void testEquals(final List<Method> methods, final T instance) throws InvocationTargetException, IllegalAccessException {
        assertNotNull(methods);
        assertNotNull(instance);
        LOG.trace("testing {}.equals", instance.getClass().getSimpleName());
        final Method equals = Reflection.findExistingMethodByNameAndParams("equals", methods, Object.class);
        assertTrue((Boolean) equals.invoke(instance, instance));
        assertFalse((Boolean) equals.invoke(instance, new Object[]{null}));
        assertFalse((Boolean) equals.invoke(instance, new Object[]{"asd"}));
    }

    protected <T> Object callMethod(final String name, final List<Method> methods, final T instance) throws InvocationTargetException, IllegalAccessException {
        assertNotNull(name);
        assertNotNull(methods);
        assertNotNull(instance);
        return callMethod(Reflection.findExistingMethodByNameAndParams(name, methods), instance);
    }

    protected <T> Object callMethod(final Method method, final T instance) throws InvocationTargetException, IllegalAccessException {
        assertNotNull(method);
        assertNotNull(instance);
        LOG.trace("testing {}.{}", instance.getClass().getSimpleName(), method.getName());
        if (!Modifier.isPublic(method.getModifiers())) {
            method.setAccessible(true);
        }
        return method.invoke(instance);
    }

    protected <T> void checkGetterSetter(final T instance, final List<Field> fields, final List<Method> methods) throws InvocationTargetException, IllegalAccessException, InstantiationException {
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

    protected void checkAnnoations(final Class<?> clazz, final List<Class<? extends Annotation>> requiredAnnoations) {
        assertNotNull(clazz);
        if (null != requiredAnnoations) {
            for (Class<? extends Annotation> requiredAnnoation : requiredAnnoations) {
                LOG.trace("check {} for {}", clazz.getName(), requiredAnnoation.getName());
                assertNotNull(String.format("class has to declare @%s", requiredAnnoation.getSimpleName()), clazz.getAnnotation(requiredAnnoation));
            }
        }
    }

    protected List<Class<? extends Annotation>> getRequiredAnnotations() {
        return Collections.emptyList();
    }

    protected List<String> getPrivateMethods() {
        return Collections.emptyList();
    }

    protected List<String> getProtectedMethods() {
        return DEFAULT_PROTECTED_METHODS;
    }

    protected List<Class<? extends Annotation>> getPrivateMethodAnnotations() {
        return Collections.emptyList();
    }
}
