package pm.mbo.license.test;

import org.junit.Test;
import pm.mbo.license.test.example.SomeEnum;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataGeneratorTest {

    @Test
    public void createInstanceOf_KnownTypes() throws Exception {
        final Class<?>[] classes = {
                String.class, Integer.class, BigDecimal.class, Double.class, Calendar.class, StringBuffer.class,
                SomeEnum.class, List.class, Set.class, Long.class
        };

        for (final Class<?> aClass : classes) {
            final Object instance = DataGenerator.createInstanceOf(aClass);
            assertNotNull(instance);
            assertTrue(aClass.isAssignableFrom(instance.getClass()));
        }

    }

    @Test
    public void createRandomString() throws Exception {
        assertNotNull(DataGenerator.createRandomString(20));
    }

    @Test
    public void createRandomBigDecimal() throws Exception {
        assertNotNull(DataGenerator.createRandomBigDecimal());
    }

    @Test
    public void createRandomDouble() throws Exception {
        assertNotNull(DataGenerator.createRandomDouble());
    }

    @Test
    public void createRandomInteger() throws Exception {
        assertNotNull(DataGenerator.createRandomInteger());
    }

    @Test
    public void createRandomLong() throws Exception {
        assertNotNull(DataGenerator.createRandomLong());
    }
}