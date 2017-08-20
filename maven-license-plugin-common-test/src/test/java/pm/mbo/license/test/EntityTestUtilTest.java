package pm.mbo.license.test;

import org.junit.Test;
import pm.mbo.license.test.example.SomeBean;

public class EntityTestUtilTest {

    @Test
    public void checkEntity_defaults() throws Exception {
        EntityTestUtil.checkEntity(SomeBean.class);
    }

    @Test(expected = AssertionError.class)
    public void checkEntity_null() throws Exception {
        EntityTestUtil.checkEntity(null, null);
    }

    @Test(expected = AssertionError.class)
    public void checkEntity_nullRequiredAnnotaitons() throws Exception {
        EntityTestUtil.checkEntity(SomeBean.class, null);
    }

}