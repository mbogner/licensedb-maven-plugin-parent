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

    @Test(expected = IllegalAccessError.class)
    public void testNetworkTool() throws Throwable {
        Reflection.callPrivateDefaultConstructor(NetworkTool.class);
    }

    @Test
    public void testCheckPort_low() {
        NetworkTool.checkPort(1);
    }

    @Test
    public void testCheckPort_high() {
        NetworkTool.checkPort(65535);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPort_tooLittle() {
        NetworkTool.checkPort(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPort_tooBig() {
        NetworkTool.checkPort(65536);
    }

}
