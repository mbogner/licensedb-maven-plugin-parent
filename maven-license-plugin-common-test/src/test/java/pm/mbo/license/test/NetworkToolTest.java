package pm.mbo.license.test;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class NetworkToolTest {

    @Test
    public void findRandomOpenPortOnAllLocalInterfaces() throws Exception {
        assertNotNull(NetworkTool.findRandomOpenPortOnAllLocalInterfaces());
    }

}