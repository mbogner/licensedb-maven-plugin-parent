package pm.mbo.license.mojo.metadata;

import org.junit.Test;
import pm.mbo.license.test.BeanTestUtil;

public class ProjectMetadataTest {

    @Test
    public void testBean() {
        new BeanTestUtil().checkBean(ProjectMetadata.class);
    }

}