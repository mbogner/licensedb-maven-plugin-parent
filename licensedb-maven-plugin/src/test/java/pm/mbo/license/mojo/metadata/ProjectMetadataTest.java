package pm.mbo.license.mojo.metadata;

import org.junit.Test;
import pm.mbo.license.test.BeanTestUtil;

import static org.junit.Assert.assertNotNull;

public class ProjectMetadataTest {

    @Test
    public void testBean() {
        new BeanTestUtil().checkBean(ProjectMetadata.class);
    }

    @Test
    public void testFullConstructor() {
        ProjectMetadata metadata = new ProjectMetadata("", "", "");
        assertNotNull(metadata.toString());
    }

}