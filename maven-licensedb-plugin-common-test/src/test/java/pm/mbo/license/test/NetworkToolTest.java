package pm.mbo.license.test;

import org.junit.Test;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

public class NetworkToolTest {

    @Test
    public void findRandomOpenPortOnAllLocalInterfaces() throws Exception {
        int port = NetworkTool.findRandomOpenPortOnAllLocalInterfaces();
        assertThat(port, greaterThanOrEqualTo(NetworkTool.PORT_MIN));
        assertThat(port, lessThanOrEqualTo(NetworkTool.PORT_MAX));
    }

}
