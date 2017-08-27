package pm.mbo.license.mojo.metadata;

import org.junit.Test;
import pm.mbo.license.test.BeanTestUtil;

public class ArtifactMetadataTest {

    @Test
    public void testBean() {
        new BeanTestUtil().checkBean(ArtifactMetadata.class);
    }

}