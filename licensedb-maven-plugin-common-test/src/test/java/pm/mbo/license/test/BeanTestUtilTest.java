package pm.mbo.license.test;

import org.junit.Test;
import pm.mbo.license.test.example.AbstractBean;
import pm.mbo.license.test.example.SomeBean;

import static org.junit.Assert.assertNotNull;

public class BeanTestUtilTest {

    @Test
    public void getRequiredAnnotations() throws Exception {
        assertNotNull(new BeanTestUtil().getRequiredAnnotations());
    }

    @Test
    public void getPrivateMethods() throws Exception {
        assertNotNull(new BeanTestUtil().getPrivateMethods());
    }

    @Test
    public void getProtectedMethods() throws Exception {
        assertNotNull(new BeanTestUtil().getProtectedMethods());
    }

    @Test
    public void getPrivateMethodAnnotations() throws Exception {
        assertNotNull(new BeanTestUtil().getPrivateMethodAnnotations());
    }

    @Test
    public void checkPublic() throws Exception {
        new BeanTestUtil().checkPublic("name", SomeBean.class.getDeclaredConstructor().getModifiers());
    }

    @Test(expected = AssertionError.class)
    public void checkPublic_null() throws Exception {
        new BeanTestUtil().checkPublic(null, 0);
    }

    @Test(expected = AssertionError.class)
    public void checkPublic_notPublic() throws Exception {
        new BeanTestUtil().checkPublic(null, NetworkTool.class.getDeclaredConstructor().getModifiers());
    }

    @Test
    public void checkProtected() throws Exception {
        new BeanTestUtil().checkProtected("name", AbstractBean.class.getDeclaredMethod("someMethod").getModifiers());
    }

    @Test(expected = AssertionError.class)
    public void checkProtected_null() throws Exception {
        new BeanTestUtil().checkProtected(null, 0);
    }

    @Test(expected = AssertionError.class)
    public void checkProtected_notProtected() throws Exception {
        new BeanTestUtil().checkProtected(null, NetworkTool.class.getDeclaredConstructor().getModifiers());
    }

    @Test
    public void checkPrivate() throws Exception {
        new BeanTestUtil().checkPrivate("name", NetworkTool.class.getDeclaredConstructor().getModifiers());
    }

    @Test(expected = AssertionError.class)
    public void checkPrivate_null() throws Exception {
        new BeanTestUtil().checkPrivate(null, 0);
    }

    @Test(expected = AssertionError.class)
    public void checkPrivate_notPrivate() throws Exception {
        new BeanTestUtil().checkPrivate(null, SomeBean.class.getDeclaredConstructor().getModifiers());
    }
}