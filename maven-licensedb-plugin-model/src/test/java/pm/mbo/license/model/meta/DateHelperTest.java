package pm.mbo.license.model.meta;

import org.junit.Test;
import pm.mbo.license.test.Reflection;

import java.util.Calendar;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DateHelperTest {
    @Test
    public void printable_null() throws Exception {
        assertNull(DateHelper.printable(null));
    }

    @Test
    public void printable() throws Exception {
        assertNotNull(DateHelper.printable(Calendar.getInstance()));
    }

    @Test(expected = IllegalAccessError.class)
    public void testDateHelper() throws Throwable {
        Reflection.callPrivateDefaultConstructor(DateHelper.class);
    }

}