package pm.mbo.license.mojo.helper;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.junit.Test;

import java.util.HashSet;

public class ConditionsTest {

    @Test
    public void notNull() throws Exception {
        Conditions.notNull("test", new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void notNull_emptyName() throws Exception {
        Conditions.notNull("", new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void notNull_null() throws Exception {
        Conditions.notNull(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notNull_null1() throws Exception {
        Conditions.notNull(null, new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void notNull_null2() throws Exception {
        Conditions.notNull("asd", null);
    }

    @Test
    public void notBlank() throws Exception {
        Conditions.notBlank("test", "12");
    }

    @Test(expected = IllegalArgumentException.class)
    public void notBlank_blank() throws Exception {
        Conditions.notBlank("test", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void notBlank_null() throws Exception {
        Conditions.notBlank(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notBlank_null1() throws Exception {
        Conditions.notBlank(null, "222");
    }

    @Test(expected = IllegalArgumentException.class)
    public void notBlank_null2() throws Exception {
        Conditions.notBlank("asd", null);
    }

    @Test
    public void inList() throws Exception {
        Conditions.inList("bla", "value", new HashSet<String>(Arrays.asList(new String[]{"a", "value"})));
    }

    @Test(expected = IllegalArgumentException.class)
    public void inList_notInList() throws Exception {
        Conditions.inList("bla", "value", new HashSet<String>(Arrays.asList(new String[]{"a", "b"})));
    }

    @Test(expected = IllegalArgumentException.class)
    public void inList_null3() throws Exception {
        Conditions.inList("bla", "value", null);
    }
}