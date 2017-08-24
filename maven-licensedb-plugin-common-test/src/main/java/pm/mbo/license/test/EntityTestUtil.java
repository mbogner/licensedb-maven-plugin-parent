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
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class EntityTestUtil extends BeanTestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(EntityTestUtil.class);

    private static final List<Class<? extends Annotation>> DEFAULT_REQUIRED_ANNOTATIONS = Arrays.asList(Entity.class, Table.class);

    @Override
    protected List<Class<? extends Annotation>> getRequiredAnnotations() {
        return DEFAULT_REQUIRED_ANNOTATIONS;
    }

    @Override
    protected List<Class<? extends Annotation>> getPrivateMethodAnnotations() {
        return Arrays.asList(
                PrePersist.class,
                PostPersist.class,
                PreUpdate.class,
                PostUpdate.class,
                PreRemove.class,
                PostRemove.class);
    }

}
