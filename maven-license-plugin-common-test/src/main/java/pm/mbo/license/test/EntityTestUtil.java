package pm.mbo.license.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

public final class EntityTestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(EntityTestUtil.class);

    public static final List<Class<? extends Annotation>> DEFAULT_REQUIRED_ANNOTATIONS = new ArrayList<>();

    static {
        DEFAULT_REQUIRED_ANNOTATIONS.add(Entity.class);
        DEFAULT_REQUIRED_ANNOTATIONS.add(Table.class);
    }

    public static void checkEntity(final Class<?> entity) {
        checkEntity(entity, DEFAULT_REQUIRED_ANNOTATIONS);
    }

    public static void checkEntity(final Class<?> entity, final List<Class<? extends Annotation>> requiredAnnoations) {
        checkAnnoations(entity, requiredAnnoations);

        final List<Field> fields = Reflection.getDeclaredFieldsOfHierarchy(entity, new ArrayList<>());
        assertThat(fields.size(), greaterThan(0));

        final List<Method> methods = Reflection.getDeclaredMethodsOfHierarchy(entity, new ArrayList<>());
        assertThat(methods.size(), greaterThan(0));

        checkGetterSetter(fields, methods);

        LOG.trace("looking for required methods");
        Reflection.findExistingMethodByNameAndParams("toString", methods);
        Reflection.findExistingMethodByNameAndParams("hashCode", methods);
        Reflection.findExistingMethodByNameAndParams("equals", methods, Object.class);
    }


    private static void checkGetterSetter(final List<Field> fields, final List<Method> methods) {
        for (final Field field : fields) {
            final String getterName = Reflection.getGetterNameFor(field);
            final String setterName = Reflection.getSetterNameFor(field);

            LOG.trace("check for getter {}", getterName);
            final Method getterMethod = Reflection.findExistingMethodByNameAndParams(getterName, methods);
            assertEquals(field.getType(), getterMethod.getReturnType());

            LOG.trace("check for setter {}", setterName);
            Reflection.findExistingMethodByNameAndParams(setterName, methods, field.getType());
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
