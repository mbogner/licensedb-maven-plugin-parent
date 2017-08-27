package pm.mbo.license.test;

import org.apache.commons.text.RandomStringGenerator;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

@SuppressWarnings("WeakerAccess")
public final class DataGenerator {

    public static <T> Object createInstanceOf(final Class<T> type) throws IllegalAccessException, InstantiationException {
        assertNotNull(type);
        if (type.isAssignableFrom(String.class)) {
            return createRandomString(20);
        }
        if (type.isAssignableFrom(BigDecimal.class)) {
            return createRandomBigDecimal();
        }
        if (type.isAssignableFrom(Integer.class)) {
            return createRandomInteger();
        }
        if (type.isAssignableFrom(Double.class)) {
            return createRandomDouble();
        }
        if (type.isAssignableFrom(Long.class)) {
            return createRandomLong();
        }
        if (type.isAssignableFrom(Calendar.class)) {
            return Calendar.getInstance();
        }
        if (type.isAssignableFrom(Date.class)) {
            return Calendar.getInstance().getTime();
        }
        if (type.isAssignableFrom(List.class)) {
            return new ArrayList<T>();
        }
        if (type.isAssignableFrom(Set.class)) {
            return new HashSet<T>();
        }
        if (type.isEnum()) {
            return createEnumInstanceOf(type);
        }
        return type.newInstance();
    }

    public static String createRandomString(final int length) {
        assertThat(length, greaterThan(0));
        final RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        return generator.generate(length);
    }

    public static BigDecimal createRandomBigDecimal() {
        return new BigDecimal(String.valueOf(createRandomDouble()));
    }

    public static Double createRandomDouble() {
        return new Random().nextDouble();
    }

    public static Integer createRandomInteger() {
        return new Random().nextInt();
    }

    public static Long createRandomLong() {
        return new Random().nextLong();
    }

    public static Object createEnumInstanceOf(final Class<?> type) {
        assertNotNull(type);
        assertTrue(type.isEnum());
        @SuppressWarnings("unchecked") final Enum[] constants = ((Class<Enum>) type).getEnumConstants();
        assertThat(constants.length, greaterThan(0));
        return constants[0];
    }

    private DataGenerator() {
        throw new IllegalAccessError();
    }
}
