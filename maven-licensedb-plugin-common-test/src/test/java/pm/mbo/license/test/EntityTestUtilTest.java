package pm.mbo.license.test;

import org.junit.Test;
import pm.mbo.license.test.example.SomeBean;

public class EntityTestUtilTest {

    @Test
    public void checkEntity_defaults() throws Exception {
        new EntityTestUtil().checkBean(SomeBean.class);
    }

    @Test(expected = AssertionError.class)
    public void checkEntity_null() throws Exception {
        new EntityTestUtil().checkBean(null, null);
    }

    @Test(expected = AssertionError.class)
    public void checkEntity_nullRequiredAnnotaitons() throws Exception {
        new EntityTestUtil().checkBean(SomeBean.class, null);
    }

}