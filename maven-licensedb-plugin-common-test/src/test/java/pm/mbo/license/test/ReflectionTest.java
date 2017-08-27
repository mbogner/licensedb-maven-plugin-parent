package pm.mbo.license.test;

import org.junit.Test;
import pm.mbo.license.test.example.SomeBean;
import pm.mbo.license.test.example.SomeUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

public class ReflectionTest {

    @Test
    public void getExistingConstructor() throws Exception {
        final Constructor<SomeBean> constructor = Reflection.getExistingConstructor(SomeBean.class);
        assertNotNull(constructor);
    }

    @Test(expected = IllegalStateException.class)
    public void getExistingConstructor_NoExisting() throws Exception {
        Reflection.getExistingConstructor(SomeBean.class, Object.class, Object.class);
    }

    @Test
    public void getDeclaredFieldsOfHierarchy() throws Exception {
        final List<Field> fields = Reflection.getDeclaredFieldsOfHierarchy(SomeBean.class, new ArrayList<>());
        assertNotNull(fields);
        assertThat(fields.size(), greaterThan(0));
    }

    @Test
    public void getDeclaredMethodsOfHierarchy() throws Exception {
        final List<Method> methods = Reflection.getDeclaredMethodsOfHierarchy(SomeBean.class, new ArrayList<>());
        assertNotNull(methods);
        assertThat(methods.size(), greaterThan(0));
    }

    @Test
    public void findFieldByName() throws Exception {
        final Field field = Reflection.findFieldByName("blaaa", Reflection.getDeclaredFieldsOfHierarchy(SomeBean.class, new ArrayList<>()));
        assertNotNull(field);
        assertThat(field.getName(), equalTo("blaaa"));
    }

    @Test
    public void findFieldByName_unknownField() throws Exception {
        final Field field = Reflection.findFieldByName("xyz", Reflection.getDeclaredFieldsOfHierarchy(SomeBean.class, new ArrayList<>()));
        assertNull(field);
    }

    @Test
    public void findMethodByNameAndParams() throws Exception {
        final Method method = Reflection.findMethodByNameAndParams("toString", Reflection.getDeclaredMethodsOfHierarchy(SomeBean.class, new ArrayList<>()));
        assertNotNull(method);
        assertThat(method.getName(), equalTo("toString"));
    }

    @Test
    public void findMethodByNameAndParams_unknownMethod() throws Exception {
        final Method method = Reflection.findMethodByNameAndParams("xyz", Reflection.getDeclaredMethodsOfHierarchy(SomeBean.class, new ArrayList<>()));
        assertNull(method);
    }

    @Test
    public void findExistingMethodByNameAndParams() throws Exception {
        final Method method = Reflection.findExistingMethodByNameAndParams("toString", Reflection.getDeclaredMethodsOfHierarchy(SomeBean.class, new ArrayList<>()));
        assertNotNull(method);
        assertThat(method.getName(), equalTo("toString"));
    }

    @Test(expected = AssertionError.class)
    public void findExistingMethodByNameAndParams_unknownMethod() throws Exception {
        Reflection.findExistingMethodByNameAndParams("xyz", Reflection.getDeclaredMethodsOfHierarchy(SomeBean.class, new ArrayList<>()));
    }

    @Test
    public void compareParams() throws Exception {
        final Method method = Reflection.findExistingMethodByNameAndParams("equals", Reflection.getDeclaredMethodsOfHierarchy(SomeBean.class, new ArrayList<>()), Object.class);
        assertNotNull(method);
        assertThat(method.getName(), equalTo("equals"));

        assertTrue(Reflection.compareParams(method, Object.class));
    }

    @Test
    public void getSetterNameFor() throws Exception {
        final String name = Reflection.getSetterNameFor(Reflection.findFieldByName("blaaa", Reflection.getDeclaredFieldsOfHierarchy(SomeBean.class, new ArrayList<>())));
        assertThat(name, equalTo("setBlaaa"));
    }

    @Test
    public void getGetterNameFor() throws Exception {
        final String name = Reflection.getGetterNameFor(Reflection.findFieldByName("blaaa", Reflection.getDeclaredFieldsOfHierarchy(SomeBean.class, new ArrayList<>())));
        assertThat(name, equalTo("getBlaaa"));
    }

    @Test(expected = IllegalAccessError.class)
    public void testCallPrivateDefaultConstructor_utilClass() throws Throwable {
        Reflection.callPrivateDefaultConstructor(DatabaseTool.class);
    }

    @Test(expected = AssertionError.class)
    public void testCallPrivateDefaultConstructor_publicConstructor() throws Throwable {
        Reflection.callPrivateDefaultConstructor(SomeBean.class);
    }

    @Test
    public void testCallPrivateDefaultConstructor() throws Throwable {
        assertNotNull(Reflection.callPrivateDefaultConstructor(SomeUtil.class));
    }

}