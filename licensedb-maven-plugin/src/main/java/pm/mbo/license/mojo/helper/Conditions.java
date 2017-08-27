package pm.mbo.license.mojo.helper;

import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public final class Conditions {

    public static void notNull(final String name, final Object obj) {
        if (null == name || name.isEmpty()) {
            throw new IllegalArgumentException("take the time to name the objects");
        }
        if (null == obj) {
            throw new IllegalArgumentException(String.format("%s must not be null", name));
        }
    }

    public static void notBlank(final String name, final String str) {
        notNull(name, str);
        if (str.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s must not be blank", name));
        }
    }

    public static void inList(final String name, final String value, final Set<String> possibilities) {
        notNull(name, value);
        notNull("possibilities", possibilities);
        if (!possibilities.contains(value)) {
            throw new IllegalArgumentException(String.format("%s has value %s which is not in (%s)", name, value, StringUtils.join(possibilities, ",")));
        }
    }

    private Conditions() {
        throw new IllegalAccessError();
    }
}
