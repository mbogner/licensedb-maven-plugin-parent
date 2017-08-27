package pm.mbo.license.model.meta;

import java.util.Calendar;

public final class DateHelper {

    public static String printable(final Calendar cal) {
        if (null == cal) {
            return null;
        }
        return String.valueOf(cal.getTime());
    }

    private DateHelper() {
        throw new IllegalAccessError();
    }
}
