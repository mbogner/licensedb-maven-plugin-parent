package pm.mbo.license.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public final class NetworkTool {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkTool.class);

    public static final int PORT_MIN = 0;
    public static final int PORT_MAX = (1 << 16) - 1;

    public static int findRandomOpenPortOnAllLocalInterfaces() throws IOException {
        try (final ServerSocket socket = new ServerSocket(0)) {
            final int port = socket.getLocalPort();
            LOG.trace("found free port {}", port);
            return port;
        }
    }

    public static int checkPort(final int port) {
        if (port < 0 || port > 0xFFFF)
            throw new IllegalArgumentException("port out of range:" + port);
        return port;
    }

    private NetworkTool() {
        throw new IllegalAccessError();
    }
}
